package spring2022.behavior;

import spring2022.domain.InteractionAttributes;
import spring2022.util.Coordinate;

public class DestroyerHeroBehavior implements HeroBehavior {
    @Override
    public int sortEnemies(InteractionAttributes m1, InteractionAttributes m2) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public boolean considerEnemy(InteractionAttributes m) {
        return false;
    }

    @Override
    public Coordinate getIdleCoordinate(Coordinate ownBase, Coordinate enemyBase, int i) {
        return null;
    }

    @Override
    public Runnable getNextAction(InteractionAttributes interactionAttributes) {
        return null;
    }
}
