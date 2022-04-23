package spring2022.io;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialData {
    @ScanIndex(0)
    private int baseX;
    @ScanIndex(1)
    private int baseY;
    @ScanIndex(2)
    private int heroesPerPlayer;
}

