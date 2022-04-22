public class AttributeMapper {
    private final GameState state;
    private final Entity hero;

    public AttributeMapper(GameState state, Entity hero) {
        this.state = state;
        this.hero = hero;
    }

    public MonsterAttributes calculateAttributes(Entity monster) {
        double distanceToBase = monster.distanceTo(state.getBasePosition());
        double distanceToHero = monster.getPosition().distanceTo(hero.getPosition());
        boolean targetedByOtherHero = state.getMyHeroes().values().stream().filter(h -> h.getId() != hero.getId()).anyMatch(h -> h.getCurrentTarget().getMonster().getId() == monster.getId());
        double monsterSpeed = monster.getVelocity().getHypotenuse();
        int reachedInRounds = Helpers.timeToCollision(hero, monster);
        Coordinate rendezvous = monster.predictPosition(reachedInRounds);
        return new MonsterAttributes(monster, distanceToBase, monsterSpeed, distanceToHero, targetedByOtherHero, rendezvous);
    }
}
