package spring2022.strategy;

import spring2022.behavior.HeroBehaviorContainer;
import spring2022.behavior.HeroClass;

import java.util.ArrayList;
import java.util.List;

public class MixedStrategy implements GameStrategy {
    @Override
    public List<HeroBehaviorContainer> getInitialBehavior() {
        List<HeroBehaviorContainer> heroBehaviors = new ArrayList<>();
        heroBehaviors.add(new HeroBehaviorContainer(HeroClass.DEFENDER, 0));
        heroBehaviors.add(new HeroBehaviorContainer(HeroClass.MANA_HUNTER, 1));
        heroBehaviors.add(new HeroBehaviorContainer(HeroClass.MANA_HUNTER, 2));
        return heroBehaviors;
    }

    @Override
    public void adaptBehavior(List<HeroBehaviorContainer> heroBehaviors) {
        if (Flags.getInstance().isFlagRaised(Flags.BASE_UNDER_ATTACK)) {
            heroBehaviors.get(2).setTempClass(HeroClass.INTERCEPTOR);
            heroBehaviors.get(2).setEndCondition(() -> !Flags.getInstance().isFlagRaised(Flags.BASE_UNDER_ATTACK));
        }
        if (Flags.getInstance().isFlagRaised(Flags.HARD_DEFENSE_NEEDED)) {
            heroBehaviors.get(0).setTempClass(HeroClass.HARD_DEFENDER);
            heroBehaviors.get(0).setEndCondition(() -> !Flags.getInstance().isFlagRaised(Flags.HARD_DEFENSE_NEEDED));
            heroBehaviors.get(1).setTempClass(HeroClass.DEFENDER);
            heroBehaviors.get(1).setEndCondition(() -> !Flags.getInstance().isFlagRaised(Flags.HARD_DEFENSE_NEEDED));
        }
        if (Flags.getInstance().isFlagRaised(Flags.SECOND_PHASE)) {
            heroBehaviors.get(1).setTempClass(HeroClass.DESTROYER);
            heroBehaviors.get(1).setEndCondition(() -> false);
        }
    }
}
