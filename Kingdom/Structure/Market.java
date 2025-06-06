package Kingdom.Structure;

import Game.Player;

public class Market extends Structure { // StructureProperties شما خالی است

    private static final int BASE_GOLD_PRODUCTION = 5;
    private static final int MAX_LEVEL_MARKET = 3;
    private static final int DURABILITY_INCREASE = 20;

    public Market(int durability, Player player, int level, int maintenanceCost, int buildingCost, int levelUpCost) {
        super(durability, player, level, maintenanceCost, buildingCost, levelUpCost);
    }

    @Override
    public int getGoldProductionTurn() {
        return getLevel() * BASE_GOLD_PRODUCTION;
    }

    @Override
    public void upgradeLevel() {
        if (getLevel() < MAX_LEVEL_MARKET) {
            setLevel(getLevel() + 1);
            setDurability(getDurability() + DURABILITY_INCREASE);
            System.out.println("Market upgraded to level " + getLevel() + ". New Durability: " + getDurability());

        } else {
           System.out.println("Market is already at max level (" + MAX_LEVEL_MARKET + ").");
        }
    }
}