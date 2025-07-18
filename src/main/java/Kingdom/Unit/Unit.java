package Kingdom.Unit;

import Game.Player;
import Kingdom.Block.Block;

public abstract class Unit {
    private int hitPoints;
    private int ownerId = -1;
    private transient Player owner;
    private int movementBlockRange;
    private int attackPower;
    private int attackRange;
    private int paymentGold;
    private int rationFood;
    private int unitSpace;
    private transient Block block;
    private boolean hasAttackedThisTurn;

    public Unit(int hitPoints, int movementBlockRange, int attackPower, int attackRange, int paymentGold, int rationFood, int unitSpace, Player owner) {
        this.hitPoints = hitPoints;
        this.movementBlockRange = movementBlockRange;
        this.attackPower = attackPower;
        this.attackRange = attackRange;
        this.paymentGold = paymentGold;
        this.rationFood = rationFood;
        this.unitSpace = unitSpace;
        this.hasAttackedThisTurn = false;
        setOwner(owner);
    }
    public void setOwner(Player owner) { this.owner = owner; this.ownerId = (owner != null) ? owner.getPlayerId() : -1; }
    public int getHitPoints() { return hitPoints; }
    public void setHitPoints(int hp) { this.hitPoints = hp > 0 ? hp : 0; }
    public int getOwnerId() { return ownerId; }
    public Player getOwner() { return owner; }
    public Block getBlock() { return block; }
    public void setBlock(Block block) { this.block = block; }
    public boolean hasAttackedThisTurn() { return hasAttackedThisTurn; }
    public void setHasAttackedThisTurn(boolean hasAttacked) { this.hasAttackedThisTurn = hasAttacked; }
    public void resetTurnActions() { this.hasAttackedThisTurn = false; }
    public int getMovementBlockRange() { return movementBlockRange; }
    public int getAttackPower() { return attackPower; }
    public int getAttackRange() { return attackRange; }
    public int getPaymentGold() { return paymentGold; }
    public int getRationFood() { return rationFood; }
    public int getUnitSpace() { return unitSpace; }
    public void takeDamage(int damage) { if (damage > 0) setHitPoints(getHitPoints() - damage); }
    public boolean isAlive() { return this.hitPoints > 0; }
    public abstract int getRank();
    public abstract Unit merge(Unit other);
    public abstract boolean canMerge(Unit other);

    public abstract boolean canAttack(Unit target);

    public abstract void dealDamage(int damage);
    //  public abstract void heal(int amount);
}