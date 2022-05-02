package spring2022.util;

import java.util.function.Supplier;
import spring2022.GameState;
import spring2022.domain.InteractionAttributes;
import spring2022.strategy.Flags;

public class EndCondition {
    public static Supplier<Boolean> monsterGone(InteractionAttributes interactionAttributes) {
        return () -> GameState.get().getMonsters().values().stream()
                .noneMatch(m -> m.getId() == interactionAttributes.getEntity().getId());
    }

    public static Supplier<Boolean> notFlag(int flag) {
        return () -> !Flags.getInstance().isFlagRaised(flag);
    }

    public static Supplier<Boolean> flag(int flag) {
        return () -> Flags.getInstance().isFlagRaised(flag);
    }

    public static Supplier<Boolean> mana(int mana) {
        return () -> GameState.get().getRoundState().getMyMana() < mana;
    }
}
