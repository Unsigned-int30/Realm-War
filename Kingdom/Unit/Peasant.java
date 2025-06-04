package Kingdom.Unit;

import Game.Player;

public class Peasant extends Unit {

    public static final int PEASANT_HP = 10;
    public static final int PEASANT_MOVEMENT = 1;
    public static final int PEASANT_ATTACK_POWER = 1;
    public static final int PEASANT_ATTACK_RANGE = 1;
    public static final int PEASANT_GOLD_COST = 1;
    public static final int PEASANT_FOOD_COST = 1;
    public static final int PEASANT_UNIT_SPACE = 1;

    public Peasant(Player owner) {
        super(PEASANT_HP, PEASANT_MOVEMENT, PEASANT_ATTACK_POWER, PEASANT_ATTACK_RANGE,
                PEASANT_GOLD_COST, PEASANT_FOOD_COST, PEASANT_UNIT_SPACE, owner);
    }

    @Override
    public Unit merge(Unit other) {
        if (canMerge(other)) {
            // دو Peasant یک Spearman می‌سازند
            return new Spearman(getOwner());
        }
        return null;
    }

    @Override
    public boolean canMerge(Unit other) {
        // یک Peasant می‌تواند با یک Peasant دیگر از همان بازیکن ادغام شود
        return other instanceof Peasant && this.getOwner() == other.getOwner();
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