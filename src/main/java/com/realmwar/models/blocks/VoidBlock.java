package com.realmwar.models.blocks;

import com.realmwar.models.Position;

public class VoidBlock extends Block {
    public VoidBlock(Position position) {
        super(position);
    }

    @Override
    public boolean canBuildStructure() {
        return false;
    }

    @Override
    public int getResourceYield(String resourceType) {
        return 0;
    }
}