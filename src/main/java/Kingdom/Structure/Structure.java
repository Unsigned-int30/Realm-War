package Kingdom.Structure;

import Game.Player;
import Kingdom.Block.Block;

public abstract class Structure {
    private int durability;
    private int ownerId = -1;
    private transient Player owner;
    private int level;
    private int maintenanceCost;
    private int buildingCost;
    private int levelUpCost;
    private transient Block block;

    public Structure(int durability, Player player, int level, int maintenanceCost, int buildingCost, int levelUpCost) {
        this.durability = durability;
        this.level = level;
        this.maintenanceCost = maintenanceCost;
        this.buildingCost = buildingCost;
        this.levelUpCost = levelUpCost;
        setOwner(player);
    }
    public void setOwner(Player owner) {
        this.owner = owner;
        if (owner != null) {
            this.ownerId = owner.getPlayerId();
        } else {
            this.ownerId = -1;
        }
    }
    public int getDurability() { return durability; }
    public void setDurability(int d) { this.durability = d > 0 ? d : 0; } // برای منفی نشدن جان ساختمان
    public int getOwnerId() { return ownerId; }
    public Player getOwner() { return owner; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getMaintenanceCost() { return maintenanceCost; }
    public int getBuildingCost() { return buildingCost; }
    public int getLevelUpCost() { return levelUpCost; }
    public void setLevelUpCost(int c) { this.levelUpCost = c; }
    public Block getBlock() { return block; }
    public void setBlock(Block block) { this.block = block; }
    public boolean isDestroyed() { return this.durability <= 0; }
    public void takeDamage(int amount) { if (amount > 0) setDurability(this.durability - amount); }
    public abstract void upgradeLevel();
    public int getProvidedUnitSpace() { return 0; }
    public int getGoldProductionPerTurn() { return 0; }
    public int getFoodProductionPerTurn() { return 0; }
}