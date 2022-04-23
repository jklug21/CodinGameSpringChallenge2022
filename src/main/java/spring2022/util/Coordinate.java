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
}
