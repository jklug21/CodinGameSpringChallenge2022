package spring2022;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import spring2022.domain.Entity;
import spring2022.strategy.Flags;
import spring2022.util.Coordinate;

@Getter
public class BattlefieldAnalyzer {
    private List<Entity> criticalMonsters;
    private Entity interceptHero;

    public void analyzeBattlefield(GameState state) {
        criticalMonsters = new ArrayList<>();
        Collection<Entity> monsters = state.getMonsters().values();
        Coordinate ownBase = state.getOwnBase();
        List<Entity> closestMonsters = monsters.stream()
                .sorted((m1, m2) -> (int) (m1.distanceTo(ownBase) - m2.distanceTo(ownBase)))
                .collect(Collectors.toList());
        criticalMonsters = closestMonsters.stream().filter(m -> m.distanceTo(ownBase) <= 2600)
                .collect(Collectors.toList());
        if (criticalMonsters.size() > 4) {
            Flags.getInstance().raiseFlag(Flags.HARD_DEFENSE_NEEDED);
        } else {
            Flags.getInstance().lowerFlag(Flags.HARD_DEFENSE_NEEDED);
        }
        List<Entity> attackingHeroes = state.getOppHeroes().values().stream().filter(e -> e.distanceTo(ownBase) < 8000).collect(Collectors.toList());
        if (attackingHeroes.size() > 0) {
            interceptHero = attackingHeroes.get(0);
            Flags.getInstance().raiseFlag(Flags.BASE_UNDER_ATTACK);
            if (criticalMonsters.size() > 0) {
                Flags.getInstance().raiseFlag(Flags.WIND_STRIKE_POSSIBLE);
            } else {
                Flags.getInstance().lowerFlag(Flags.WIND_STRIKE_POSSIBLE);
            }
        } else {
            if (interceptHero != null && state.getOppHeroes().values().stream().filter(e -> e.distanceTo(ownBase) > 10000).anyMatch(e -> e.getId() == interceptHero.getId())) {
                Flags.getInstance().lowerFlag(Flags.BASE_UNDER_ATTACK);
            }
        }
        if (state.getRound() == 60) {
            Flags.getInstance().raiseFlag(Flags.SECOND_PHASE);
        }
    }
}
