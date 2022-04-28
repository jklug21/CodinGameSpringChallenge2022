package spring2022.domain;

import lombok.Getter;
import lombok.Setter;
import spring2022.commands.HeroCommand;
import spring2022.io.EntityData;
import spring2022.util.Coordinate;

@Getter
@Setter
public class Hero extends Entity {
    public static Hero NVL = new Hero(Entity.NVL);
    private InteractionAttributes currentTarget;
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
                entity.getNearBase(),
                entity.getThreatFor());
    }

    public void setCurrentTarget(InteractionAttributes target) {
        this.currentTarget = target;
    }

    public void clearTarget() {
        setCurrentTarget(InteractionAttributes.NVL);
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
}
