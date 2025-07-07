package View;

import Game.Player;
import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private JLabel currentPlayerLabel;
    private JLabel goldLabel;
    private JLabel foodLabel;
    private JLabel unitSpaceLabel;
    private JTextArea selectionInfoArea;

    public InfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(200, 0));

        currentPlayerLabel = new JLabel("Player: -");
        goldLabel = new JLabel("Gold: -");
        foodLabel = new JLabel("Food: -");
        unitSpaceLabel = new JLabel("Unit Space: -/-");

        selectionInfoArea = new JTextArea("No selection.");
        selectionInfoArea.setEditable(false);
        selectionInfoArea.setLineWrap(true);
        selectionInfoArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(selectionInfoArea);

        add(createTitledPanel("Current Player", currentPlayerLabel, goldLabel, foodLabel, unitSpaceLabel));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(createTitledPanel("Selection Info", scrollPane));
    }

    private JPanel createTitledPanel(String title, Component... components) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        for (Component comp : components) {
            panel.add(comp);
        }
        return panel;
    }

    public void updatePlayerInfo(Player player) {
        if (player != null) {
            currentPlayerLabel.setText("Player: " + player.getPlayerName());
            goldLabel.setText("Gold: " + player.getGold());
            foodLabel.setText("Food: " + player.getFood());
            unitSpaceLabel.setText("Unit Space: " + player.getCurrentUnitSpaceUsed() + "/" + player.getMaxUnitSpace());
        }
    }

    public void updateSelectionInfo(String info) {
        selectionInfoArea.setText(info);
    }
}