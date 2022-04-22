import java.util.Optional;
import java.util.Scanner;

class Player {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int baseX = in.nextInt();
        int baseY = in.nextInt();
        int heroesPerPlayer = in.nextInt();
        GameState state = new GameState(baseX, baseY, heroesPerPlayer);

        // game loop
        //noinspection InfiniteLoopStatement
        while (true) {
            state.updateState(in);
            for (int i = 0; i < heroesPerPlayer; i++) {
                if (!state.getMonsters().isEmpty()) {
                    Entity hero = state.getMyHeroes().values().toArray(new Entity[0])[i];
                    AttributeMapper mapper = new AttributeMapper(state, hero);

                    hero.setCurrentTarget(MonsterAttributes.NVL);
                    Optional<MonsterAttributes> selectedMonster = state.getMonsters().stream()
                            .map(mapper::calculateAttributes)
                            .filter(a -> !a.isTargetedByOtherHero())
                            .min(Helpers::compareAttributes);

                    //noinspection OptionalGetWithoutIsPresent
                    MonsterAttributes target = selectedMonster.orElse(state.getMonsters().stream()
                            .map(mapper::calculateAttributes)
                            .min(Helpers::compareAttributes).get());

                    hero.setCurrentTarget(target);
                    Log.log("main", target.debug(hero, state));
                } else {
                    HeroCommands.takeReadyPosition(i, baseX, baseY);
                }
            }


            if (!state.getMonsters().isEmpty()) {
                boolean windCast = false;
                for (int i = 0; i < heroesPerPlayer; i++) {
                    Entity hero = state.getMyHeroes().values().toArray(new Entity[0])[i];
                    MonsterAttributes target = hero.getCurrentTarget();
                    if (target.getDistanceToBase() <= GameParameters.BASE_FIGHTING_AREA) {
                        if (hero.distanceTo(target.getMonster()) < 1280) {
                            windCast = HeroCommands.castWind(state.getBasePosition(), target, windCast);
                        } else {
                            HeroCommands.move(target);
                        }
                    } else {
                        Log.log("main", "No targets in defensive area");
                        HeroCommands.takeReadyPosition(i, baseX, baseY);
                    }
                }
            }
        }
    }
}