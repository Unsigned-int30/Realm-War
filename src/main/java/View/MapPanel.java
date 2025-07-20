package View;

import View.GameMap;
import Game.Player;
import Kingdom.Block.Block;
import Kingdom.Block.EmptyBlock;
import Kingdom.Block.ForestBlock;
import Kingdom.Block.VoidBlock;
import Kingdom.Structure.Structure;
import Kingdom.Unit.Unit;

import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;


public class MapPanel extends JPanel {
    private GameMap gameMap;
    private Block selectedBlock;
    private static final int CELL_SIZE = 45;
    private static final int DEFAULT_PANEL_SIZE = 700;

    public MapPanel(GameMap gameMap) {
        this.selectedBlock = null;
        setGameMap(gameMap);
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
        if (gameMap != null) {
            int preferredWidth = gameMap.getWidth() * CELL_SIZE;
            int preferredHeight = gameMap.getHeight() * CELL_SIZE;
            setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        } else {
            setPreferredSize(new Dimension(DEFAULT_PANEL_SIZE, DEFAULT_PANEL_SIZE));
        }
        revalidate();
        repaint();
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
        if (gameMap == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("Loading Map...", 20, 20);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                Block currentBlock = gameMap.getBlockAt(x, y);
                if (currentBlock == null) continue;

                if (currentBlock.getOwner() != null) {
                    g2d.setColor(getPlayerTerritoryColor(currentBlock.getOwner().getPlayerId()));
                    g2d.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }

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
        if (block instanceof EmptyBlock) return new Color(211, 211, 211, 150);
        if (block instanceof ForestBlock) {
            return ((ForestBlock) block).hasForest() ? new Color(34, 139, 34, 150) : new Color(139, 69, 19, 150);
        }
        if (block instanceof VoidBlock) return Color.BLACK;
        return Color.WHITE;
    }

    private Color getPlayerTerritoryColor(int playerId) {
        if (playerId == 0) return new Color(173, 216, 230);
        if (playerId == 1) return new Color(255, 182, 193);
        return Color.WHITE;
    }

    private void drawStructure(Graphics2D g2d, Structure structure, int x, int y) {
        String assetName = structure.getClass().getSimpleName().toLowerCase() + ".png";
        BufferedImage img = AssetManager.getAsset(assetName);
        if (img != null) {
            g2d.drawImage(img, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(x * CELL_SIZE + 10, y * CELL_SIZE + 10, CELL_SIZE - 20, CELL_SIZE - 20);
        }

        if (structure.getOwner() != null) {
            g2d.setColor(getPlayerColor(structure.getOwner().getPlayerId()));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x * CELL_SIZE + 2, y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            g2d.setStroke(new BasicStroke(1));
        }
    }

    private void drawUnit(Graphics2D g2d, Unit unit, int x, int y) {
        String assetName = unit.getClass().getSimpleName().toLowerCase() + ".png";
        BufferedImage img = AssetManager.getAsset(assetName);
        if (img != null) {
            g2d.drawImage(img, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
        } else {
            if (unit.getOwner() != null) {
                g2d.setColor(getPlayerColor(unit.getOwner().getPlayerId()));
                g2d.fillOval(x * CELL_SIZE + 15, y * CELL_SIZE + 15, CELL_SIZE - 30, CELL_SIZE - 30);
            }
        }
    }

    private Color getPlayerColor(int playerId) {
        if (playerId == 0) return Color.BLUE;
        if (playerId == 1) return Color.RED;
        return Color.WHITE;
    }

    public void refreshView() {
        repaint();
    }
}