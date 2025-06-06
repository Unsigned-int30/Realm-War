package Game;

import Kingdom.Block.Block;
import Kingdom.Structure.Structure;
import Kingdom.Structure.TownHall;
import Kingdom.Unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {

    private final String playerName;
    private final int playerId;
    private int gold;
    private int food;

    private TownHall townHall;
    private final List<Unit> units;
    private final List<Structure> structures;
    private final List<Block> ownedBlocks;

    private static final int INITIAL_GOLD = 50;
    private static final int INITIAL_FOOD = 20;

    public Player(int playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.gold = INITIAL_GOLD;
        this.food = INITIAL_FOOD;
        this.units = new ArrayList<>();
        this.structures = new ArrayList<>();
        this.ownedBlocks = new ArrayList<>();
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getGold() {
        return gold;
    }

    public int getFood() {
        return food;
    }

    public TownHall getTownHall() {
        return townHall;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public List<Structure> getStructures() {
        return structures;
    }

    public List<Block> getOwnedBlocks() {
        return ownedBlocks;
    }

    public void firstPosition(TownHall townHall, Block startingBlock) {
        if (townHall == null || startingBlock == null) {
            System.err.println("Error: TownHall or Starting Block cannot be null for player initialization.");
            return;
        }
        this.townHall = townHall;
        if (!this.structures.contains(townHall)) {
            this.structures.add(townHall);
        }

        if (!this.ownedBlocks.contains(startingBlock)) {
            this.ownedBlocks.add(startingBlock);
        }
        startingBlock.setBlockOwner(this);
    }

    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
        }
    }

    public boolean spendGold(int amount) {
        if (amount <= 0) return false;
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }

    public void addFood(int amount) {
        if (amount > 0) {
            this.food += amount;
        }
    }

    public boolean spendFood(int amount) {
        if (amount <= 0) return false;
        if (this.food >= amount) {
            this.food -= amount;
            return true;
        }
        return false;
    }

    public boolean canAfford(int goldCost, int foodCost) {
        return this.gold >= goldCost && this.food >= foodCost;
    }

    public boolean payCost(int goldCost, int foodCost) {
        if (canAfford(goldCost, foodCost)) {
            this.gold -= goldCost;
            this.food -= foodCost;
            return true;
        }
        return false;
    }

    public void addOwnedBlock(Block block) {
        if (block != null && !this.ownedBlocks.contains(block)) {
            this.ownedBlocks.add(block);
            block.setBlockOwner(this);
        }
    }

    public void removeOwnedBlock(Block block) {
        if (block != null && this.ownedBlocks.remove(block)) {
            if (block.getBlockOwner() == this) {
                block.setBlockOwner(null);
            }
        }
    }

    public void addStructure(Structure structure) {
        if (structure != null && !this.structures.contains(structure)) {
            this.structures.add(structure);
        }
    }

    public void removeStructure(Structure structure) {
        this.structures.remove(structure);
    }

    public int getMaxUnitSpace() {
        int maxSpace = 0;
        for (Structure structure : this.structures) {
            if (structure != null) {
                maxSpace += structure.getProvidedUnitSpace();
            }
        }
        return maxSpace;
    }

    public int getCurrentUnitSpaceUsed() {
        int usedSpace = 0;
        for (Unit unit : this.units) {
            if (unit != null) {
                usedSpace += unit.getUnitSpace();
            }
        }
        return usedSpace;
    }

    public boolean canPlaceUnit(Unit unitToAdd) {
        if (unitToAdd == null) return false;
        return (getMaxUnitSpace() - getCurrentUnitSpaceUsed()) >= unitToAdd.getUnitSpace();
    }

    public void addUnit(Unit unit) {
        if (unit != null && !this.units.contains(unit)) {
            if (canPlaceUnit(unit)) {
                this.units.add(unit);
                // unit.setOwner(this); // یا unit.setPlayerOwner(this) پس از اصلاح در کلاس Unit
                System.out.println(unit.getClass().getSimpleName() + " (Space: " + unit.getUnitSpace() + ") added to " + playerName + "'s army. " +
                        "Current space: " + getCurrentUnitSpaceUsed() + "/" + getMaxUnitSpace());
            } else {
                System.out.println(playerName + " does not have enough unit space for " + unit.getClass().getSimpleName() +
                        " (Needs: " + unit.getUnitSpace() + ", Available: " + (getMaxUnitSpace() - getCurrentUnitSpaceUsed()) + ").");
            }
        }
    }

    public void removeUnit(Unit unit) {
        this.units.remove(unit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerId == player.playerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }

    @Override
    public String toString() {
        return "Player " + playerId + ": " + playerName + " (Gold: " + gold + ", Food: " + food +
                ", Units: " + units.size() + " [" + getCurrentUnitSpaceUsed() + "/" + getMaxUnitSpace() + " space])";
    }
}