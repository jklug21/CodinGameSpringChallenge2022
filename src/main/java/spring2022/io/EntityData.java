package spring2022.io;

import lombok.Getter;
import lombok.NoArgsConstructor;
import spring2022.domain.Faction;

@Getter
@NoArgsConstructor
public class EntityData {
    @ScanIndex(0)
    private int id;
    @ScanIndex(1)
    private int faction;
    @ScanIndex(2)
    private int x;
    @ScanIndex(3)
    private int y;
    @ScanIndex(4)
    private int shieldLife;
    @ScanIndex(5)
    private int isControlled;
    @ScanIndex(6)
    private int health;
    @ScanIndex(7)
    private int vx;
    @ScanIndex(8)
    private int vy;
    @ScanIndex(9)
    private int nearBase;
    @ScanIndex(10)
    private int threatFor;

    public Faction getFaction() {
        return Faction.parse(faction);
    }

    public Faction getThreatFor() {
        return Faction.parse(threatFor);
    }
}
