package spring2022.commands;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import spring2022.domain.Entity;
import spring2022.domain.Hero;
import spring2022.util.Constants;
import spring2022.util.Coordinate;

public class AttackCommand implements HeroCommand {
    private final Coordinate target;
    private final String message;

    public AttackCommand(Hero hero, Entity target, String message, Collection<Entity> allMonsters) {
        List<Entity> closebyMonsters = allMonsters.stream().filter(e -> target.distanceTo(e.getPosition()) < 1600).collect(Collectors.toList());
        Optional<Coordinate> averageCoordinate = closebyMonsters.stream()
                .map(Entity::getPosition)
                .reduce(this::sumCoordinate)
                .map(c -> new Coordinate(c.getX() / closebyMonsters.size(), c.getY() / closebyMonsters.size()));
        this.target = hero.calculateRendezvous(averageCoordinate.orElse(target.getPosition()), target.getVelocity(), Constants.MELEE_RANGE);
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

    private Coordinate sumCoordinate(Coordinate c1, Coordinate c2) {
        return new Coordinate(c1.getX() + c2.getX(), c1.getY() + c2.getY());
    }
}
