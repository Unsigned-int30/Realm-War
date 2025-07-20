package com.realmwar.models.blocks;

import com.realmwar.models.Position;
import com.realmwar.models.structures.Structure;
import com.realmwar.models.units.Unit;

public abstract class Block {
    private Position position;
    private boolean isAbsorbed;
    private int kingdomId; // -1 if not absorbed
    private Structure structure;
    private Unit unit;

    public Block(Position position) {
        this.position = position;
        this.isAbsorbed = false;
        this.kingdomId = -1;
    }

    public abstract boolean canBuildStructure();
    public abstract int getResourceYield(String resourceType);
    
    // Getters and setters
    public Position getPosition() { return position; }
    public boolean isAbsorbed() { return isAbsorbed; }
    public int getKingdomId() { return kingdomId; }
    public void setAbsorbed(boolean absorbed, int kingdomId) {
        this.isAbsorbed = absorbed;
        this.kingdomId = kingdomId;
    }

    public boolean hasStructure() {
        return structure != null;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public boolean hasUnit() {
        return unit != null;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}