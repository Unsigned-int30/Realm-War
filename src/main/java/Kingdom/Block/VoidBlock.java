package Kingdom.Block;

import Utils.ResourceYield;
import Utils.ResourceType;

public class VoidBlock extends Block {
    public VoidBlock(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean canBuildStructure() {
        return false;
    }

    @Override
    public boolean canMoveInto() {
        return false;
    }

    @Override
    public ResourceYield produceResources() {
        return new ResourceYield(ResourceType.NONE, 0);
    }
}