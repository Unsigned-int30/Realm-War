package Kingdom.Structure;

import Game.Player;

public class Farm extends Structure { // StructureProperties شما خالی است

    private static final int BASE_FOOD_PRODUCTION = 5;
    private static final int MAX_LEVEL_FARM = 3;
    private static final int DURABILITY = 15;

    public Farm(int durability, Player player, int level, int maintenanceCost, int buildingCost, int levelUpCost) {
        super(durability, player, level, maintenanceCost, buildingCost, levelUpCost);
    }




    @Override
    public int getFoodProduction() {
        return getLevel() * BASE_FOOD_PRODUCTION;
    }

    @Override
    public void upgradeLevel() {

        if (getLevel() < MAX_LEVEL_FARM) {
            setLevel(getLevel() + 1);
            setDurability(getDurability() + DURABILITY);
            System.out.println("Farm upgraded to level " + getLevel() + ". New Durability: " + getDurability());
        } else {
            System.out.println("Farm is already at max level (" + MAX_LEVEL_FARM + ").");
        }
    }
}