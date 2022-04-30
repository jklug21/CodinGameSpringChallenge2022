package spring2022.strategy;

import java.util.ArrayList;
import java.util.List;
import spring2022.GameState;
import spring2022.behavior.HeroBehaviorContainer;
import spring2022.behavior.HeroClass;
import spring2022.util.EndCondition;
import spring2022.util.Helpers;

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
        Flags flags = Flags.getInstance();
        GameState state = GameState.get();
        if (flags.isFlagRaised(Flags.BASE_UNDER_ATTACK)) {
            Helpers.changeClass(2, HeroClass.INTERCEPTOR, EndCondition.notFlag(Flags.BASE_UNDER_ATTACK));
        }
        if (flags.isFlagRaised(Flags.HARD_DEFENSE_NEEDED)) {
            Helpers.changeClass(0, HeroClass.HARD_DEFENDER, EndCondition.notFlag(Flags.HARD_DEFENSE_NEEDED));
            Helpers.changeClass(1, HeroClass.DEFENDER, EndCondition.notFlag(Flags.HARD_DEFENSE_NEEDED));
        } else if (!flags.isFlagRaised(Flags.MONSTER_CLOSE_TO_BASE)) {
            Helpers.changeClass(0, HeroClass.MANA_HUNTER, EndCondition.flag(Flags.MONSTER_CLOSE_TO_BASE));
        }
        if (flags.isFlagRaised(Flags.SECOND_PHASE) && state.getRoundState().getMyMana() >= 100) {
            Helpers.changeClass(1, HeroClass.DESTROYER, EndCondition.mana(50));
        }
    }
}
