package spring2022.strategy;

import java.util.ArrayList;
import java.util.List;
import spring2022.behavior.HeroBehaviorContainer;
import spring2022.behavior.HeroClass;

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
        if (Flags.getInstance().istFlagRaised(Flags.BASE_UNDER_ATTACK)) {
            heroBehaviors.get(2).setTempClass(HeroClass.INTERCEPTOR);
            heroBehaviors.get(2).setEndCondition(() -> !Flags.getInstance().istFlagRaised(Flags.BASE_UNDER_ATTACK));
        }
    }
}
