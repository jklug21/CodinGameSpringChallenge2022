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
import spring2022.strategy.BattlefieldAnalyzer;
import spring2022.util.Coordinate;
import spring2022.util.Helpers;

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

        Map<Integer, Hero> oldHeroes = myHeroes;
        myHeroes = entities.stream().filter(e -> e.getFaction() == Faction.OWN)
                .map(Hero::new)
                .map(this::stayOnTarget)
                .collect(Collectors.toMap(Entity::getId, e -> e));
        heroesAffectedByMagic = oldHeroes.entrySet().stream()
                .filter(e -> e.getValue().getTargetCoordinate()
                        .distanceTo(myHeroes.get(e.getKey()).getPosition()) > 10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        extrapolatePosition(monsters.values());

        List<Entity> newMonsters = entities.stream().filter(e -> e.getFaction() == Faction.MONSTER)
                .map(Entity::new).collect(Collectors.toList());

        monsters = monsters.entrySet().stream()
                // remove monsters that should be visible but are not
                .filter(m -> {
                    boolean newContains = newMonsters.contains(m.getValue());
                    boolean heroCantSee = myHeroes.values().stream().noneMatch(h -> h.canSee(m.getValue().getPosition()));
                    boolean baseCantSee = ownBase.distanceTo(m.getValue().getPosition()) > 6000;
                    return newContains || (heroCantSee && baseCantSee);
                })
                // remove monsters who left the map
                .filter(this::insideMapArea)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // add/update actually seen monsters
        monsters.putAll(newMonsters.stream()
                .collect(Collectors.toMap(Entity::getId, e -> e)));
    }

    private boolean insideMapArea(Map.Entry<Integer, Entity> entity) {
        Coordinate pos = entity.getValue().getPosition();
        return Helpers.insideMapArea(pos);
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
