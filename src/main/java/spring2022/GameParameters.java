package spring2022;

public class GameParameters {
    public static final int HERO_SPEED = 800;
    public static final int HUNTING_AREA = 11000;
    public static final int FIGHTING_AREA = 7000;
    public static final int SAFETY_AREA = 5000;
    public static final int CRITICAL_AREA = 3000;
    private static boolean reRunInputs;
    private static boolean captureInputs;

    public static void setReRunInputs(boolean reRunInputs) {
        GameParameters.reRunInputs = reRunInputs;
    }

    public static boolean isReRunInputs() {
        return reRunInputs;
    }

    public static void setCaptureInputs(boolean captureInputs) {
        GameParameters.captureInputs = captureInputs;
    }

    public static boolean isCaptureInputs() {
        return captureInputs;
    }
}
