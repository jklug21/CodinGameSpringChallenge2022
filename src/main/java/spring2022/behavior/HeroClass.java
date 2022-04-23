package spring2022.behavior;

import lombok.Getter;

@Getter
public enum HeroClass {
    DEFENDER(DefensiveHeroBehavior.class),
    MANA_HUNTER(ManaHunterHeroBehavior.class),
    DESTROYER(DefensiveHeroBehavior.class),
    SABOTEUR(SaboteurHeroBehavior.class);

    private final Class<? extends HeroBehavior> heroBehaviorClass;

    HeroClass(Class<? extends HeroBehavior> heroBehaviorClass) {
        this.heroBehaviorClass = heroBehaviorClass;
    }
}
