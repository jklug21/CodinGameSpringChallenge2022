package spring2022;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;
import spring2022.behavior.HeroBehavior;
import spring2022.behavior.HeroBehaviorContainer;
import spring2022.domain.Entity;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;
import spring2022.io.DerivedScanner;
import spring2022.io.EntityData;
import spring2022.io.InitialData;
import spring2022.io.RoundState;
import spring2022.strategy.GameStrategy;
import spring2022.strategy.MixedStrategy;
import spring2022.util.AttributeMapper;
import spring2022.util.HeroCommands;
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
        GameState state = new GameState(initialData, strategy.getInitialBehavior());

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

            Hero[] heroes = state.getMyHeroes().values().toArray(new Hero[0]);
            for (int i = 0; i < state.getHeroesPerPlayer(); i++) {
                Hero hero = heroes[i];
                HeroBehavior heroBehavior = heroBehaviors.get(hero.getId() % 3).getBehavior();
                if (hero.getCurrentTarget() != null && state.getMonsters().stream().noneMatch(m -> m.getId() == hero.getCurrentTarget().getEntity().getId())) {
                    hero.clearTarget();
                }

                AttributeMapper mapper = new AttributeMapper(state, hero);

                Optional<InteractionAttributes> selectedMonster = Stream.concat(state.getMonsters().stream(),
                        state.getOppHeroes().stream())
                        .map(mapper::calculateAttributes)
                        .filter(a -> !a.isTargetedByOtherHero())
                        .filter(heroBehavior::considerEnemy)
                        .min(heroBehavior::sortEnemies);

                InteractionAttributes target = selectedMonster.orElseGet(() -> state.getMonsters().stream()
                        .map(mapper::calculateAttributes)
                        .filter(heroBehavior::considerEnemy)
                        .min(heroBehavior::sortEnemies).orElse(null));

                hero.setCurrentTarget(target);
                if (target != null) {
                    Log.log("main", hero.getId() + ": " + target.debugShort(hero));
                    hero.setNextAction(heroBehavior.getNextAction(target));
                }

                if (!hero.hasNextAction()) {
                    hero.setNextAction(HeroCommands.move(heroBehavior.getIdleCoordinate(state.getOwnBase(), state.getEnemyBase(), i), "boring"));
                }
            }

            for (int i = 0; i < state.getHeroesPerPlayer(); i++) {
                heroes[i].executeAction();
            }
        }
    }
}