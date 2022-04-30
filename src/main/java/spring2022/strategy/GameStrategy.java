package spring2022.strategy;

import java.util.List;
import spring2022.behavior.HeroBehaviorContainer;

public interface GameStrategy {
    List<HeroBehaviorContainer> getInitialBehavior();

    void adaptBehavior();
}
