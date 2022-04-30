package spring2022.strategy;

import java.util.ArrayList;
import java.util.List;
import spring2022.GameState;
import spring2022.behavior.HeroBehaviorContainer;
import spring2022.behavior.HeroClass;
import spring2022.util.EndCondition;
import spring2022.util.Helpers;

public class DefensiveStrategy implements GameStrategy {
    @Override
    public List<HeroBehaviorContainer> getInitialBehavior() {
        List<HeroBehaviorContainer> heroBehaviors = new ArrayList<>();
        heroBehaviors.add(new HeroBehaviorContainer(HeroClass.DEFENDER, 0));
        heroBehaviors.add(new HeroBehaviorContainer(HeroClass.DEFENDER, 1));
        heroBehaviors.add(new HeroBehaviorContainer(HeroClass.MANA_HUNTER, 2));
        return heroBehaviors;
    }

    @Override
    public void adaptBehavior() {
        List<HeroBehaviorContainer> heroBehaviors = GameState.get().getHeroBehaviors();
        if (Flags.getInstance().isFlagRaised(Flags.BASE_UNDER_ATTACK)) {
            Helpers.changeClass(2, HeroClass.INTERCEPTOR, EndCondition.notFlag(Flags.BASE_UNDER_ATTACK));
        }
    }
}
