package spring2022.behavior;

public class HeroBehaviorFactory {
    public static HeroBehavior get(HeroClass heroClass) {
        switch (heroClass) {
            case DEFENDER:
                return new DefensiveHeroBehavior();
            case MANA_HUNTER:
                return new ManaHunterHeroBehavior();
            case DESTROYER:
                return new DestroyerHeroBehavior();
            case SABOTEUR:
                return new SaboteurHeroBehavior();
            default:
                throw new IllegalArgumentException();
        }
    }
}
