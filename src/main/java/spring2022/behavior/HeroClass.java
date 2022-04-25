package spring2022.behavior;

import lombok.Getter;

@Getter
public enum HeroClass {
    HARD_DEFENDER(HardDefensiveHeroBehavior.class),
    DEFENDER(DefensiveHeroBehavior.class),
    MANA_HUNTER(ManaHunterHeroBehavior.class),
    DESTROYER(DefensiveHeroBehavior.class),
    INTERCEPTOR(InterceptorHeroBehavior.class);

    private final Class<? extends HeroBehavior> heroBehaviorClass;

    HeroClass(Class<? extends HeroBehavior> heroBehaviorClass) {
        this.heroBehaviorClass = heroBehaviorClass;
    }
}
