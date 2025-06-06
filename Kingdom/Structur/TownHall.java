package Kingdom.Structure;

import Game.Player;

public class TownHall extends Structure {

    private static final int PROVIDED_UNIT_SPACE = 5;
    private static final int MAX_LEVEL_TOWNHALL = 1;
    private static final int MAINTENANCE_COST_TOWNHALL = 0;
    private static final int GOLD_PRODUCTION_TOWNHALL = 5;
    private static final int FOOD_PRODUCTION_TOWNHALL = 5;

    public TownHall(int durability, Player player, int buildingCost, int levelUpCost) {
        super(durability, player, MAX_LEVEL_TOWNHALL, MAINTENANCE_COST_TOWNHALL, buildingCost, levelUpCost);
    }

    @Override
    public void upgradeLevel() {
        System.out.println("TownHall cannot be upgraded.");
    }

    @Override
    public int getProvidedUnitSpace() {
        return PROVIDED_UNIT_SPACE;
    }

    @Override
    public int getGoldProductionTurn() {
        return GOLD_PRODUCTION_TOWNHALL;
    }

    @Override
    public int getFoodProduction() {
        return FOOD_PRODUCTION_TOWNHALL;
    }
}