package spring2022.util;

import spring2022.GameState;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;

public class ConsiderIf {
    public static boolean isMonster(InteractionAttributes entity) {
        return entity.getEntity().getFaction() == Faction.MONSTER;
    }

    public static boolean isEnemy(InteractionAttributes entity) {
        return entity.getEntity().getFaction() == Faction.ENEMY;
    }

    public static boolean targetsEnemyBase(InteractionAttributes entity) {
        return entity.getEntity().getThreatFor() == Faction.ENEMY;
    }

    public static boolean targetsMe(InteractionAttributes entity) {
        return entity.getEntity().getThreatFor() == Faction.OWN;
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

    public static boolean targetedByOtherHero(InteractionAttributes m) {
        GameState state = GameState.get();
        int heroId = m.getHero().getId();
        return state.getMyHeroes().values().stream()
                .filter(h -> h.getId() != heroId)
                .anyMatch(h -> h.getCurrentTarget().getEntity().getId() == m.getEntity().getId());
    }

    public static boolean isCurrentTarget(InteractionAttributes m) {
        return m.getHero().getCurrentTarget().getEntity().getId() == m.getEntity().getId();
    }
}
