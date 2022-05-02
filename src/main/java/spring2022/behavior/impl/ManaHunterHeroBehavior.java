package spring2022.behavior.impl;

import spring2022.GameState;
import spring2022.behavior.HeroBehavior;
import spring2022.behavior.HeroClass;
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
    public static final int[] POS_PRESET = {5000, 8400, 8000, 500, 7800, 8300};

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
                //!ConsiderIf.isCurrentTarget(m) &&
                !ConsiderIf.targetedByOtherHero(m) &&
                !ConsiderIf.targetsEnemyBase(m);
    }

    @Override
    public Coordinate getIdleCoordinate(int i) {
        Coordinate ownBase = GameState.get().getOwnBase();
        int x = Math.abs(ownBase.getX() - POS_PRESET[i * 2]);
        int y = Math.abs(ownBase.getY() - POS_PRESET[i * 2 + 1]);
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
                return HeroCommands.shield(entity.getId());
            } else if (interactionAttributes.getDistanceToBase() > 5000) {
                return HeroCommands.monsterAttack(entity, "Attack, my minion!");
            }
        }
        return HeroCommands.attack(hero, interactionAttributes.getEntity());
    }

    @Override
    public HeroClass getHeroClass() {
        return HeroClass.MANA_HUNTER;
    }
}
