package spring2022.strategy;

import java.util.HashMap;
import java.util.Map;
import spring2022.util.Log;

public class Flags {
    private static final Map<Integer, String> names;
    private static final Flags FLAGS = new Flags();
    public static final int BASE_UNDER_ATTACK = 1;
    public static final int WIND_STRIKE_POSSIBLE = 2;
    public static final int SECOND_PHASE = 4;
    public static final int HARD_DEFENSE_NEEDED = 8;
    public static final int MONSTER_CLOSE_TO_BASE = 16;

    public int flags = 0;

    static {
        names = new HashMap<>();
        names.put(1, "Base under attack");
        names.put(2, "Wind strike possible");
        names.put(4, "Second phase");
        names.put(8, "Hard defense needed");
        names.put(16, "Monster approaching base");
    }

    public static Flags getInstance() {
        return FLAGS;
    }

    public void raiseFlag(int flag) {
        if (!isFlagRaised(flag)) {
            Log.log(this, "flag raised " + names.get(flag));
            flags |= flag;
        }
    }

    public void lowerFlag(int flag) {
        if (isFlagRaised(flag)) {
            Log.log(this, "flag lowered " + names.get(flag));
            flags &= ~flag;
        }
    }

    public boolean isFlagRaised(int flag) {
        return (flags & flag) == flag;
    }
}
