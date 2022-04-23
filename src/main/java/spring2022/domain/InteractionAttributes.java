package spring2022.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import spring2022.GameState;
import spring2022.util.Coordinate;
import spring2022.util.Helpers;

@Getter
@AllArgsConstructor
public class InteractionAttributes {
    public static final InteractionAttributes NVL = new InteractionAttributes(Entity.NVL, Hero.NVL, null, 0, 0, 0, false, new Coordinate(0, 0), new Coordinate(0, 0), 0);
    private final Entity entity;
    private final Hero hero;
    private final GameState state;
    private final double distanceToBase;
    private final double speed;
    private final double distanceToHero;
    private final boolean targetedByOtherHero;
    private final Coordinate rendezvous;
    private final Coordinate rendezvous2;
    private final int buddyCount;

    public String debug(Entity hero) {
        int reachedInRounds = Helpers.timeToCollision(hero, entity);
        return String.format("{target=%s, distanceToBase=%s, distanceToHero=%s, targetedByOtherHero=%s, monsterSpeed=%s, reachedInRounds=%d}", entity, distanceToBase, distanceToHero, targetedByOtherHero, entity.getVelocity().getHypotenuse(), reachedInRounds);
    }

    public String debugShort(Entity hero) {
        int reachedInRounds = Helpers.timeToCollision(hero, entity);
        return String.format("{target=%s, distanceToBase=%.0f, distanceToHero=%.0f, targetedByOtherHero=%s, monsterSpeed=%.0f, reachedInRounds=%d}", entity.toStringShort(), distanceToBase, distanceToHero, targetedByOtherHero, entity.getVelocity().getHypotenuse(), reachedInRounds);
    }
}
