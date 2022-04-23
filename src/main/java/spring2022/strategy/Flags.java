package spring2022.strategy;

import spring2022.util.Log;

public class Flags {
    private static final Flags FLAGS = new Flags();
    public static final int BASE_UNDER_ATTACK = 1;
    public static final int SECOND_FLAG = 2;
    public static final int THIRD_FLAG = 4;

    public int flags = 0;

    public static Flags getInstance() {
        return FLAGS;
    }

    public void raiseFlag(int flag) {
        Log.log(this, "flag raised " + flag);
        flags |= flag;
    }

    public void lowerFlag(int flag) {
        flags &= ~flag;
    }

    public boolean istFlagRaised(int flag) {
        return (flags & flag) > 0;
    }
}
