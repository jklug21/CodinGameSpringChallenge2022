package spring2022.behavior;

import spring2022.GameState;
import spring2022.commands.HeroCommand;
import spring2022.commands.HeroCommands;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;
import spring2022.util.ConsiderIf;
import spring2022.util.Constants;
import spring2022.util.Coordinate;
import spring2022.util.Decision;
import spring2022.util.DecisionChain;

public class ManaHunterHeroBehavior implements HeroBehavior {
    public static final int[] POS_PRESET = {8000, 1200};

    @Override
    public int sortEnemies(InteractionAttributes ia1, InteractionAttributes ia2) {
        return DecisionChain.of(
                Decision.currentTargetInRange(ia1, ia2),
                Decision.largerTargetGroup(ia1, ia2)
        );
    }

    @Override
    public boolean considerEnemy(InteractionAttributes m) {
        return ConsiderIf.isMonster(m) &&
                ConsiderIf.heroCanReachBeforeExitingMap(m, Constants.MELEE_RANGE) &&
                !ConsiderIf.targetsEnemyBase(m);
    }

    @Override
    public Coordinate getIdleCoordinate(int i) {
        Coordinate ownBase = GameState.get().getOwnBase();
        int x = Math.abs(ownBase.getX() - POS_PRESET[i % 2]);
        int y = Math.abs(ownBase.getY() - POS_PRESET[1 - i % 2]);
        return new Coordinate(x, y);
    }

    @Override
    public HeroCommand getNextAction(InteractionAttributes interactionAttributes) {
        GameState state = GameState.get();
        Coordinate enemyBase = state.getEnemyBase();
        Entity entity = interactionAttributes.getEntity();
        Hero hero = interactionAttributes.getHero();
        if (entity.getHealth() > 10 && state.getRoundState().getMyMana() > 100 && interactionAttributes.getDistanceToHero() < 2200) {
            if (entity.getThreatFor() == Faction.ENEMY && entity.distanceTo(enemyBase) < 6000) {
                hero.setCurrentTarget(InteractionAttributes.NVL);
                return HeroCommands.shield(entity.getId());
            } else if (interactionAttributes.getDistanceToBase() > 5000) {
                hero.setCurrentTarget(InteractionAttributes.NVL);
                return HeroCommands.monsterAttack(entity.getId(), "Attack, my minion!");
            }
        }
        return HeroCommands.attack(hero, interactionAttributes.getEntity());
    }
}
