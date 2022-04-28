package spring2022.commands;

import spring2022.domain.Hero;
import spring2022.util.Coordinate;

public interface HeroCommand {
    Coordinate expectedPositionAfterExecution(Hero hero);

    Runnable getExecution();

    default void execute() {
        getExecution().run();
    }
}
