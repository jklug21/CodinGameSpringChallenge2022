package spring2022.util;

import spring2022.GameParameters;
import spring2022.domain.Entity;

public class Helpers {
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
