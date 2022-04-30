package spring2022.util;

import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;

public class ConsiderIf {
    public static boolean isMonster(Entity entity) {
        return entity.getFaction() == Faction.MONSTER;
    }

    public static boolean isEnemy(Entity entity) {
        return entity.getFaction() == Faction.ENEMY;
    }

    public static boolean targetsEnemyBase(Entity entity) {
        return entity.getThreatFor() == Faction.ENEMY;
    }

    public static boolean targetsMe(Entity entity) {
        return entity.getThreatFor() == Faction.OWN;
    }

    public static boolean isMonster(InteractionAttributes entity) {
        return entity.getEntity().getFaction() == Faction.MONSTER;
    }

    public static boolean isEnemy(InteractionAttributes entity) {
        return entity.getEntity().getFaction() == Faction.ENEMY;
    }

    public static boolean targetsEnemyBase(InteractionAttributes entity) {
        return entity.getEntity().getFaction() == Faction.ENEMY;
    }

    public static boolean targetsMe(InteractionAttributes entity) {
        return entity.getEntity().getFaction() == Faction.OWN;
    }

    public static boolean heroCanReachBeforeExitingMap(InteractionAttributes m, int range) {
        Hero hero = m.getHero();
        Entity entity = m.getEntity();
        int timeToCollision = Helpers.timeToCollision(hero, entity, range);
        return Helpers.insideMapArea(entity.predictPosition(timeToCollision));
    }

    public static boolean heroCanControlOrShield(InteractionAttributes m) {
        return m.getDistanceToHero() <= Constants.CONTROL_RANGE;
    }
}
