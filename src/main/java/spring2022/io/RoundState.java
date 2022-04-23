package spring2022.io;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoundState {
    @ScanIndex(0)
    private int myHealth;
    @ScanIndex(1)
    private int myMana;
    @ScanIndex(2)
    private int oppHealth;
    @ScanIndex(3)
    private int oppMana;
    @ScanIndex(4)
    private int entityCount;

    public void reduceMana(int i) {
        myMana -= i;
    }
}
