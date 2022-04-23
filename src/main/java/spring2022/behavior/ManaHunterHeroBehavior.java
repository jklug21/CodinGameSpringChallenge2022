package spring2022.behavior;

import spring2022.GameParameters;
import spring2022.GameState;
import spring2022.domain.Faction;
import spring2022.domain.InteractionAttributes;
import spring2022.util.Constants;
import spring2022.util.Coordinate;
import spring2022.util.HeroCommands;
import spring2022.util.Log;

public class ManaHunterHeroBehavior implements HeroBehavior {
    public static final int[] POS_PRESET = {7000, 3000};

    @Override
    public int sortEnemies(InteractionAttributes m1, InteractionAttributes m2) {
        InteractionAttributes currentTarget = m1.getHero().getCurrentTarget();
        int currentTargetId = -1;
        if (currentTarget != null) {
            currentTargetId = currentTarget.getEntity().getId();
        }
        double baseDist1 = m1.getDistanceToBase();
        double baseDist2 = m2.getDistanceToBase();

        // stay on target
        if (m1.getEntity().getId() == currentTargetId) {
            return -1;
        }
        if (m2.getEntity().getId() == currentTargetId) {
            return 1;
        }

        // scale buddy count to distance from hero linearly
        double buddyScale1 = (m1.getBuddyCount() + 1) * Constants.MELEE_RANGE_SQ / m1.getDistanceToHero() * baseDist1;
        double buddyScale2 = (m2.getBuddyCount() + 1) * Constants.MELEE_RANGE_SQ / m2.getDistanceToHero() * baseDist2;
        return (buddyScale2 - buddyScale1) > 0 ? 1 : -1;
    }

    @Override
    public boolean considerEnemy(InteractionAttributes m) {
        return m.getEntity().getFaction() == Faction.MONSTER;
    }

    @Override
    public Coordinate getIdleCoordinate(Coordinate ownBase, Coordinate enemyBase, int i) {
        int x = Math.abs(ownBase.getX() - POS_PRESET[i % 2]);
        int y = Math.abs(ownBase.getY() - POS_PRESET[1 - i % 2]);
        return new Coordinate(x, y);
    }

    @Override
    public Runnable getNextAction(InteractionAttributes interactionAttributes) {
        if (interactionAttributes.getDistanceToBase() < GameParameters.CRITICAL_AREA) {
            int heroId = interactionAttributes.getHero().getId();
            Log.log(this, heroId + ": Switching to " + HeroClass.DEFENDER);
            GameState state = interactionAttributes.getState();
            HeroBehaviorContainer behavior = state.getHeroBehaviors().get(heroId % 3);
            behavior.setTempClass(HeroClass.DEFENDER);
            behavior.setEndCondition(() -> state.getMonsters().stream()
                    .noneMatch(m -> m.getId() == interactionAttributes.getEntity().getId()));
        }
        return HeroCommands.move(interactionAttributes.getRendezvous(), interactionAttributes.getEntity().getId());
    }
}
