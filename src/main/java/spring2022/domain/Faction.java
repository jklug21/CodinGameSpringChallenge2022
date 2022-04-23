package spring2022.domain;

public enum Faction {
    MONSTER, OWN, ENEMY, NVL;

    public static Faction parse(int type) {
        switch (type) {
            case 0:
                return MONSTER;
            case 1:
                return OWN;
            case 2:
                return ENEMY;
            default:
                return NVL;
        }
    }
}
