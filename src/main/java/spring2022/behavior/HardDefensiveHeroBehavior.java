package spring2022.behavior;

import spring2022.GameParameters;
import spring2022.GameState;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;
import spring2022.io.RoundState;
import spring2022.util.Constants;
import spring2022.util.Coordinate;
import spring2022.util.Helpers;
import spring2022.util.HeroCommands;
import spring2022.util.Log;

public class HardDefensiveHeroBehavior implements HeroBehavior {

    @Override
    public int sortEnemies(InteractionAttributes m1, InteractionAttributes m2) {
        return (int) (m2.getDistanceToBase() - m1.getDistanceToBase());
    }

    @Override
    public boolean considerEnemy(InteractionAttributes m) {
        Entity entity = m.getEntity();
        Faction faction = entity.getFaction();

        return (faction == Faction.MONSTER &&
                m.getDistanceToBase() < 2000 &&
                m.getDistanceToHero() < 1280);
    }

    @Override
    public Coordinate getIdleCoordinate(Coordinate ownBase, Coordinate enemyBase, int i) {
        int x = Math.abs(ownBase.getX() - 900);
        int y = Math.abs(ownBase.getY() - 900);
        return new Coordinate(x, y);
    }

    @Override
    public Runnable getNextAction(InteractionAttributes interaction) {
        GameState state = GameState.get();
        Coordinate ownBase = state.getOwnBase();
        if (interaction == null) {
            return HeroCommands.move(getIdleCoordinate(ownBase, null, 0), "Holding");
        }
        RoundState round = state.getRoundState();
        int mana = round.getMyMana();
        Hero hero = interaction.getHero();
        Coordinate enemyBase = state.getEnemyBase();

        if (ownBase.distanceTo(hero.getPosition()) > 1280 && mana > Constants.SPELL_COST) {
            return HeroCommands.move(getIdleCoordinate(ownBase, null, 0), "Holding");
        } else {
            round.reduceMana(Constants.SPELL_COST);
            return HeroCommands.castWindTowards(enemyBase, "Woosh");
        }
    }
}
