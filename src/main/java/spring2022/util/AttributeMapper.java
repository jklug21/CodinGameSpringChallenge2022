package spring2022.util;

import spring2022.GameState;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.domain.InteractionAttributes;

public class AttributeMapper {
    private final GameState state;
    private final Hero hero;

    public AttributeMapper(GameState state, Hero hero) {
        this.state = state;
        this.hero = hero;
    }

    public InteractionAttributes calculateAttributes(Entity entity) {
        double distanceToBase = entity.distanceTo(state.getOwnBase());
        double distanceToEnemyBase = entity.distanceTo(state.getEnemyBase());
        double distanceToHero = entity.getPosition().distanceTo(hero.getPosition());
        boolean targetedByOtherHero = state.getMyHeroes().values().stream()
                .filter(h -> h.getId() != hero.getId())
                .filter(h -> h.getCurrentTarget() != null)
                .anyMatch(h -> h.getCurrentTarget().getEntity().getId() == entity.getId());
        double monsterSpeed = entity.getVelocity().getHypotenuse();
        int reachedInRounds = Helpers.timeToCollision(hero, entity);
        Coordinate rendezvous = entity.predictPosition(reachedInRounds);
        Coordinate rendezvous2 = entity.predictPosition(reachedInRounds + 1);
        int buddyCount;
        if (entity.getFaction() == Faction.MONSTER) {
            buddyCount = (int) state.getMonsters().values().stream()
                    .filter(m -> m.distanceTo(entity) < Constants.MELEE_RANGE * 2)
                    .count();
        } else {
            buddyCount = (int) state.getOppHeroes().values().stream()
                    .filter(m -> m.distanceTo(entity) < Constants.MELEE_RANGE * 2)
                    .count();
        }
        return new InteractionAttributes(entity, hero, distanceToBase, distanceToEnemyBase, monsterSpeed, distanceToHero, targetedByOtherHero, rendezvous, rendezvous2, buddyCount);
    }
}
