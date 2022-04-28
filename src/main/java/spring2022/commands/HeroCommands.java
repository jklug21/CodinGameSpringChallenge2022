package spring2022.commands;

import spring2022.util.Coordinate;

public class HeroCommands {

    public static HeroCommand move(Coordinate target, int id) {
        return move(target, String.valueOf(id));
    }

    public static HeroCommand move(Coordinate target, String message) {
        return new MoveCommand(target, message);
    }

    public static HeroCommand castWindTowards(Coordinate target, String message) {
        return new WindCommand(target, message);
    }

    public static HeroCommand shield(int targetId, String message) {
        return new ShieldCommand(targetId, message);
    }

    public static HeroCommand shield(int targetId) {
        return new ShieldCommand(targetId, "");
    }

    public static HeroCommand control(int targetId, Coordinate target, String message) {
        return new ControlCommand(targetId, target, message);
    }
}
