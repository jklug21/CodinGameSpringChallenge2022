package spring2022.behavior;

import spring2022.GameParameters;
import spring2022.GameState;
import spring2022.commands.HeroCommand;
import spring2022.commands.HeroCommands;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;
import spring2022.io.RoundState;
import spring2022.strategy.Flags;
import spring2022.util.ConsiderIf;
import spring2022.util.Constants;
import spring2022.util.Coordinate;
import spring2022.util.Decision;
import spring2022.util.DecisionChain;
import spring2022.util.Log;

public class DefensiveHeroBehavior implements HeroBehavior {
    public static final int[] POS_PRESET = {5000, 3800, 2000};

    @Override
    public int sortEnemies(InteractionAttributes m1, InteractionAttributes m2) {
        return DecisionChain.of(
                Decision.onlyOneInRange(m1, m2, GameParameters.FIGHTING_AREA),
                Decision.onlyOneInRange(m1, m2, GameParameters.SAFETY_AREA),
                Decision.onlyOneInRange(m1, m2, GameParameters.CRITICAL_AREA),
                Decision.closerToBase(m1, m2)
        );
    }

    @Override
    public boolean considerEnemy(InteractionAttributes m) {
        Entity entity = m.getEntity();
        boolean heroCanReachBeforeExitingMap = ConsiderIf.heroCanReachBeforeExitingMap(m, Constants.WIND_RANGE);
        if (!heroCanReachBeforeExitingMap) {
            Log.log(this, "Ignoring " + m.getEntity().getId());
        }

        return ConsiderIf.isMonster(entity) &&
                ConsiderIf.targetsMe(entity) &&
                heroCanReachBeforeExitingMap;
    }

    @Override
    public Coordinate getIdleCoordinate(int i) {
        Coordinate ownBase = GameState.get().getOwnBase();
        int x = Math.abs(ownBase.getX() - POS_PRESET[i]);
        int y = Math.abs(ownBase.getY() - POS_PRESET[2 - i]);
        return new Coordinate(x, y);
    }

    @Override
    public HeroCommand getNextAction(InteractionAttributes interaction) {
        GameState state = GameState.get();
        if (interaction == null) {
            return null;
        }
        RoundState round = state.getRoundState();
        int mana = round.getMyMana();
        Coordinate enemyBase = state.getEnemyBase();
        Hero hero = interaction.getHero();
        Entity entity = interaction.getEntity();
        Faction faction = entity.getFaction();

        if (faction == Faction.MONSTER &&
                interaction.getDistanceToHero() < Constants.WIND_RANGE &&
                (interaction.getDistanceToBase() - Constants.ENEMY_RANGE) < interaction.getSpeed() * 2 &&
                mana >= Constants.SPELL_COST) {
            return HeroCommands.castWindTowards(enemyBase, "Woosh");
        } else if (Flags.getInstance().isFlagRaised(Flags.BASE_UNDER_ATTACK | Flags.WIND_STRIKE_POSSIBLE) &&
                state.getAnalyzer().getCriticalMonsters().stream().anyMatch(m -> m.distanceTo(hero) < Constants.WIND_RANGE - 400)) {
            return HeroCommands.castWindTowards(enemyBase, "Woosh");
        } else {
            return HeroCommands.attack(hero, entity);
        }
    }
}
