package spring2022.strategy;

import spring2022.util.Log;

public class Flags {
    private static final Flags FLAGS = new Flags();
    public static final int BASE_UNDER_ATTACK = 1;
    public static final int WIND_STRIKE_POSSIBLE = 2;
    public static final int SECOND_PHASE = 4;
    public static final int HARD_DEFENSE_NEEDED = 8;

    public int flags = 0;

    public static Flags getInstance() {
        return FLAGS;
    }

    public void raiseFlag(int flag) {
        if (!isFlagRaised(flag)) {
            Log.log(this, "flag raised " + flag);
            flags |= flag;
        }
    }

    public void lowerFlag(int flag) {
        if (isFlagRaised(flag)) {
            Log.log(this, "flag lowered " + flag);
            flags &= ~flag;
        }
    }

    public boolean isFlagRaised(int flag) {
        return (flags & flag) == flag;
    }
}
