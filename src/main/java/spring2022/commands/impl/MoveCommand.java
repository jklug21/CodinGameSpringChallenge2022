package spring2022.commands.impl;

import spring2022.commands.HeroCommand;
import spring2022.domain.Hero;
import spring2022.util.Coordinate;

public class MoveCommand implements HeroCommand {
    private final Coordinate target;
    private final String message;

    public MoveCommand(Coordinate target, String message) {
        this.target = target;
        this.message = message;
    }

    @Override
    public Coordinate expectedPositionAfterExecution(Hero hero) {
        Coordinate pos = hero.getPosition();
        double distance = pos.distanceTo(target);
        if (distance <= 800) {
            return target;
        } else {
            int dx = target.getX() - pos.getX();
            int dy = target.getY() - pos.getY();
            return new Coordinate(pos.getX() + (int) (dx * (800 / distance)), pos.getY() + (int) (dy * (800 / distance)));
        }
    }

    @Override
    public Runnable getExecution() {
        return () -> System.out.printf("MOVE %d %d %s%n", target.getX(), target.getY(), message);
    }
}
