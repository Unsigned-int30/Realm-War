package Kingdom.Block;

import Utils.ResourceYield;
import Utils.ResourceType;

public class ForestBlock extends Block {
    private static final int FOOD_YIELD_AMOUNT = 1;
    private boolean hasForest;

    public ForestBlock(int x, int y) {
        super(x, y);
        this.hasForest = true;
    }

    public boolean hasForest() {
        return hasForest;
    }

    public void removeForest() {
        this.hasForest = false;
    }

    @Override
    public ResourceYield produceResources() {
        if (getOwner() != null && this.hasForest) {
            return new ResourceYield(ResourceType.FOOD, FOOD_YIELD_AMOUNT);
        } else {
            return new ResourceYield(ResourceType.NONE, 0);
        }
    }
}