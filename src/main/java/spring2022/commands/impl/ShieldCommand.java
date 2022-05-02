package spring2022.commands.impl;

import spring2022.commands.StationaryCommand;

public class ShieldCommand extends StationaryCommand {
    private final Integer targetId;
    private final String message;

    public ShieldCommand(Integer targetId, String message) {
        this.targetId = targetId;
        this.message = message;
    }

    @Override
    public Runnable getExecution() {
        return () -> System.out.printf("SPELL SHIELD %d %s%n", targetId, message);
    }
}
