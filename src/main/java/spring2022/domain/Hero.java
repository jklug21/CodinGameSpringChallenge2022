package spring2022.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import spring2022.GameState;
import spring2022.behavior.HeroBehavior;
import spring2022.behavior.HeroBehaviorContainer;
import spring2022.commands.HeroCommand;
import spring2022.io.EntityData;
import spring2022.util.Constants;
import spring2022.util.Coordinate;
import spring2022.util.Helpers;

@Getter
@Setter
public class Hero extends Entity {
    public static Hero NVL = new Hero(Entity.NVL);
    private InteractionAttributes currentTarget = InteractionAttributes.NVL;
    private HeroCommand nextAction;
    private Coordinate targetCoordinate;

    public Hero(EntityData entityData) {
        super(entityData);
    }

    public Hero(Entity entity) {
        super(entity.getId(),
                entity.getFaction(),
                entity.getPosition().getX(),
                entity.getPosition().getY(),
                entity.getShieldLife(),
                entity.getIsControlled(),
                entity.getHealth(),
                entity.getVelocity().getX(),
                entity.getVelocity().getY(),
                entity.getNearBase());
    }

    public void setCurrentTarget(InteractionAttributes target) {
        if (target == null) {
            target = InteractionAttributes.NVL;
        }
        this.currentTarget = target;
    }

    public void clearCurrentTarget() {
        currentTarget = InteractionAttributes.NVL;
    }

    public void clearTargetIfGone() {
        GameState state = GameState.get();
        if (state.getMonsters().keySet().stream()
                .noneMatch(monsterId -> monsterId == currentTarget.getEntity().getId())) {
            setCurrentTarget(InteractionAttributes.NVL);
        }
    }

    public void setNextAction(HeroCommand action) {
        this.nextAction = action;
    }

    public boolean hasNextAction() {
        return nextAction != null;
    }

    public void executeAction() {
        targetCoordinate = nextAction.expectedPositionAfterExecution(this);
        nextAction.execute();
    }

    public boolean canSee(Coordinate position) {
        return this.getPosition().distanceTo(position) <= Constants.VIEWING_DISTANCE;
    }

    public Coordinate calculateRendezvous(Coordinate target, Coordinate velocity, int distance) {
        int reachedInRounds = Helpers.timeToCollision(this, target, velocity, distance);
        return Helpers.predictPosition(target, velocity, reachedInRounds);
    }

    public HeroBehavior getBehavior() {
        return getBehaviorContainer().getBehavior();
    }

    public HeroBehaviorContainer getBehaviorContainer() {
        List<HeroBehaviorContainer> heroBehaviors = GameState.get().getHeroBehaviors();
        return heroBehaviors.get(getId() % 3);
    }
}
