package spring2022.domain;

import lombok.Getter;
import lombok.Setter;
import spring2022.GameState;
import spring2022.io.EntityData;
import spring2022.util.Coordinate;
import spring2022.util.Helpers;

import java.util.Objects;

@Getter
@Setter
public class Entity {
    public static final Entity NVL = new Entity(0, Faction.NVL, 0, 0, 0, 0, 0, 0, 0, 0);
    private final int id;
    private final Faction faction;
    private final int shieldLife;
    private final int isControlled;
    private final int nearBase;
    private final Faction threatFor;

    private int health;
    private Coordinate velocity;
    private Coordinate position;

    public Entity(EntityData data) {
        GameState state = GameState.get();
        this.id = data.getId();
        this.faction = data.getFaction();
        this.position = new Coordinate(data.getX(), data.getY());
        this.shieldLife = data.getShieldLife();
        this.isControlled = data.getIsControlled();
        this.health = data.getHealth();
        this.velocity = new Coordinate(data.getVx(), data.getVy());
        this.nearBase = data.getNearBase();
        this.threatFor = Helpers.calculateThreatFor(data.getX(), data.getY(), data.getVx(), data.getVy(), state.getEnemyBase(), state.getOwnBase());
    }

    public Entity(int id, Faction faction, int x, int y, int shieldLife, int isControlled, int health, int vx, int vy, int nearBase) {
        GameState state = GameState.get();
        this.id = id;
        this.faction = faction;
        this.position = new Coordinate(x, y);
        this.shieldLife = shieldLife;
        this.isControlled = isControlled;
        this.health = health;
        this.velocity = new Coordinate(vx, vy);
        this.nearBase = nearBase;
        this.threatFor = Helpers.calculateThreatFor(x, y, vx, vy, state.getEnemyBase(), state.getOwnBase());
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
                ", f=" + faction +
                ", sL=" + shieldLife +
                ", c=" + isControlled +
                ", hp=" + health +
                ", v=" + velocity +
                ", nearBase=" + nearBase +
                ", threatFor=" + threatFor +
                ", p=" + position +
                '}';
    }

    public String toStringShort() {
        return "{" + id +
                " (" + faction +
                ") hp=" + health +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void doDamage() {
        health -= 2;
    }
}
