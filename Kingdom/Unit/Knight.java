package Kingdom.Unit; // یا Kingdom.Unit

import Game.Player;

public class Knight extends Unit {

    public static final int KNIGHT_HP = 50;
    public static final int KNIGHT_MOVEMENT = 3;
    public static final int KNIGHT_ATTACK_POWER = 12;
    public static final int KNIGHT_ATTACK_RANGE = 1;
    public static final int KNIGHT_GOLD_COST = 5;
    public static final int KNIGHT_FOOD_COST = 3;
    public static final int KNIGHT_UNIT_SPACE = 3;

    public Knight(Player owner) {
        super(KNIGHT_HP, KNIGHT_MOVEMENT, KNIGHT_ATTACK_POWER, KNIGHT_ATTACK_RANGE,
                KNIGHT_GOLD_COST, KNIGHT_FOOD_COST, KNIGHT_UNIT_SPACE, owner);
    }

    @Override
    public Unit merge(Unit other) {
        return null;
    }

    @Override
    public boolean canMerge(Unit other) {
        return false;
    }

    @Override
    public boolean canAttack(Unit target) {
        return target != null && target.getOwner() != this.getOwner() && target.isAlive();
    }

    @Override
    public void dealDamage(int damage) {
        if (damage > 0) {
            setHitPoints(getHitPoints() - damage);
        }
    }
}