package spring2022.commands;

import spring2022.domain.Hero;
import spring2022.util.Coordinate;

public abstract class StationaryCommand implements HeroCommand {
    @Override
    public Coordinate expectedPositionAfterExecution(Hero hero) {
        return hero.getPosition();
    }

    public abstract Runnable getExecution();
}
