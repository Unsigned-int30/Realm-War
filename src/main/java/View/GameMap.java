package View;

import Game.Player;
import Kingdom.Block.Block;
import Kingdom.Block.EmptyBlock;
import Kingdom.Block.ForestBlock;
import Kingdom.Block.VoidBlock;
import Kingdom.Structure.Structure;
import Kingdom.Unit.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameMap {
    private final int width;
    private final int height;
    private final Block[][] mapGrid;
    private static final int DEFAULT_WIDTH = 15;
    private static final int DEFAULT_HEIGHT = 15;

    public GameMap() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        initializeDefaultMap();
    }

    public GameMap(int width, int height) {
        this.width = width > 0 ? width : DEFAULT_WIDTH;
        this.height = height > 0 ? height : DEFAULT_HEIGHT;
        this.mapGrid = new Block[this.height][this.width];
    }

    private void initializeDefaultMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if ((x == 0 || x == width - 1) || (y == 0 || y == height - 1)) {
                    mapGrid[y][x] = new VoidBlock(x, y);
                } else if (x % 3 == 0 && y % 2 == 0) {
                    mapGrid[y][x] = new ForestBlock(x, y);
                } else {
                    mapGrid[y][x] = new EmptyBlock(x, y);
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Block getBlockAt(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return mapGrid[y][x];
        }
        return null;
    }

    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public List<Block> getAdjacentBlocks(Block block) {
        if (block == null) return Collections.emptyList();
        List<Block> neighbors = new ArrayList<>();
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        for (int i = 0; i < dx.length; i++) {
            int newX = block.getX() + dx[i];
            int newY = block.getY() + dy[i];
            if (isValidCoordinate(newX, newY)) {
                neighbors.add(getBlockAt(newX, newY));
            }
        }
        return neighbors;
    }

    public int getDistance(Block b1, Block b2) {
        if (b1 == null || b2 == null) return Integer.MAX_VALUE;
        return Math.abs(b1.getX() - b2.getX()) + Math.abs(b1.getY() - b2.getY());
    }

    public void relinkAll(List<Player> players) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Block block = getBlockAt(x, y);
                if (block == null) continue;

                Player blockOwner = block.getOwner();

                if (block.getStructure() != null) {
                    Structure s = block.getStructure();
                    s.setBlock(block);
                    s.setOwner(blockOwner);
                }


                if (block.getUnit() != null) {
                    Unit u = block.getUnit();
                    u.setBlock(block);
                    u.setOwner(blockOwner);
                }
            }
        }
    }

}

