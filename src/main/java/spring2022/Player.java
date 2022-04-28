package spring2022;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;
import spring2022.behavior.HeroBehavior;
import spring2022.behavior.HeroBehaviorContainer;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;
import spring2022.io.DerivedScanner;
import spring2022.io.EntityData;
import spring2022.io.InitialData;
import spring2022.io.RoundState;
import spring2022.strategy.GameStrategy;
import spring2022.strategy.MixedStrategy;
import spring2022.util.AttributeMapper;
import spring2022.commands.HeroCommands;
import spring2022.util.Log;

class Player {
    public static void main(String[] args) {
        new Player().start();
    }

    private void start() {
        Scanner in = new Scanner(System.in);
        DerivedScanner scanner = new DerivedScanner(in);
        GameStrategy strategy = new MixedStrategy();
        InitialData initialData = scanner.getObjectFromInput(InitialData.class);
        BattlefieldAnalyzer analyzer = new BattlefieldAnalyzer();
        GameState state = GameState.init(initialData, strategy.getInitialBehavior(), analyzer);

        // game loop
        //noinspection InfiniteLoopStatement
        while (true) {
            // re-evaluate strategy

            // adapt behavior
            List<HeroBehaviorContainer> heroBehaviors = state.getHeroBehaviors();
            strategy.adaptBehavior(heroBehaviors);

            RoundState roundState = scanner.getObjectFromInput(RoundState.class);
            List<EntityData> entities = new ArrayList<>(roundState.getEntityCount());
            for (int i = 0; i < roundState.getEntityCount(); i++) {
                entities.add(scanner.getObjectFromInput(EntityData.class));
            }
            state.updateState(roundState, entities);

            analyzer.analyzeBattlefield(state);

            Hero[] heroes = state.getMyHeroes().values().toArray(new Hero[0]);
            for (int i = 0; i < state.getHeroesPerPlayer(); i++) {
                Hero hero = heroes[i];
                if (state.getHeroesAffectedByMagic().contains(hero.getId() % 3) && !hero.isShielded()) {
                    hero.setNextAction(HeroCommands.shield(hero.getId()));
                } else {
                    HeroBehavior heroBehavior = heroBehaviors.get(hero.getId() % 3).getBehavior();
                    int currentTargetId;
                    if (hero.getCurrentTarget() == null) {
                        currentTargetId = -1;
                    } else {
                        if (state.getMonsters().keySet().stream().noneMatch(monsterId -> monsterId == hero.getCurrentTarget().getEntity().getId())) {
                            hero.clearTarget();
                            currentTargetId = -1;
                        } else {
                            currentTargetId = hero.getCurrentTarget().getEntity().getId();
                        }
                    }

                    AttributeMapper mapper = new AttributeMapper(state, hero);

                    Optional<InteractionAttributes> selectedMonster = Stream.concat(state.getMonsters().values().stream(),
                            state.getOppHeroes().values().stream())
                            .map(mapper::calculateAttributes)
                            .filter(a -> !a.isTargetedByOtherHero() || a.getEntity().getId() == currentTargetId)
                            .filter(heroBehavior::considerEnemy)
                            .min(heroBehavior::sortEnemies);

                    InteractionAttributes target = selectedMonster.orElseGet(() -> state.getMonsters().values().stream()
                            .map(mapper::calculateAttributes)
                            .filter(heroBehavior::considerEnemy)
                            .min(heroBehavior::sortEnemies).orElse(null));

                    hero.setCurrentTarget(target);
                    if (target != null) {
                        Log.log("main", hero.getId() + " [" + heroBehavior.getClass().getSimpleName() + "]: " + target.debugShort(hero));
                        hero.setNextAction(heroBehavior.getNextAction(target));
                    }

                    if (!hero.hasNextAction()) {
                        hero.setNextAction(HeroCommands.move(heroBehavior.getIdleCoordinate(state.getOwnBase(), state.getEnemyBase(), i), "boring"));
                    }
                }
            }

            for (int i = 0; i < state.getHeroesPerPlayer(); i++) {
                heroes[i].executeAction();
            }
        }
    }
}