package spring2022.behavior;

import spring2022.GameParameters;
import spring2022.GameState;
import spring2022.commands.HeroCommand;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;
import spring2022.util.Constants;
import spring2022.util.Coordinate;
import spring2022.util.Helpers;
import spring2022.commands.HeroCommands;
import spring2022.util.Log;

public class ManaHunterHeroBehavior implements HeroBehavior {
    public static final int[] POS_PRESET = {8000, 1200};

    @Override
    public int sortEnemies(InteractionAttributes m1, InteractionAttributes m2) {
        InteractionAttributes currentTarget = m1.getHero().getCurrentTarget();
        int currentTargetId = -1;
        if (currentTarget != null) {
            currentTargetId = currentTarget.getEntity().getId();
        }
        double baseDist1 = m1.getDistanceToBase();
        double baseDist2 = m2.getDistanceToBase();

        // stay on target
        if (m1.getEntity().getId() == currentTargetId) {
            return -1;
        }
        if (m2.getEntity().getId() == currentTargetId) {
            return 1;
        }

        // scale buddy count to distance from hero linearly
        double buddyScale1 = (m1.getBuddyCount() + 1) * Constants.MELEE_RANGE_SQ / m1.getDistanceToHero() * baseDist1;
        double buddyScale2 = (m2.getBuddyCount() + 1) * Constants.MELEE_RANGE_SQ / m2.getDistanceToHero() * baseDist2;
        return (buddyScale2 - buddyScale1) > 0 ? 1 : -1;
    }

    @Override
    public boolean considerEnemy(InteractionAttributes m) {
        Hero hero = m.getHero();
        Entity entity = m.getEntity();
        int roundsToStrike = (int) ((m.getDistanceToBase() - 300) / 400);
        int timeToCollision = Helpers.timeToCollision(hero, entity);
        return entity.getFaction() == Faction.MONSTER && timeToCollision < roundsToStrike &&
                (entity.getThreatFor() != Faction.ENEMY);
    }

    @Override
    public Coordinate getIdleCoordinate(Coordinate ownBase, Coordinate enemyBase, int i) {
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
        if (interactionAttributes.getDistanceToBase() < GameParameters.CRITICAL_AREA) {
            int heroId = hero.getId();
            Log.log(this, heroId + ": Switching to " + HeroClass.DEFENDER);
            HeroBehaviorContainer behavior = state.getHeroBehaviors().get(heroId % 3);
            behavior.setTempClass(HeroClass.DEFENDER);
            behavior.setEndCondition(() -> state.getMonsters().values().stream()
                    .noneMatch(m -> m.getId() == interactionAttributes.getEntity().getId()));
        } else if (entity.getHealth() > 10 && state.getRoundState().getMyMana() > 100 && interactionAttributes.getDistanceToHero() < 2200) {
            if (entity.getThreatFor() == Faction.ENEMY && entity.distanceTo(enemyBase) < 6000) {
                state.getRoundState().reduceMana(Constants.SPELL_COST);
                hero.setCurrentTarget(InteractionAttributes.NVL);
                return HeroCommands.shield(entity.getId());
            } else if (interactionAttributes.getDistanceToBase() > 5000) {
                state.getRoundState().reduceMana(Constants.SPELL_COST);
                hero.setCurrentTarget(InteractionAttributes.NVL);
                int dx = (int) (Math.random() * 5000d);
                int dy = (int) (Math.random() * 5000d);
                Coordinate target = new Coordinate(Math.abs(enemyBase.getX() - dx), Math.abs(enemyBase.getY() - dy));
                return HeroCommands.control(entity.getId(), target, "Attack, my minion!");
            }
        }
        return HeroCommands.move(interactionAttributes.getRendezvous(), interactionAttributes.getEntity().getId());
    }
}
