package Kingdom.Unit;

import Game.Player;

public class Spearman extends Unit {

    public static final int SPEARMAN_HP = 20;
    public static final int SPEARMAN_MOVEMENT = 1; // یا 2
    public static final int SPEARMAN_ATTACK_POWER = 5;
    public static final int SPEARMAN_ATTACK_RANGE = 1; // یا 2 اگر نیزه بلند است
    public static final int SPEARMAN_GOLD_COST = 2;
    public static final int SPEARMAN_FOOD_COST = 1;
    public static final int SPEARMAN_UNIT_SPACE = 1;

    public Spearman(Player owner) {
        super(SPEARMAN_HP, SPEARMAN_MOVEMENT, SPEARMAN_ATTACK_POWER, SPEARMAN_ATTACK_RANGE,
                SPEARMAN_GOLD_COST, SPEARMAN_FOOD_COST, SPEARMAN_UNIT_SPACE, owner);
    }

    @Override
    public Unit merge(Unit other) {
        if (canMerge(other)) {
            // دو Spearman یک Swordman می‌سازند
            return new Swordman(getOwner());
        }
        return null;
    }

    @Override
    public boolean canMerge(Unit other) {
        return other instanceof Spearman && this.getOwner() == other.getOwner();
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