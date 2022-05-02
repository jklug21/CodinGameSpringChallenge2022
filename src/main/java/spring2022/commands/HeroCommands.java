package spring2022.commands;

import spring2022.GameState;
import spring2022.commands.impl.AttackCommand;
import spring2022.commands.impl.ControlCommand;
import spring2022.commands.impl.MoveCommand;
import spring2022.commands.impl.ShieldCommand;
import spring2022.commands.impl.WindCommand;
import spring2022.domain.Entity;
import spring2022.domain.Hero;
import spring2022.util.Constants;
import spring2022.util.Coordinate;

public class HeroCommands {

    public static HeroCommand move(Coordinate target, int id) {
        return move(target, String.valueOf(id));
    }

    public static HeroCommand move(Coordinate target, String message) {
        return new MoveCommand(target, message);
    }

    public static HeroCommand attack(Hero hero, Entity target) {
        GameState state = GameState.get();
        return new AttackCommand(hero, target, String.valueOf(target.getId()), state.getMonsters().values());
    }

    public static HeroCommand attack(Hero hero, Entity target, String message) {
        GameState state = GameState.get();
        return new AttackCommand(hero, target, message, state.getMonsters().values());
    }

    public static HeroCommand castWindTowards(Coordinate target, String message) {
        GameState state = GameState.get();
        state.getRoundState().reduceMana(Constants.SPELL_COST);
        return new WindCommand(target, message);
    }

    public static HeroCommand shield(int targetId, String message) {
        GameState state = GameState.get();
        state.getRoundState().reduceMana(Constants.SPELL_COST);
        return new ShieldCommand(targetId, message);
    }

    public static HeroCommand shield(int targetId) {
        GameState state = GameState.get();
        state.getRoundState().reduceMana(Constants.SPELL_COST);
        return new ShieldCommand(targetId, "");
    }

    public static HeroCommand control(Entity entity, Coordinate target, String message) {
        GameState state = GameState.get();
        state.getRoundState().reduceMana(Constants.SPELL_COST);
        return new ControlCommand(entity, target, message);
    }

    public static HeroCommand monsterAttack(Entity entity, String message) {
        GameState state = GameState.get();
        Coordinate enemyBase = state.getEnemyBase();
        int tx = enemyBase.getX();
        int ty = enemyBase.getY();
        if (Math.random() > 0.5) {
            tx = (int) Math.abs(enemyBase.getX() - Math.random() * 5000d);
        } else {
            ty = (int) Math.abs(enemyBase.getY() - Math.random() * 5000d);
        }

        Coordinate target = new Coordinate(tx, ty);
        return control(entity, target, message);
    }
}
