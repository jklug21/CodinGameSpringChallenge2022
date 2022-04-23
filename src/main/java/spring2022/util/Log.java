package spring2022.util;

public class Log {
    public static void log(Object logger, String message) {
        log(logger.getClass().getSimpleName(), message);
    }

    public static void log(String logger, String message) {
        System.err.printf("%s: %s%n", logger, message);
    }
}
