package Kingdom.Structure;

import Game.Player;

public class TownHall extends Structure {
    private static final int PROVIDED_UNIT_SPACE = 5;
    private static final int MAX_LEVEL_TOWNHALL = 1;
    private static final int MAINTENANCE_COST_TOWNHALL = 0;
    private static final int GOLD_PRODUCTION_TOWNHALL = 5;
    public static final int INITIAL_DURABILITY = 100;
    private static final int FOOD_PRODUCTION_TOWNHALL = 5;

    public TownHall(Player owner) {
        super(INITIAL_DURABILITY, owner, MAX_LEVEL_TOWNHALL, MAINTENANCE_COST_TOWNHALL, 0, 0);
    }

    @Override
    public void upgradeLevel() {
        // TownHall قابل ارتقا نیست
    }

    @Override
    public int getProvidedUnitSpace() {
        return PROVIDED_UNIT_SPACE;
    }

    @Override
    public int getGoldProductionPerTurn() {
        return GOLD_PRODUCTION_TOWNHALL;
    }

    @Override
    public int getFoodProductionPerTurn() {
        return FOOD_PRODUCTION_TOWNHALL;
    }
}