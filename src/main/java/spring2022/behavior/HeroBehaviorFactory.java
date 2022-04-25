package spring2022.behavior;

public class HeroBehaviorFactory {
    public static HeroBehavior get(HeroClass heroClass) {
        switch (heroClass) {
            case HARD_DEFENDER:
                return new HardDefensiveHeroBehavior();
            case DEFENDER:
                return new DefensiveHeroBehavior();
            case MANA_HUNTER:
                return new ManaHunterHeroBehavior();
            case DESTROYER:
                return new DestroyerHeroBehavior();
            case INTERCEPTOR:
                return new InterceptorHeroBehavior();
            default:
                throw new IllegalArgumentException();
        }
    }
}
