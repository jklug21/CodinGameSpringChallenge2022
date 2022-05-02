package spring2022.commands.impl;

import spring2022.commands.StationaryCommand;
import spring2022.util.Coordinate;

public class WindCommand extends StationaryCommand {
    private final Coordinate target;
    private final String message;

    public WindCommand(Coordinate target, String message) {
        this.target = target;
        this.message = message;
    }

    @Override
    public Runnable getExecution() {
        return () -> System.out.printf("SPELL WIND %d %d %s%n", target.getX(), target.getY(), message);
    }
}
