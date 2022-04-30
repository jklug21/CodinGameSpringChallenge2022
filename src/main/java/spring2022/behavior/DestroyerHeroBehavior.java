package spring2022.behavior;

import spring2022.GameParameters;
import spring2022.GameState;
import spring2022.commands.HeroCommand;
import spring2022.commands.HeroCommands;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;
import spring2022.util.Constants;
import spring2022.util.Coordinate;
import spring2022.util.Decision;
import spring2022.util.DecisionChain;
import spring2022.util.Log;

public class DestroyerHeroBehavior implements HeroBehavior {
    private int controlledLastRound = -1;

    @Override
    public int sortEnemies(InteractionAttributes m1, InteractionAttributes m2) {
        return DecisionChain.of(
                Decision.inAttackShieldingRange(m1, m2),
                Decision.closerToBase(m1, m2)
        );
    }

    @Override
    public boolean considerEnemy(InteractionAttributes m) {
        if (m.getEntity().getFaction() != Faction.MONSTER) {
            return false;
        }
        if (m.getEntity().getId() == controlledLastRound) {
            return false;
        }
        Hero hero = m.getHero();
        if (hero.getId() == 1 && m.getEntity().getId() == 43) {
            Log.log(this, m.debug(hero));
        }
        GameState state = GameState.get();
        double heroFromEnemyBase = hero.distanceTo(GameState.get().getEnemyBase());
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
    public Coordinate getIdleCoordinate(int i) {
        Coordinate enemyBase = GameState.get().getEnemyBase();
        return new Coordinate(Math.abs(enemyBase.getX() - 4000), Math.abs(enemyBase.getY() - 2600));
    }

    @Override
    public HeroCommand getNextAction(InteractionAttributes m) {
        GameState state = GameState.get();
        Coordinate enemyBase = state.getEnemyBase();
        Coordinate heroPos = m.getHero().getPosition();
        Entity entity = m.getEntity();
        int targetId = entity.getId();
        if (entity.getThreatFor() != Faction.ENEMY && m.getHero().distanceTo(enemyBase) > 7000) {
            controlledLastRound = targetId;
            return HeroCommands.monsterAttack(targetId, "Chaaarge!");
        } else if (entity.getThreatFor() == Faction.ENEMY && m.getDistanceToEnemyBase() <= 12 * 400) {
            return HeroCommands.shield(targetId);
        } else {
            long monstersCloseby = state.getMonsters().values().stream().filter(mo -> mo.distanceTo(heroPos) < Constants.WIND_RANGE).count();
            if (monstersCloseby > 4) {
                return HeroCommands.castWindTowards(enemyBase, "Surprise motherf*");
            }
            if (controlledLastRound != targetId) {
                controlledLastRound = targetId;
                return HeroCommands.monsterAttack(targetId, "Chaaarge!");
            } else {
                return HeroCommands.move(enemyBase, "Attacking");
            }
        }
    }
}
