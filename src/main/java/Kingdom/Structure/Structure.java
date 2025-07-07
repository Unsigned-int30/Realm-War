package Kingdom.Structure;

import Game.Player;
import Kingdom.Block.Block;

public abstract class Structure {
    private int durability;
    private Player owner;
    private int level;
    private int maintenanceCost;
    private int buildingCost;
    private int levelUpCost;
    private Block block;

    public Structure(int durability, Player player, int level, int maintenanceCost, int buildingCost, int levelUpCost) {
        this.durability = durability;
        this.owner = player;
        this.level = level;
        this.maintenanceCost = maintenanceCost;
        this.buildingCost = buildingCost;
        this.levelUpCost = levelUpCost;
        this.block = null;
    }

    public int getDurability() { return durability; }
    public void setDurability(int durability) { this.durability = durability > 0 ? durability : 0; }
    public Player getOwner() { return owner; }
    public void setOwner(Player owner) { this.owner = owner; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getMaintenanceCost() { return maintenanceCost; }
    public void setMaintenanceCost(int maintenanceCost) { this.maintenanceCost = maintenanceCost; }
    public int getBuildingCost() { return buildingCost; }
    public void setBuildingCost(int buildingCost) { this.buildingCost = buildingCost; }
    public int getLevelUpCost() { return levelUpCost; }
    public void setLevelUpCost(int levelUpCost) { this.levelUpCost = levelUpCost; }
    public Block getBlock() { return block; }
    public void setBlock(Block block) { this.block = block; }
    public boolean isDestroyed() { return this.durability <= 0; }
    public void takeDamage(int amount) { if (amount > 0) { setDurability(this.durability - amount); } }
    public abstract void upgradeLevel();
    public int getProvidedUnitSpace() { return 0; }
    public int getGoldProductionPerTurn() { return 0; }
    public int getFoodProductionPerTurn() { return 0; }
}