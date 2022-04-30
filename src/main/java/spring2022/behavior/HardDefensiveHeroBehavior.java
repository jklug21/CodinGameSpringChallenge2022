package spring2022.behavior;

import spring2022.GameState;
import spring2022.commands.HeroCommand;
import spring2022.commands.HeroCommands;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;
import spring2022.io.RoundState;
import spring2022.util.Constants;
import spring2022.util.Coordinate;
import spring2022.util.Decision;
import spring2022.util.DecisionChain;

public class HardDefensiveHeroBehavior implements HeroBehavior {

    @Override
    public int sortEnemies(InteractionAttributes m1, InteractionAttributes m2) {
        return DecisionChain.of(Decision.closerToBase(m1, m2));
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
    public Coordinate getIdleCoordinate(int i) {
        Coordinate ownBase = GameState.get().getOwnBase();
        int x = Math.abs(ownBase.getX() - 900);
        int y = Math.abs(ownBase.getY() - 900);
        return new Coordinate(x, y);
    }

    @Override
    public HeroCommand getNextAction(InteractionAttributes interaction) {
        GameState state = GameState.get();
        Coordinate ownBase = state.getOwnBase();
        if (interaction == null) {
            return HeroCommands.move(getIdleCoordinate(0), "Holding");
        }
        RoundState round = state.getRoundState();
        int mana = round.getMyMana();
        Hero hero = interaction.getHero();
        Coordinate enemyBase = state.getEnemyBase();

        if (ownBase.distanceTo(hero.getPosition()) > 1280 && mana > Constants.SPELL_COST) {
            return HeroCommands.move(getIdleCoordinate(0), "Holding");
        } else {
            return HeroCommands.castWindTowards(enemyBase, "Woosh");
        }
    }
}
