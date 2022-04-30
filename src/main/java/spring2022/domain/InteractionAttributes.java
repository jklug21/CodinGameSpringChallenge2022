package spring2022.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import spring2022.util.Constants;
import spring2022.util.Helpers;

@Getter
@AllArgsConstructor
public class InteractionAttributes {
    public static final InteractionAttributes NVL = new InteractionAttributes(Entity.NVL, Hero.NVL, /*null,*/ 0, 0, 0, 0, false, 0);
    private final Entity entity;
    private final Hero hero;
    private final double distanceToBase;
    private final double distanceToEnemyBase;
    private final double speed;
    private final double distanceToHero;
    private final boolean targetedByOtherHero;
    private final int buddyCount;

    public String debug(Hero hero) {
        int reachedInRoundsMelee = Helpers.timeToCollision(hero, entity, Constants.MELEE_RANGE);
        int reachedInRoundsWind = Helpers.timeToCollision(hero, entity, Constants.WIND_RANGE);
        return String.format("{target=%s, distanceToBase=%s, distanceToHero=%s, targetedByOtherHero=%s, monsterSpeed=%s, meleeInRounds=%d, windInRounds=%d}", entity, distanceToBase, distanceToHero, targetedByOtherHero, entity.getVelocity().getHypotenuse(), reachedInRoundsMelee, reachedInRoundsWind);
    }

    public String debugShort(Hero hero) {
        int reachedInRoundsMelee = Helpers.timeToCollision(hero, entity, Constants.MELEE_RANGE);
        int reachedInRoundsWind = Helpers.timeToCollision(hero, entity, Constants.WIND_RANGE);
        return String.format("{target=%s, distanceToBase=%.0f, distanceToHero=%.0f, targetedByOtherHero=%s, monsterSpeed=%.0f, meleeInRounds=%d, windInRounds=%d}", entity.toStringShort(), distanceToBase, distanceToHero, targetedByOtherHero, entity.getVelocity().getHypotenuse(), reachedInRoundsMelee, reachedInRoundsWind);
    }
}
