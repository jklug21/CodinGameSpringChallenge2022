package spring2022.behavior;

import java.util.function.Supplier;
import lombok.Setter;
import spring2022.util.Log;

@Setter
public class HeroBehaviorContainer {
    private final HeroBehavior behavior;
    private final int id;
    private HeroBehavior tempBehavior;
    private Supplier<Boolean> endCondition;
    private HeroClass tempClass;


    public HeroBehaviorContainer(HeroClass mainClass, int id) {
        behavior = HeroBehaviorFactory.get(mainClass);
        this.id = id;
        tempBehavior = null;
        endCondition = null;
    }

    public HeroBehavior getBehavior() {
        if (tempBehavior != null) {
            if (!endCondition.get()) {
                return tempBehavior;
            } else {
                Log.log(this, id + ": Condition reached. Switching back to " + behavior.getClass().getSimpleName());
                tempBehavior = null;
                endCondition = null;
            }
        }
        return behavior;
    }

    public void setTempClass(HeroClass heroClass) {
        if (heroClass != tempClass) {
            tempClass = heroClass;
            Log.log(this, id + ": Switching to " + heroClass);
            tempBehavior = HeroBehaviorFactory.get(heroClass);
        }
    }
}
