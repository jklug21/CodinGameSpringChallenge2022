package spring2022.behavior;

import spring2022.GameParameters;
import spring2022.GameState;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;
import spring2022.io.RoundState;
import spring2022.strategy.Flags;
import spring2022.util.Constants;
import spring2022.util.Coordinate;
import spring2022.util.Helpers;
import spring2022.util.HeroCommands;
import spring2022.util.Log;

public class DefensiveHeroBehavior implements HeroBehavior {
    public static final int[] POS_PRESET = {5000, 3800, 2000};

    @Override
    public int sortEnemies(InteractionAttributes m1, InteractionAttributes m2) {
        InteractionAttributes currentTarget = m1.getHero().getCurrentTarget();
        int currentTargetId = -1;
        if (currentTarget != null) {
            currentTargetId = currentTarget.getEntity().getId();
        }
        double baseDist1 = m1.getDistanceToBase();
        double baseDist2 = m2.getDistanceToBase();

        // only one close enough
        if (baseDist1 > GameParameters.FIGHTING_AREA && baseDist2 > GameParameters.FIGHTING_AREA) {
            // irrelevant
            return 0;
        } else if (baseDist1 < GameParameters.FIGHTING_AREA && baseDist2 > GameParameters.FIGHTING_AREA) {
            return -1;
        } else if (baseDist2 < GameParameters.FIGHTING_AREA && baseDist1 > GameParameters.FIGHTING_AREA) {
            return 1;
        }

        // only one close enough
        if (baseDist1 < GameParameters.SAFETY_AREA && baseDist2 > GameParameters.SAFETY_AREA) {
            return -1;
        } else if (baseDist2 < GameParameters.SAFETY_AREA && baseDist1 > GameParameters.SAFETY_AREA) {
            return 1;
        }

        // if only one in critical area, kill the one within the critical area
        if (baseDist1 < GameParameters.CRITICAL_AREA && baseDist2 > GameParameters.CRITICAL_AREA) {
            return -1;
        } else if (baseDist2 < GameParameters.CRITICAL_AREA && baseDist1 > GameParameters.CRITICAL_AREA) {
            return 1;
        }  // else kill the current target
        else if (currentTargetId == m1.getEntity().getId()) {
            return -1;
        } else if (currentTargetId == m2.getEntity().getId()) {
            return 1;
        }

        // both outside or inside critical area, kill the one closer to the hero
        return (int) (m2.getDistanceToHero() - m1.getDistanceToHero());
    }

    @Override
    public boolean considerEnemy(InteractionAttributes m) {
        Entity entity = m.getEntity();
        Faction faction = entity.getFaction();
        if (faction == Faction.ENEMY) {
            if (m.getDistanceToBase() < GameParameters.HUNTING_AREA) {
                Flags.getInstance().raiseFlag(Flags.BASE_UNDER_ATTACK);
            }
        }
        return (faction == Faction.MONSTER && entity.getThreatFor() == Faction.OWN && m.getDistanceToBase() < GameParameters.FIGHTING_AREA);
    }

    @Override
    public Coordinate getIdleCoordinate(Coordinate ownBase, Coordinate enemyBase, int i) {
        int x = Math.abs(ownBase.getX() - POS_PRESET[i]);
        int y = Math.abs(ownBase.getY() - POS_PRESET[2 - i]);
        return new Coordinate(x, y);
    }

    @Override
    public Runnable getNextAction(InteractionAttributes interaction) {
        if (interaction == null) {
            return null;
        }
        GameState state = interaction.getState();
        RoundState round = state.getRoundState();
        int mana = round.getMyMana();
        Coordinate enemyBase = state.getEnemyBase();
        Hero hero = interaction.getHero();
        Faction faction = interaction.getEntity().getFaction();

        //Log.log(this, String.format("%s:%s:%s", (interaction.getDistanceToBase() - Constants.ENEMY_RANGE), interaction.getDistanceToBase(), interaction.getSpeed() * 2));
        if (faction == Faction.MONSTER &&
                interaction.getDistanceToHero() < Constants.WIND_RANGE &&
                (interaction.getDistanceToBase() - Constants.ENEMY_RANGE) < interaction.getSpeed() * 2 &&
                mana >= Constants.SPELL_COST) {
            round.reduceMana(Constants.SPELL_COST);
            return HeroCommands.castWindTowards(enemyBase, "Woosh");
        } else {
            int reachedInRounds = Helpers.timeToCollision(hero, interaction.getEntity());
            Coordinate rendezvous = interaction.getEntity().predictPosition(reachedInRounds + 1);
            return HeroCommands.move(rendezvous, interaction.getEntity().getId());
        }
    }
}
