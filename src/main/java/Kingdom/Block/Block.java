package Kingdom.Block;

import Game.Player;
import Utils.ResourceYield;
import Kingdom.Structure.Structure;
import Kingdom.Unit.Unit;

public abstract class Block {
    private final int x;
    private final int y;
    private Player owner;
    private Structure structure;
    private Unit unit;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        this.owner = null;
        this.structure = null;
        this.unit = null;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Player getOwner() { return owner; }
    public void setOwner(Player owner) { this.owner = owner; }
    public Structure getStructure() { return structure; }
    public void setStructure(Structure structure) { this.structure = structure; }
    public boolean hasStructure() { return this.structure != null; }
    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }
    public boolean hasUnit() { return this.unit != null; }

    public boolean canBuildStructure() { return !hasStructure(); }
    public boolean canMoveInto() { return !hasUnit(); }

    public abstract ResourceYield produceResources();
}