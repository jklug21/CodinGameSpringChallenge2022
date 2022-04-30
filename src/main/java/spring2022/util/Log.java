package spring2022.util;

import spring2022.GameParameters;

public class Log {
    public static void log(Object logger, String message) {
        log(logger.getClass().getSimpleName(), message);
    }

    public static void log(String logger, String message) {
        if (!GameParameters.CAPTURE_INPUTS) {
            System.err.printf("%s: %s%n", logger, message);
        }
    }

    public static void captureInput(int input) {
        if (GameParameters.CAPTURE_INPUTS) {
            System.err.println(input);
        }
    }
}
