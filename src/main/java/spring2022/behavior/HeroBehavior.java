package spring2022.behavior;

import spring2022.GameState;
import spring2022.commands.HeroCommand;
import spring2022.domain.InteractionAttributes;
import spring2022.util.Coordinate;

public interface HeroBehavior {
    int sortEnemies(InteractionAttributes m1, InteractionAttributes m2);

    boolean considerEnemy(InteractionAttributes interactionAttributes);

    Coordinate getIdleCoordinate(Coordinate ownBase, Coordinate enemyBase, int i);

    HeroCommand getNextAction(InteractionAttributes interactionAttributes);
}
