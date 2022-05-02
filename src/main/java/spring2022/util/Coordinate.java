package spring2022.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Coordinate {
    private final int x;
    private final int y;

    public double distanceTo(Coordinate other) {
        return Helpers.getDistance(x, y, other.x, other.y);
    }

    public double getHypotenuse() {
        return Math.hypot(x, y);
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Coordinate getVector(Coordinate target) {
        return new Coordinate(target.getX() - x, target.getY() - y);
    }

    public Coordinate scaleVectorTo(int length) {
        double hypotenuse = getHypotenuse();
        return new Coordinate((int) (((double) x / hypotenuse) * (double) length), (int) (((double) y / hypotenuse) * (double) length));
    }
}
