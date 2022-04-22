public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double distanceTo(Coordinate other) {
        return Helpers.getDistance(x, y, other.x, other.y);
    }

    public Coordinate getVector(Coordinate other) {
        return new Coordinate(other.x - x, other.y - y);
    }

    public double getHypotenuse() {
        return Math.hypot(x, y);
    }
}
