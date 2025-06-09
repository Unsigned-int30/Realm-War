package Kingdom.Block;

import Utils.ResourceYield;
import Utils.ResourceType;

public class EmptyBlock extends Block {
    private static final int GOLD_YIELD_AMOUNT = 1;

    public EmptyBlock(int x, int y) {
        super(x, y);
    }

    @Override
    public ResourceYield produceResources() {
        if (getOwner() != null) {
            return new ResourceYield(ResourceType.GOLD, GOLD_YIELD_AMOUNT);
        } else {
            return new ResourceYield(ResourceType.NONE, 0);
        }
    }
}