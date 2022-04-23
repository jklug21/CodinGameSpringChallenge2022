package spring2022.behavior;

import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.InteractionAttributes;
import spring2022.util.Coordinate;

public class SaboteurHeroBehavior implements HeroBehavior {
    @Override
    public int sortEnemies(InteractionAttributes m1, InteractionAttributes m2) {
        Entity entity1 = m1.getEntity();
        Entity entity2 = m2.getEntity();

        if (entity1.getFaction() == Faction.ENEMY && entity2.getFaction() == Faction.ENEMY) {
            if (entity1.getShieldLife() > 0 && entity2.getShieldLife() > 0) {
                return 0;
            } else {
                return entity1.getShieldLife() - entity2.getShieldLife();
            }
        } else if (entity1.getFaction() == Faction.ENEMY && entity1.getShieldLife() == 0) {
            return -1;
        } else if (entity2.getFaction() == Faction.ENEMY && entity2.getShieldLife() == 0) {
            return 1;
        } else if (entity1.getThreatFor() == Faction.ENEMY && entity2.getThreatFor() == Faction.ENEMY) {
            return (int) (m2.getDistanceToHero() - m1.getDistanceToHero());
        } else if (entity1.getThreatFor() == Faction.ENEMY && entity2.getThreatFor() != Faction.ENEMY) {
            return -1;
        } else if (entity1.getThreatFor() != Faction.ENEMY && entity2.getThreatFor() == Faction.ENEMY) {
            return 1;
        } else {
            return (int) (m2.getDistanceToHero() - m1.getDistanceToHero());
        }
    }

    @Override
    public boolean considerEnemy(InteractionAttributes m) {
        return true;
    }

    @Override
    public Coordinate getIdleCoordinate(Coordinate ownBase, Coordinate enemyBase, int i) {
        return null;
    }

    @Override
    public Runnable getNextAction(InteractionAttributes interactionAttributes) {
        return null;
    }
}
