public class HeroCommands {
    public static void move(MonsterAttributes target) {
        Coordinate rendezvous = target.getRendezvous();
        System.out.printf("MOVE %d %d%n", rendezvous.getX(), rendezvous.getY());
    }

    public static boolean castWind(Coordinate base, MonsterAttributes target, boolean windCast) {
        if (!windCast) {
            Coordinate targetPos = target.getMonster().getPosition();
            System.out.printf("SPELL WIND %d %d%n", targetPos.getX() - (base.getX() - targetPos.getX()), targetPos.getY() - (base.getY() - targetPos.getY()));
        } else {
            move(target);
        }
        return true;
    }

    public static void takeReadyPosition(int i, int baseX, int baseY) {
        int rx = baseX - GameParameters.POS_PRESET[i];
        int ry = baseY - GameParameters.POS_PRESET[2 - i];

        System.out.printf("MOVE %d %d%n", Math.abs(rx), Math.abs(ry));
    }
}
