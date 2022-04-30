package spring2022.util;

import org.junit.jupiter.api.Test;

class HelpersTest {

    @Test
    void calculateThreatFor() {
        Coordinate enemyBase = new Coordinate(0, 0);
        Coordinate ownBase = new Coordinate(17630, 9000);
        System.out.println(Helpers.calculateThreatFor(10072, 5041, -357, -178, enemyBase, ownBase));
        System.out.println(Helpers.calculateThreatFor(11144, 5576, 137, 375, enemyBase, ownBase));
        System.out.println(Helpers.calculateThreatFor(10688, 4950, -357, -178, enemyBase, ownBase));
    }
}