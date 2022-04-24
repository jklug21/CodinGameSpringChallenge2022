package spring2022.behavior;

import spring2022.GameState;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;
import spring2022.util.Constants;
import spring2022.util.Coordinate;
import spring2022.util.HeroCommands;

import java.util.Comparator;
import java.util.Optional;

public class InterceptorHeroBehavior implements HeroBehavior {
    @Override
    public int sortEnemies(InteractionAttributes m1, InteractionAttributes m2) {
        return (int) (m1.getDistanceToBase() - m2.getDistanceToBase());
    }

    @Override
    public boolean considerEnemy(InteractionAttributes m) {
        return m.getEntity().getFaction() == Faction.ENEMY;
    }

    @Override
    public Coordinate getIdleCoordinate(Coordinate ownBase, Coordinate enemyBase, int i) {
        return new Coordinate(5000, 5000);
    }

    @Override
    public Runnable getNextAction(InteractionAttributes interactionAttributes) {
        GameState state = interactionAttributes.getState();
        Hero hero = interactionAttributes.getHero();
        Coordinate base = state.getOwnBase();
        Coordinate enemyBase = state.getEnemyBase();
        if (state.getRoundState().getMyMana() >= 50) {
            Optional<Entity> closestMonster = state.getMonsters().stream()
                    .filter(e -> e.getThreatFor() == Faction.OWN)
                    .min(getEntityComparator(hero, base));
            if (closestMonster.isPresent()) {
                Entity entity = closestMonster.get();
                if (entity.distanceTo(hero) < Constants.CONTROL_RANGE) {
                    if (entity.distanceTo(base) > 5400) {
                        return HeroCommands.control(entity.getId(), enemyBase, "Good boy");
                    } else {
                        return HeroCommands.castWindTowards(enemyBase, "*slap*");
                    }
                }
            }
        }

        double distanceToBase = interactionAttributes.getDistanceToBase();
        if (distanceToBase < 4000) {
            int x = (int) ((interactionAttributes.getEntity().getPosition().getX() / distanceToBase) * 5000);
            int y = (int) ((interactionAttributes.getEntity().getPosition().getY() / distanceToBase) * 5000);
            return HeroCommands.move(new Coordinate(x, y), "");
        } else {
            int x = (int) ((interactionAttributes.getEntity().getPosition().getX() / distanceToBase) * (distanceToBase + 1000));
            int y = (int) ((interactionAttributes.getEntity().getPosition().getY() / distanceToBase) * (distanceToBase + 1000));
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
