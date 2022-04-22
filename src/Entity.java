public class Entity {
    public static final Entity NVL = new Entity(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
    private final int id;
    private final int type;
    private final Coordinate position;
    private final int shieldLife;
    private final int isControlled;
    private final int health;
    private final Coordinate velocity;
    private final int nearBase;
    private final int threatFor;
    private MonsterAttributes currentTarget;

    public Entity(int id, int type, int x, int y, int shieldLife, int isControlled, int health, int vx, int vy, int nearBase, int threatFor, MonsterAttributes currentTarget) {
        this.id = id;
        this.type = type;
        this.position = new Coordinate(x, y);
        this.shieldLife = shieldLife;
        this.isControlled = isControlled;
        this.health = health;
        this.velocity = new Coordinate(vx, vy);
        this.nearBase = nearBase;
        this.threatFor = threatFor;
        this.currentTarget = currentTarget;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getShieldLife() {
        return shieldLife;
    }

    public int getIsControlled() {
        return isControlled;
    }

    public int getHealth() {
        return health;
    }

    public int getNearBase() {
        return nearBase;
    }

    public int getThreatFor() {
        return threatFor;
    }

    public MonsterAttributes getCurrentTarget() {
        return currentTarget;
    }

    public Coordinate getPosition() {
        return position;
    }

    public Coordinate getVelocity() {
        return velocity;
    }

    public double distanceTo(Entity other) {
        return position.distanceTo(other.getPosition());
    }

    public double distanceTo(Coordinate coordinate) {
        return position.distanceTo(coordinate);
    }

    public void setCurrentTarget(MonsterAttributes currentTarget) {
        this.currentTarget = currentTarget;
    }

    public Coordinate predictPosition(int rounds) {
        int x = position.getX() + velocity.getX() * rounds;
        int y = position.getY() + velocity.getY() * rounds;
        return new Coordinate(x, y);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", type=" + type +
                ", health=" + health +
                ", nearBase=" + nearBase +
                ", threatFor=" + threatFor +
                '}';
    }
}
