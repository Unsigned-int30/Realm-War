package Kingdom.Block;

import Game.Player;
import Utils.ResourceYield;
import Kingdom.Structure.Structure;
import Kingdom.Unit.Unit;

public abstract class Block {
    private final int x;
    private final int y;
    private int ownerId = -1;
    private transient Player owner;
    private Structure structure;
    private Unit unit;

    public Block(int x, int y) { this.x = x; this.y = y; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Player getOwner() { return owner; }
    public int getOwnerId() { return ownerId; }
    public Structure getStructure() { return structure; }
    public Unit getUnit() { return unit; }
    public boolean hasStructure() { return this.structure != null; }
    public boolean hasUnit() { return this.unit != null; }
    public void setStructure(Structure structure) { this.structure = structure; }
    public void setUnit(Unit unit) { this.unit = unit; }
    public void setOwner(Player owner) {
        this.owner = owner;
        this.ownerId = (owner != null) ? owner.getPlayerId() : -1;
    }
    public boolean canBuildStructure() { return !hasStructure(); }
    public boolean canMoveInto() { return !hasUnit(); }
    public abstract ResourceYield produceResources();
}