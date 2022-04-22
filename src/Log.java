public class Log {
    public static void log(Object logger, String message) {
        log(logger.toString(), message);
    }

    public static void log(String logger, String message) {
        System.err.printf("%s: %s%n", logger.getClass().getSimpleName(), message);
    }
}
