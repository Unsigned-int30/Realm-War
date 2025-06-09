package Kingdom.Unit;

import Game.Player;

public class Swordman extends Unit {

    public static final int CREATION_GOLD_COST = 30;
    public static final int SWORDMAN_HP = 30;
    public static final int SWORDMAN_MOVEMENT = 2;
    public static final int SWORDMAN_ATTACK_POWER = 8;
    public static final int SWORDMAN_ATTACK_RANGE = 1;
    public static final int SWORDMAN_GOLD_COST = 3;
    public static final int SWORDMAN_FOOD_COST = 2;
    public static final int SWORDMAN_UNIT_SPACE = 2;

    public Swordman(Player owner) {
        super(SWORDMAN_HP, SWORDMAN_MOVEMENT, SWORDMAN_ATTACK_POWER, SWORDMAN_ATTACK_RANGE, SWORDMAN_GOLD_COST, SWORDMAN_FOOD_COST, SWORDMAN_UNIT_SPACE, owner);
    }

    @Override
    public Unit merge(Unit other) {
        if (canMerge(other)) {
            // دو Swordman یک Knight می‌سازند
            return new Knight(getOwner());
        }
        return null;
    }

    @Override
    public boolean canMerge(Unit other) {
        return other instanceof Swordman && this.getOwner() == other.getOwner();
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