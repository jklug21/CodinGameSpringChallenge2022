public class MonsterAttributes {
    public static final MonsterAttributes NVL = new MonsterAttributes(Entity.NVL, 0, 0, 0, false, new Coordinate(0, 0));
    private final Entity monster;
    private final double distanceToBase;
    private final double speed;
    private final double distanceToHero;
    private final boolean targetedByOtherHero;
    private final Coordinate rendezvous;

    MonsterAttributes(Entity monster, double distanceToBase, double speed, double distanceToHero, boolean targetedByOtherHero, Coordinate rendezvous) {
        this.monster = monster;
        this.distanceToBase = distanceToBase;
        this.speed = speed;
        this.distanceToHero = distanceToHero;
        this.targetedByOtherHero = targetedByOtherHero;
        this.rendezvous = rendezvous;
    }

    public Entity getMonster() {
        return monster;
    }

    public double getDistanceToBase() {
        return distanceToBase;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDistanceToHero() {
        return distanceToHero;
    }

    public boolean isTargetedByOtherHero() {
        return targetedByOtherHero;
    }

    public Coordinate getRendezvous() {
        return rendezvous;
    }

    public String debug(Entity hero, GameState state) {
        int reachedInRounds = Helpers.timeToCollision(hero, monster);
        return String.format("MonsterAttributes{monster=%s, distanceToBase=%s, distanceToHero=%s, targetedByOtherHero=%s, monsterSpeed=%s, threatForBase=%s, reachedInRounds=%d, distanceToBaseAfterRounds=%s}", monster, distanceToBase, distanceToHero, targetedByOtherHero, monster.getVelocity().getHypotenuse(), monster.getThreatFor() == 1 && monster.getNearBase() == 1, reachedInRounds, Helpers.predictDistance(reachedInRounds, monster, state.getBasePosition()));
    }
}
