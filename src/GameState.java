import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameState {
    public static final int TYPE_MONSTER = 0;
    public static final int TYPE_MY_HERO = 1;
    public static final int TYPE_OP_HERO = 2;

    private final Coordinate basePosition;
    private final int heroesPerPlayer;

    private List<Entity> oppHeroes = new ArrayList<>();
    private List<Entity> monsters = new ArrayList<>();
    private final Map<Integer, Entity> myHeroes = new HashMap<>();
    private int myHealth; // Your base health
    private int myMana; // Ignore in the first league; Spend ten mana to cast a spell
    private int oppHealth;
    private int oppMana;
    private int entityCount;

    GameState(int baseX, int baseY, int heroesPerPlayer) {
        this.basePosition = new Coordinate(baseX, baseY);
        this.heroesPerPlayer = heroesPerPlayer;
    }

    public void updateState(Scanner in) {
        myHealth = in.nextInt(); // Your base health
        myMana = in.nextInt(); // Ignore in the first league; Spend ten mana to cast a spell
        oppHealth = in.nextInt();
        oppMana = in.nextInt();
        entityCount = in.nextInt(); // Amount of heros and monsters you can see

        oppHeroes = new ArrayList<>(entityCount);
        monsters = new ArrayList<>(entityCount);
        for (int i = 0; i < entityCount; i++) {
            int id = in.nextInt();              // Unique identifier
            int type = in.nextInt();            // 0=monster, 1=your hero, 2=opponent hero
            int x = in.nextInt();               // Position of this entity
            int y = in.nextInt();
            int shieldLife = in.nextInt();      // Ignore for this league; Count down until shield spell fades
            int isControlled = in.nextInt();    // Ignore for this league; Equals 1 when this entity is under a control spell
            int health = in.nextInt();          // Remaining health of this monster
            int vx = in.nextInt();              // Trajectory of this monster
            int vy = in.nextInt();
            int nearBase = in.nextInt();        // 0=monster with no target yet, 1=monster targeting a base
            int threatFor = in.nextInt();       // Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither

            Entity entity = new Entity(
                    id, type, x, y, shieldLife, isControlled, health, vx, vy, nearBase, threatFor, MonsterAttributes.NVL
            );
            switch (type) {
                case TYPE_MONSTER:
                    monsters.add(entity);
                    break;
                case TYPE_MY_HERO:
                    if (myHeroes.containsKey(id)) {
                        entity.setCurrentTarget(myHeroes.get(id).getCurrentTarget());
                    }
                    myHeroes.put(id, entity);
                    break;
                case TYPE_OP_HERO:
                    oppHeroes.add(entity);
                    break;
            }
        }
    }

    public int getHeroesPerPlayer() {
        return heroesPerPlayer;
    }

    public List<Entity> getOppHeroes() {
        return oppHeroes;
    }

    public List<Entity> getMonsters() {
        return monsters;
    }

    public Map<Integer, Entity> getMyHeroes() {
        return myHeroes;
    }

    public int getMyHealth() {
        return myHealth;
    }

    public int getMyMana() {
        return myMana;
    }

    public int getOppHealth() {
        return oppHealth;
    }

    public int getOppMana() {
        return oppMana;
    }

    public int getEntityCount() {
        return entityCount;
    }

    public Coordinate getBasePosition() {
        return basePosition;
    }
}
