package spring2022;

import java.util.ArrayList;
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

@Getter
public class GameState {
    private final Coordinate ownBase;
    private final int heroesPerPlayer;
    private final Coordinate enemyBase;
    private final List<HeroBehaviorContainer> heroBehaviors;

    private List<Entity> oppHeroes = new ArrayList<>();
    private List<Entity> monsters = new ArrayList<>();
    private Map<Integer, Hero> myHeroes = new HashMap<>();
    private RoundState roundState;

    GameState(InitialData initialData, List<HeroBehaviorContainer> initialBehavior) {
        int baseX = initialData.getBaseX();
        int baseY = initialData.getBaseY();
        this.ownBase = new Coordinate(baseX, baseY);
        this.enemyBase = new Coordinate(Math.abs(baseX - 17630), Math.abs(baseY - 9000));
        this.heroesPerPlayer = initialData.getHeroesPerPlayer();
        this.heroBehaviors = initialBehavior;
    }

    public void updateState(RoundState roundState, List<EntityData> entities) {
        this.roundState = roundState;
        oppHeroes = entities.stream().filter(e -> e.getFaction() == Faction.ENEMY)
                .map(Entity::new)
                .collect(Collectors.toList());
        monsters = entities.stream().filter(e -> e.getFaction() == Faction.MONSTER)
                .map(Entity::new)
                .collect(Collectors.toList());
        myHeroes = entities.stream().filter(e -> e.getFaction() == Faction.OWN)
                .map(Hero::new)
                .map(this::stayOnTarget)
                .collect(Collectors.toMap(Entity::getId, e -> e));
    }

    private Hero stayOnTarget(Hero hero) {
        if (myHeroes.containsKey(hero.getId())) {
            hero.setCurrentTarget(myHeroes.get(hero.getId()).getCurrentTarget());
        }
        return hero;
    }
}