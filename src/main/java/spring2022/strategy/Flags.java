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
    public static final int ATTACK_OPPORTUNITY = 32;
    public static final int DEFENDER_INFLUENCED = 64;

    public int flags = 0;

    static {
        names = new HashMap<>();
        names.put(BASE_UNDER_ATTACK, "Base under attack");
        names.put(WIND_STRIKE_POSSIBLE, "Wind strike possible");
        names.put(SECOND_PHASE, "Second phase");
        names.put(HARD_DEFENSE_NEEDED, "Hard defense needed");
        names.put(MONSTER_CLOSE_TO_BASE, "Monster approaching base");
        names.put(ATTACK_OPPORTUNITY, "Attack opportunity");
        names.put(DEFENDER_INFLUENCED, "Defender influenced");
    }

    public static Flags getInstance() {
        return FLAGS;
    }

    public static void print() {
        System.err.print("[");
        if (getInstance().isFlagRaised(BASE_UNDER_ATTACK)) {
            System.err.print("A");
        } else {
            System.err.print(" ");
        }
        if (getInstance().isFlagRaised(WIND_STRIKE_POSSIBLE)) {
            System.err.print("W");
        } else {
            System.err.print(" ");
        }
        if (getInstance().isFlagRaised(SECOND_PHASE)) {
            System.err.print("2");
        } else {
            System.err.print(" ");
        }
        if (getInstance().isFlagRaised(HARD_DEFENSE_NEEDED)) {
            System.err.print("H");
        } else {
            System.err.print(" ");
        }
        if (getInstance().isFlagRaised(MONSTER_CLOSE_TO_BASE)) {
            System.err.print("M");
        } else {
            System.err.print(" ");
        }
        if (getInstance().isFlagRaised(ATTACK_OPPORTUNITY)) {
            System.err.print("O");
        } else {
            System.err.print(" ");
        }
        if (getInstance().isFlagRaised(DEFENDER_INFLUENCED)) {
            System.err.print("I");
        } else {
            System.err.print(" ");
        }
        System.err.println("]");
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
