package Kingdom.Structure;
import Game.Player;

public class Barrack extends Structure  {

    private static final int UNIT_SPACE_PER_LEVEL = 5;
    private static final int MAX_LEVEL_BARRACK = 3;

    public Barrack(int durability, Player player, int level, int maintenanceCost, int buildingCost, int levelUpCost) {
        super(durability, player, level, maintenanceCost, buildingCost, levelUpCost);
    }

    @Override
    public int getProvidedUnitSpace() {
        return getLevel() * UNIT_SPACE_PER_LEVEL;
    }

    @Override
    public void upgradeLevel() {
        if (getLevel() < MAX_LEVEL_BARRACK) {

            setLevel(getLevel() + 1);
            System.out.println("Barrack has been Successful Upgraded to Level "+ getLevel());
        } else {
            System.out.println("Barrack is already at max level (" + MAX_LEVEL_BARRACK + ").");
        }
    }

}