package spring2022;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import spring2022.behavior.HeroBehaviorContainer;
import spring2022.domain.Entity;
import spring2022.domain.Faction;
import spring2022.domain.Hero;
import spring2022.io.EntityData;
import spring2022.io.InitialData;
import spring2022.io.RoundState;
import spring2022.util.Coordinate;
import spring2022.util.Log;

@Getter
public class GameState {
    private static GameState instance = null;

    private final Coordinate ownBase;
    private final int heroesPerPlayer;
    private final Coordinate enemyBase;
    private final List<HeroBehaviorContainer> heroBehaviors;
    private final BattlefieldAnalyzer analyzer;

    private Map<Integer, Entity> oppHeroes = new HashMap<>();
    private Map<Integer, Entity> monsters = new HashMap<>();
    private Map<Integer, Hero> myHeroes = new HashMap<>();
    private RoundState roundState;
    private int round = -1;
    private List<Integer> heroesAffectedByMagic = new ArrayList<>();

    GameState(InitialData initialData, List<HeroBehaviorContainer> initialBehavior, BattlefieldAnalyzer analyzer) {
        this.analyzer = analyzer;
        int baseX = initialData.getBaseX();
        int baseY = initialData.getBaseY();
        this.ownBase = new Coordinate(baseX, baseY);
        this.enemyBase = new Coordinate(Math.abs(baseX - 17630), Math.abs(baseY - 9000));
        this.heroesPerPlayer = initialData.getHeroesPerPlayer();
        this.heroBehaviors = initialBehavior;
    }

    public static GameState get() {
        return instance;
    }

    public static GameState init(InitialData initialData, List<HeroBehaviorContainer> initialBehavior, BattlefieldAnalyzer analyzer) {
        instance = new GameState(initialData, initialBehavior, analyzer);
        return instance;
    }

    public void updateState(RoundState roundState, List<EntityData> entities) {
        this.round++;
        this.roundState = roundState;
        extrapolatePosition(oppHeroes.values());
        oppHeroes.putAll(entities.stream().filter(e -> e.getFaction() == Faction.ENEMY)
                .map(Entity::new)
                .collect(Collectors.toMap(Entity::getId, e -> e)));

        extrapolatePosition(monsters.values());

        monsters = monsters.entrySet().stream()
                // remove monsters killed in the last round
                .filter(e -> myHeroes.values()
                        .stream().noneMatch(h -> e.getValue().getHealth() <= 2 && h.distanceTo(e.getValue()) <= 800))
                // remove monsters who left the map
                .filter(this::insideMapArea)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // add/update actually seen monsters
        monsters.putAll(entities.stream().filter(e -> e.getFaction() == Faction.MONSTER)
                .map(Entity::new)
                .collect(Collectors.toMap(Entity::getId, e -> e)));

        Map<Integer, Hero> oldHeroes = myHeroes;
        myHeroes = entities.stream().filter(e -> e.getFaction() == Faction.OWN)
                .map(Hero::new)
                .map(this::stayOnTarget)
                .collect(Collectors.toMap(Entity::getId, e -> e));
        heroesAffectedByMagic = oldHeroes.entrySet().stream()
                .peek(e -> Log.log(this, e.getValue().predictPosition(1) + " " + myHeroes.get(e.getKey()).getPosition()))
                .filter(e -> e.getValue().getTargetCoordinate()
                        .distanceTo(myHeroes.get(e.getKey()).getPosition()) > 10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (heroesAffectedByMagic.size() > 0) {
            Log.log(this, "Affected: " + heroesAffectedByMagic.stream().map(String::valueOf).collect(Collectors.joining(", ")));
        }
    }

    private boolean insideMapArea(Map.Entry<Integer, Entity> entity) {
        Coordinate pos = entity.getValue().getPosition();
        return !(pos.getX() < 0 || pos.getY() < 0 || pos.getX() > 17630 || pos.getY() > 9000);
    }

    private void extrapolatePosition(Collection<Entity> entities) {
        for (Entity entity : entities) {
            entity.setPosition(entity.predictPosition(1));
        }
    }

    private Hero stayOnTarget(Hero hero) {
        if (myHeroes.containsKey(hero.getId())) {
            hero.setCurrentTarget(myHeroes.get(hero.getId()).getCurrentTarget());
        }
        return hero;
    }
}
