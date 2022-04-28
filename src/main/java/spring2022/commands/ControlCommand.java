package spring2022.commands;

import spring2022.util.Coordinate;

public class ControlCommand extends StationaryCommand {
    private final Integer targetId;
    private final Coordinate target;
    private final String message;

    public ControlCommand(Integer targetId, Coordinate target, String message) {
        this.targetId = targetId;
        this.target = target;
        this.message = message;
    }

    @Override
    public Runnable getExecution() {
        return () -> System.out.printf("SPELL CONTROL %d %d %d %s%n", targetId, target.getX(), target.getY(), message);
    }
}
