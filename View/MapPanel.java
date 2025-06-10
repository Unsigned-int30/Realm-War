package View;

import View.GameMap;
import Kingdom.Block.Block;
import Kingdom.Block.EmptyBlock;
import Kingdom.Block.ForestBlock;
import Kingdom.Block.VoidBlock;
import Kingdom.Structure.Structure;
import Kingdom.Unit.Unit;

import javax.swing.JPanel;
import java.awt.*;

public class MapPanel extends JPanel {
    private GameMap gameMap;
    private Block selectedBlock;
    private static final int CELL_SIZE = 40;

    public MapPanel(GameMap gameMap) {
        this.gameMap = gameMap;
        this.selectedBlock = null;
        int preferredWidth = gameMap.getWidth() * CELL_SIZE;
        int preferredHeight = gameMap.getHeight() * CELL_SIZE;
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));
    }

    public void setSelectedBlock(Block block) {
        this.selectedBlock = block;
        repaint();
    }

    public int getCellSize() {
        return CELL_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameMap == null) return;
        Graphics2D g2d = (Graphics2D) g;

        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                Block currentBlock = gameMap.getBlockAt(x, y);
                if (currentBlock == null) continue;

                Color blockColor = getBlockColor(currentBlock);
                g2d.setColor(blockColor);
                g2d.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                if (currentBlock.hasStructure()) {
                    drawStructure(g2d, currentBlock.getStructure(), x, y);
                }
                if (currentBlock.hasUnit()) {
                    drawUnit(g2d, currentBlock.getUnit(), x, y);
                }

                g2d.setColor(Color.DARK_GRAY);
                g2d.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                if (currentBlock == selectedBlock) {
                    g2d.setColor(Color.YELLOW);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 3, CELL_SIZE - 3);
                    g2d.setStroke(new BasicStroke(1));
                }
            }
        }
    }

    private Color getBlockColor(Block block) {
        if (block instanceof EmptyBlock) return Color.LIGHT_GRAY;
        if (block instanceof ForestBlock) {
            return ((ForestBlock) block).hasForest() ? new Color(34, 139, 34) : new Color(139, 69, 19);
        }
        if (block instanceof VoidBlock) return Color.BLACK;
        return Color.WHITE;
    }

    private void drawStructure(Graphics2D g2d, Structure structure, int x, int y) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x * CELL_SIZE + 10, y * CELL_SIZE + 10, CELL_SIZE - 20, CELL_SIZE - 20);
        if (structure.getOwner() != null) {
            g2d.setColor(getPlayerColor(structure.getOwner().getPlayerId()));
            g2d.drawRect(x * CELL_SIZE + 10, y * CELL_SIZE + 10, CELL_SIZE - 20, CELL_SIZE - 20);
        }
    }

    private void drawUnit(Graphics2D g2d, Unit unit, int x, int y) {
        if (unit.getOwner() != null) {
            g2d.setColor(getPlayerColor(unit.getOwner().getPlayerId()));
            g2d.fillOval(x * CELL_SIZE + 15, y * CELL_SIZE + 15, CELL_SIZE - 30, CELL_SIZE - 30);
        }
    }

    private Color getPlayerColor(int playerId) {
        if (playerId == 0) return Color.BLUE;
        if (playerId == 1) return Color.RED;
        if (playerId == 2) return Color.YELLOW;
        if (playerId == 3) return Color.GREEN;
        return Color.WHITE;
    }

    public void refreshView() {
        repaint();
    }
}