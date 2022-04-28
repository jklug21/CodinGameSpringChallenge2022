package spring2022.behavior;

import java.util.Comparator;
import java.util.Optional;
import spring2022.GameState;
import spring2022.commands.HeroCommand;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;
import spring2022.util.Constants;
import spring2022.util.Coordinate;
import spring2022.commands.HeroCommands;

public class InterceptorHeroBehavior implements HeroBehavior {
    @Override
    public int sortEnemies(InteractionAttributes m1, InteractionAttributes m2) {
        return (int) (m1.getDistanceToBase() - m2.getDistanceToBase());
    }

    @Override
    public boolean considerEnemy(InteractionAttributes m) {
        return m.getDistanceToHero() <= 2200;
    }

    @Override
    public Coordinate getIdleCoordinate(Coordinate ownBase, Coordinate enemyBase, int i) {
        return new Coordinate(5000, 5000);
    }

    @Override
    public HeroCommand getNextAction(InteractionAttributes interactionAttributes) {
        GameState state = GameState.get();
        Entity target = state.getAnalyzer().getInterceptHero();
        Hero hero = interactionAttributes.getHero();
        Coordinate base = state.getOwnBase();
        Coordinate enemyBase = state.getEnemyBase();
        if (state.getRoundState().getMyMana() >= 50) {
            Optional<Entity> closestMonster = state.getMonsters().values().stream()
                    .filter(e -> e.getThreatFor() != Faction.MONSTER)
                    .min(getEntityComparator(hero, base));
            if (closestMonster.isPresent()) {
                Entity entity = closestMonster.get();
                if (entity.distanceTo(hero) < Constants.CONTROL_RANGE) {
                    if (entity.distanceTo(base) > 5400) {

                        int dx = (int) (Math.random() * 5000d);
                        int dy = (int) (Math.random() * 5000d);
                        Coordinate enemyBaseDirection = new Coordinate(Math.abs(enemyBase.getX() - dx), Math.abs(enemyBase.getY() - dy));
                        return HeroCommands.control(entity.getId(), enemyBaseDirection, "Good boy");
                    } else {
                        return HeroCommands.castWindTowards(enemyBase, "*slap*");
                    }
                }
            }
        }

        double distanceToBase = target.distanceTo(base);
        if (distanceToBase < 4000) {
            int x = (int) ((target.getPosition().getX() / distanceToBase) * 5000);
            int y = (int) ((target.getPosition().getY() / distanceToBase) * 5000);
            return HeroCommands.move(new Coordinate(x, y), "");
        } else {
            int x;
            int y;
            if (base.getX() == 0) {
                x = (int) ((target.getPosition().getX() / distanceToBase) * (distanceToBase + 100));
                y = (int) ((target.getPosition().getY() / distanceToBase) * (distanceToBase + 100));
            } else {
                x = (int) ((target.getPosition().getX() / distanceToBase) * (distanceToBase - 100));
                y = (int) ((target.getPosition().getY() / distanceToBase) * (distanceToBase - 100));
            }
            return HeroCommands.move(new Coordinate(x, y), "");
        }
    }

    private Comparator<Entity> getEntityComparator(Hero hero, Coordinate base) {
        return (m1, m2) -> {
            if (m1.distanceTo(base) > 5400 && m2.distanceTo(base) < 5400) {
                return -1;
            } else if (m1.distanceTo(base) < 5400 && m2.distanceTo(base) > 5400) {
                return 1;
            } else {
                return (int) (m2.distanceTo(hero) - m1.distanceTo(hero));
            }
        };
    }
}
