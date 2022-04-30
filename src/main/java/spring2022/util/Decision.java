package spring2022.util;

import java.util.function.Supplier;
import spring2022.domain.Entity;
import spring2022.domain.InteractionAttributes;

public class Decision {
    private final Supplier<Integer> decision;

    public Decision(Supplier<Integer> decision) {
        this.decision = decision;
    }

    public Integer takeDecision() {
        return decision.get();
    }


    public static Decision largerTargetGroup(InteractionAttributes ia1, InteractionAttributes ia2) {
        return new Decision(() -> {
            double baseDist1 = ia1.getDistanceToBase();
            double baseDist2 = ia2.getDistanceToBase();
            double buddyScale1 = (ia1.getBuddyCount() + 1) * Constants.MELEE_RANGE_SQ / ia1.getDistanceToHero() * baseDist1;
            double buddyScale2 = (ia2.getBuddyCount() + 1) * Constants.MELEE_RANGE_SQ / ia2.getDistanceToHero() * baseDist2;
            return (buddyScale2 - buddyScale1) > 0 ? 1 : -1;
        });
    }

    public static Decision currentTargetInRange(InteractionAttributes ia1, InteractionAttributes ia2) {
        return new Decision(() -> {
            InteractionAttributes currentTarget = ia1.getHero().getCurrentTarget();
            int currentTargetId = -1;
            if (currentTarget != null) {
                currentTargetId = currentTarget.getEntity().getId();
            }

            Entity m1 = ia1.getEntity();
            Entity m2 = ia2.getEntity();
            if (m1.getId() == currentTargetId && ia1.getDistanceToHero() - ia2.getDistanceToHero() < Constants.MELEE_RANGE) {
                return -1;
            }
            if (m2.getId() == currentTargetId && ia2.getDistanceToHero() - ia1.getDistanceToHero() < Constants.MELEE_RANGE) {
                return 1;
            }
            return 0;
        });
    }

    public static Decision onlyOneInRange(InteractionAttributes ia1, InteractionAttributes ia2, int range) {
        return new Decision(() -> {
            double baseDist1 = ia1.getDistanceToBase();
            double baseDist2 = ia2.getDistanceToBase();

            if (baseDist1 < range && baseDist2 > range) {
                return -1;
            } else if (baseDist2 < range && baseDist1 > range) {
                return 1;
            }
            return 0;
        });
    }

    public static Decision closerToBase(InteractionAttributes m1, InteractionAttributes m2) {
        return new Decision(() -> (int) (m1.getDistanceToBase() - m2.getDistanceToBase()));
    }

    public static Decision inAttackShieldingRange(InteractionAttributes m1, InteractionAttributes m2) {
        return new Decision(() -> {
            boolean shieldingRange1 = m1.getDistanceToEnemyBase() <= 12 * 400;
            boolean shieldingRange2 = m2.getDistanceToEnemyBase() <= 12 * 400;
            if (shieldingRange1 && !shieldingRange2) {
                return -1;
            }
            if (shieldingRange2 && !shieldingRange1) {
                return 1;
            }
            return 0;
        });
    }
}
