package spring2022.util;

public class HeroCommands {
    public static Runnable move(Coordinate target, int id) {
        return move(target, String.valueOf(id));
    }

    public static Runnable move(Coordinate target, String message) {
        return () -> System.out.printf("MOVE %d %d %s%n", target.getX(), target.getY(), message);
    }

    public static Runnable castWindAwayFrom(Coordinate source, Coordinate target) {
        return () -> System.out.printf("SPELL WIND %d %d%n", target.getX() - (source.getX() - target.getX()), target.getY() - (source.getY() - target.getY()));
    }

    public static Runnable castWindTowards(Coordinate target, String message) {
        return () -> System.out.printf("SPELL WIND %d %d %s%n", target.getX(), target.getY(), message);
    }

    public static Runnable shield(int targetId) {
        return () -> System.out.printf("SPELL SHIELD %d%n", targetId);
    }

    public static Runnable control(int targetId, Coordinate target, String message) {
        return () -> System.out.printf("SPELL CONTROL %d %d %d %s%n", targetId, target.getX(), target.getY(), message);
    }
}
