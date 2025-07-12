package View;

import Game.Player;
import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private JLabel currentPlayerLabel;
    private JLabel goldLabel;
    private JLabel foodLabel;
    private JLabel unitSpaceLabel;
    private JLabel turnTimerLabel;
    private JTextArea selectionInfoArea;

    public InfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(200, 0));

        currentPlayerLabel = new JLabel("Player: -");
        goldLabel = new JLabel("Gold: -");
        foodLabel = new JLabel("Food: -");
        unitSpaceLabel = new JLabel("Unit Space: -/-");

        turnTimerLabel = new JLabel("Time Left: -");
        turnTimerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        turnTimerLabel.setForeground(Color.RED);

        selectionInfoArea = new JTextArea("No selection.");
        selectionInfoArea.setEditable(false);
        selectionInfoArea.setLineWrap(true);
        selectionInfoArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(selectionInfoArea);
        scrollPane.setPreferredSize(new Dimension(180, 200));

        add(createTitledPanel("Current Player", currentPlayerLabel, goldLabel, foodLabel, unitSpaceLabel, turnTimerLabel));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(createTitledPanel("Selection Info", scrollPane));
    }

    private JPanel createTitledPanel(String title, Component... components) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (Component comp : components) {
            ((JComponent) comp).setAlignmentX(Component.LEFT_ALIGNMENT);
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
        } else {
            currentPlayerLabel.setText("Player: -");
            goldLabel.setText("Gold: -");
            foodLabel.setText("Food: -");
            unitSpaceLabel.setText("Unit Space: -/-");
        }
    }

    public void updateTimer(int timeLeft) {
        turnTimerLabel.setText("Time Left: " + timeLeft);
    }

    public void updateSelectionInfo(String info) {
        selectionInfoArea.setText(info);
    }
}