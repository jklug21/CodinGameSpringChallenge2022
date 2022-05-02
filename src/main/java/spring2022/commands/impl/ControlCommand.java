package spring2022.commands.impl;

import spring2022.commands.StationaryCommand;
import spring2022.domain.Entity;
import spring2022.util.Constants;
import spring2022.util.Coordinate;

public class ControlCommand extends StationaryCommand {
    private final Entity entity;
    private final Coordinate target;
    private final String message;

    public ControlCommand(Entity entity, Coordinate target, String message) {
        this.entity = entity;
        this.target = target;
        this.message = message;
    }

    @Override
    public Runnable getExecution() {
        return () -> {
            Coordinate monsterVelocity = entity.getPosition()
                    .getVector(target)
                    .scaleVectorTo(Constants.MONSTER_SPEED);
            entity.setVelocity(monsterVelocity);
            System.out.printf("SPELL CONTROL %d %d %d %s%n", entity.getId(), target.getX(), target.getY(), message);
        };
    }
}
