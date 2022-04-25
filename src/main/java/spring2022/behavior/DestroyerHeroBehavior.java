package spring2022.behavior;

import spring2022.GameParameters;
import spring2022.GameState;
import spring2022.domain.Faction;
import spring2022.domain.InteractionAttributes;
import spring2022.util.Constants;
import spring2022.util.Coordinate;
import spring2022.util.HeroCommands;

public class DestroyerHeroBehavior implements HeroBehavior {
    @Override
    public int sortEnemies(InteractionAttributes m1, InteractionAttributes m2) {
        boolean shieldingRange1 = m1.getDistanceToEnemyBase() <= 12 * 400;
        boolean shieldingRange2 = m2.getDistanceToEnemyBase() <= 12 * 400;
        if (shieldingRange1 && !shieldingRange2) {
            return -1;
        }
        if (shieldingRange2 && !shieldingRange1) {
            return 1;
        }
        return (int) (m2.getDistanceToBase() - m1.getDistanceToBase());
    }

    @Override
    public boolean considerEnemy(InteractionAttributes m) {
        if (m.getEntity().getFaction() != Faction.MONSTER) {
            return false;
        }
        GameState state = GameState.get();
        double heroFromEnemyBase = m.getHero().distanceTo(GameState.get().getEnemyBase());
        if (state.getRound() < 80) {
            int remainingRounds = 80 - state.getRound();
            if ((heroFromEnemyBase - 7000) / remainingRounds > GameParameters.HERO_SPEED) {
                return false;
            } else {
                return m.getDistanceToHero() < Constants.CONTROL_RANGE && !m.getEntity().isShielded();
            }
        }
        if (heroFromEnemyBase > 7000) {
            return m.getEntity().getThreatFor() != Faction.ENEMY && m.getDistanceToHero() < Constants.CONTROL_RANGE && !m.getEntity().isShielded();
        } else {
            boolean inShieldingRange = m.getDistanceToEnemyBase() <= 12 * 400;
            if (m.getEntity().getThreatFor() == Faction.ENEMY && inShieldingRange) {
                return m.getDistanceToHero() < Constants.CONTROL_RANGE && !m.getEntity().isShielded();
            } else {
                return m.getDistanceToHero() < Constants.CONTROL_RANGE && !m.getEntity().isShielded() && m.getEntity().getThreatFor() != Faction.ENEMY;
            }
        }
    }

    @Override
    public Coordinate getIdleCoordinate(Coordinate ownBase, Coordinate enemyBase, int i) {
        return new Coordinate(Math.abs(enemyBase.getX() - 4000), Math.abs(enemyBase.getY() - 2600));
    }

    @Override
    public Runnable getNextAction(InteractionAttributes m) {
        GameState state = GameState.get();
        Coordinate enemyBase = state.getEnemyBase();
        Coordinate heroPos = m.getHero().getPosition();
        int dx = (int) (Math.random() * (heroPos.getX() - enemyBase.getX()));
        int dy = (int) (Math.random() * (heroPos.getY() - enemyBase.getY()));
        Coordinate target = new Coordinate(enemyBase.getX() + dx, enemyBase.getY() + dy);
        if (m.getEntity().getThreatFor() != Faction.ENEMY && m.getHero().distanceTo(enemyBase) > 7000) {
            return HeroCommands.control(m.getEntity().getId(), target, "Chaaarge!");
        } else if (m.getEntity().getThreatFor() == Faction.ENEMY && m.getDistanceToEnemyBase() <= 12 * 400) {
            return HeroCommands.shield(m.getEntity().getId());
        } else {
            long monstersCloseby = state.getMonsters().stream().filter(mo -> mo.distanceTo(heroPos) < Constants.WIND_RANGE).count();
            if (monstersCloseby > 4) {
                return HeroCommands.castWindTowards(enemyBase, "Surprise motherf*");
            }
            return HeroCommands.control(m.getEntity().getId(), target, "Chaaarge!");
        }
    }
}
