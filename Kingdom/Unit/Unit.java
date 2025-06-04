package Kingdom.Unit;

import Game.Player;

public abstract class Unit {

    private int hitPoints;
    private int movementBlockRange;
    private int attackPower;
    private int attackRange;
    private int paymentGold;
    private int rationFood;
    private int unitSpace;
    private Player owner;

    public Unit(int hitPoints, int movementBlockRange, int attackPower, int attackRange,
                int paymentGold, int rationFood, int unitSpace, Player owner) {
        this.hitPoints = hitPoints;
        this.movementBlockRange = movementBlockRange;
        this.attackPower = attackPower;
        this.attackRange = attackRange;
        this.paymentGold = paymentGold;
        this.rationFood = rationFood;
        this.unitSpace = unitSpace;
        this.owner = owner;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getMovementBlockRange() {
        return movementBlockRange;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getPaymentGold() {
        return paymentGold;
    }

    public int getRationFood() {
        return rationFood;
    }

    public int getUnitSpace() {
        return unitSpace;
    }

    public Player getOwner() {
        return owner;
    }


    public void setHitPoints(int hitPoints) {
        if (hitPoints < 0) {
            this.hitPoints = 0;
        } else {
            this.hitPoints = hitPoints;
        }
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public abstract Unit merge(Unit other);

    public abstract boolean canMerge(Unit other);

    public abstract boolean canAttack(Unit target);

    public abstract void dealDamage(int damage);
   // public abstract void heal(int amount);
   // public abstract void removeUnit(Unit remove)

    public boolean isAlive() {
        return this.hitPoints > 0;
    }
}