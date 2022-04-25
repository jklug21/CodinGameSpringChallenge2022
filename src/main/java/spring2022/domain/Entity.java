package spring2022.domain;

import lombok.Getter;
import spring2022.GameState;
import spring2022.io.EntityData;
import spring2022.util.Coordinate;
import spring2022.util.Helpers;

@Getter
public class Entity {
    public static final Entity NVL = new Entity(0, Faction.NVL, 0, 0, 0, 0, 0, 0, 0, 0, Faction.NVL);
    private final int id;
    private final Faction faction;
    private final Coordinate position;
    private final int shieldLife;
    private final int isControlled;
    private final int health;
    private final Coordinate velocity;
    private final int nearBase;
    private final Faction threatFor;

    public Entity(EntityData data) {
        this.id = data.getId();
        this.faction = data.getFaction();
        this.position = new Coordinate(data.getX(), data.getY());
        this.shieldLife = data.getShieldLife();
        this.isControlled = data.getIsControlled();
        this.health = data.getHealth();
        this.velocity = new Coordinate(data.getVx(), data.getVy());
        this.nearBase = data.getNearBase();
        this.threatFor = data.getThreatFor();
    }

    public Entity(int id, Faction faction, int x, int y, int shieldLife, int isControlled, int health, int vx, int vy, int nearBase, Faction threatFor) {
        this.id = id;
        this.faction = faction;
        this.position = new Coordinate(x, y);
        this.shieldLife = shieldLife;
        this.isControlled = isControlled;
        this.health = health;
        this.velocity = new Coordinate(vx, vy);
        this.nearBase = nearBase;
        this.threatFor = threatFor;
    }

    public boolean isShielded() {
        return shieldLife > 0;
    }

    public double distanceTo(Entity other) {
        return position.distanceTo(other.getPosition());
    }

    public double distanceTo(Coordinate coordinate) {
        return position.distanceTo(coordinate);
    }

    public Coordinate predictPosition(int rounds) {
        int x = position.getX() + velocity.getX() * rounds;
        int y = position.getY() + velocity.getY() * rounds;
        return new Coordinate(x, y);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", type=" + faction +
                ", health=" + health +
                ", nearBase=" + nearBase +
                ", threatFor=" + threatFor +
                '}';
    }

    public String toStringShort() {
        return "{" + id +
                " (" + faction +
                ") hp=" + health +
                '}';
    }
}
