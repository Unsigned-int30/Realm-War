package Kingdom.Structure;

import Game.Player;

public class Tower extends Structure {

    private static final int MAX_LEVEL_TOWER = 3;
    private static final int DURABILITY_INCREASE_PER_LEVEL = 30;

    public Tower(int durability, Player player, int level, int maintenanceCost, int buildingCost, int levelUpCost) {
        super(durability, player, level, maintenanceCost, buildingCost, levelUpCost);
    }

    // private int calculateInitialAttackPower(int level) {
    //     return level * 10;
    // }

    public int getRestrictionLevel() {
        return getLevel();
    }

    @Override
    public void upgradeLevel() {
        if (getLevel() < MAX_LEVEL_TOWER) {
            setLevel(getLevel() + 1);
            setDurability(getDurability() + DURABILITY_INCREASE_PER_LEVEL);


            // setLevelUpCost(getLevelUpCost() + 10)
            // this.attackPower = calculateInitialAttackPower(getLevel());

          System.out.println("Tower upgraded to level " + getLevel());
        } else {
            System.out.println("Tower is already at max level (" + MAX_LEVEL_TOWER + ").");
        }
    }
}