package spring2022;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import spring2022.behavior.HeroBehavior;
import spring2022.behavior.HeroClass;
import spring2022.commands.HeroCommands;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;
import spring2022.io.DerivedScanner;
import spring2022.io.EntityData;
import spring2022.io.InitialData;
import spring2022.io.RoundState;
import spring2022.strategy.BattlefieldAnalyzer;
import spring2022.strategy.Flags;
import spring2022.strategy.GameStrategy;
import spring2022.strategy.impl.MixedStrategy;
import spring2022.util.AttributeMapper;
import spring2022.util.Log;

class Player {
    public static void main(String[] args) throws FileNotFoundException {
        GameParameters.setReRunInputs(false);
        GameParameters.setCaptureInputs(false);
        new Player().start();
    }

    private void start() throws FileNotFoundException {
        Scanner in;
        if (GameParameters.isReRunInputs()) {
            in = new Scanner(new FileReader("src/main/resources/in.txt"));
        } else {
            in = new Scanner(System.in);
        }
        DerivedScanner scanner = new DerivedScanner(in);
        GameStrategy strategy = new MixedStrategy();
        InitialData initialData = scanner.getObjectFromInput(InitialData.class);
        BattlefieldAnalyzer analyzer = new BattlefieldAnalyzer();
        GameState state = GameState.init(initialData, strategy.getInitialBehavior(), analyzer);

        while (in.hasNext()) {
            // re-evaluate strategy

            // adapt behavior
            strategy.adaptBehavior();

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
                HeroBehavior heroBehavior = hero.getBehavior();
                if (state.getHeroesAffectedByMagic().contains(hero.getId() % 3) && !hero.isShielded()) {
                    if(heroBehavior.getHeroClass() == HeroClass.DEFENDER) {
                        Flags.getInstance().raiseFlag(Flags.DEFENDER_INFLUENCED);
                    }
                    hero.setNextAction(HeroCommands.shield(hero.getId()));
                } else {
                    hero.clearTargetIfGone();

                    AttributeMapper mapper = new AttributeMapper(state, hero);
                    //stop at 119
                    InteractionAttributes target = Stream.concat(state.getMonsters().values().stream(), state.getOppHeroes().values().stream())
                            .map(mapper::calculateAttributes)
                            .filter(heroBehavior::considerEnemy)
                            .min(heroBehavior::sortEnemies).orElse(null);

                    if (target != null) {
                        hero.setCurrentTarget(target);
                        Log.log("main", hero.getId() + " [" + heroBehavior.getClass().getSimpleName() + "]: " + target.debugShort(hero));
                        hero.setNextAction(heroBehavior.getNextAction(target));
                    }

                    if (!hero.hasNextAction()) {
                        hero.setNextAction(HeroCommands.move(heroBehavior.getIdleCoordinate(i), "boring"));
                    }
                }
            }

            /*
            List<InteractionAttributes> targets = Arrays.stream(heroes).map(Hero::getCurrentTarget).collect(Collectors.toList());

            int[][] permutations = Helpers.getPermutations(new int[]{0, 1, 2});
            int bestPermutation = -1;
            int bestPermutationValue = Integer.MAX_VALUE;
            for (int i = 0; i < permutations.length; i++) {
                int[] permutation = permutations[i];
                int distance = 0;
                for (int j = 0; j < 3; j++) {
                    distance += heroes[j].distanceTo(targets.get(permutation[j]).getEntity());
                }
                if (distance < bestPermutationValue) {
                    bestPermutationValue = distance;
                    bestPermutation = i;
                }
            }

            for (int i = 0; i < 3; i++) {
                int newIndex = permutations[bestPermutation][i];
                heroes[i].setCurrentTarget(heroes[newIndex].getCurrentTarget());
                heroes[i].setNextAction(heroes[newIndex].getNextAction());
            }
*/
            for (int i = 0; i < state.getHeroesPerPlayer(); i++) {
                Hero hero = heroes[i];
                hero.executeAction();
            }
        }
    }
}