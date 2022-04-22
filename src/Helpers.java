public class Helpers {
    public static int compareAttributes(MonsterAttributes m1, MonsterAttributes m2) {
        if (m1.getMonster().getThreatFor() == 1 && m2.getMonster().getThreatFor() != 1) {
            return -1;
        } else if (m2.getMonster().getThreatFor() == 1 && m1.getMonster().getThreatFor() != 1) {
            return 1;
        }
        // only one close enough
        if (m1.getDistanceToBase() > GameParameters.BASE_FIGHTING_AREA && m2.getDistanceToBase() > GameParameters.BASE_FIGHTING_AREA) {
            // irrelevant
            return 0;
        } else if (m1.getDistanceToBase() < GameParameters.BASE_FIGHTING_AREA && m2.getDistanceToBase() > GameParameters.BASE_FIGHTING_AREA) {
            return -1;
        } else if (m2.getDistanceToBase() < GameParameters.BASE_FIGHTING_AREA && m1.getDistanceToBase() > GameParameters.BASE_FIGHTING_AREA) {
            return 1;
        }

        // only one close enough
        if (m1.getDistanceToBase() > GameParameters.BASE_SAFETY_AREA && m2.getDistanceToBase() > GameParameters.BASE_SAFETY_AREA) {
            // irrelevant
            return 0;
        } else if (m1.getDistanceToBase() < GameParameters.BASE_SAFETY_AREA && m2.getDistanceToBase() > GameParameters.BASE_SAFETY_AREA) {
            return -1;
        } else if (m2.getDistanceToBase() < GameParameters.BASE_SAFETY_AREA && m1.getDistanceToBase() > GameParameters.BASE_SAFETY_AREA) {
            return 1;
        }

        // if only one in critical area, kill the one within the critical area
        if (m1.getDistanceToBase() < GameParameters.BASE_CRITICAL_AREA && m2.getDistanceToBase() > GameParameters.BASE_CRITICAL_AREA) {
            return -1;
        } else if (m2.getDistanceToBase() < GameParameters.BASE_CRITICAL_AREA && m1.getDistanceToBase() > GameParameters.BASE_CRITICAL_AREA) {
            return 1;
        }

        // both outside or inside critical area, kill the one closer to the hero
        return (int) (m2.getDistanceToHero() - m1.getDistanceToHero());
    }

    public static double getDistance(int x1, int y1, int x2, int y2) {
        return Math.hypot(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    public static double predictDistance(int rounds, Entity entity, Coordinate target) {
        return entity.predictPosition(rounds).distanceTo(target);
    }

    public static double predictDistance(int rounds, Entity entity, Entity target) {
        return entity.predictPosition(rounds).distanceTo(target.predictPosition(rounds));
    }

    public static int timeToCollision(Entity hero, Entity monster) {
        for (int i = 0; i < 10; i++) {
            if (predictDistance(i, monster, hero) < GameParameters.HERO_SPEED * i) {
                return i;
            }
        }
        return 10; // bigger values should be irrelevant
    }
}
