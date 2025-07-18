package View;

import Game.Player;
import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private JLabel currentPlayerLabel;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JTextArea winnersHistoryArea;
    private JLabel goldLabel;
    private JLabel foodLabel;
    private JLabel unitSpaceLabel;
    private JLabel turnTimerLabel;
    private JTextArea selectionInfoArea;

    public InfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(200, 0));


        player1ScoreLabel = new JLabel("Player 1 (Blue): 0");
        player2ScoreLabel = new JLabel("Player 2 (Red): 0");
        JPanel scoresPanel = new JPanel(new GridLayout(2, 1));
        scoresPanel.add(player1ScoreLabel);
        scoresPanel.add(player2ScoreLabel);
        winnersHistoryArea = new JTextArea(5, 20);
        winnersHistoryArea.setEditable(false);
        JScrollPane historyScroll = new JScrollPane(winnersHistoryArea);
        add(createTitledPanel("Scores", scoresPanel));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(createTitledPanel("Winners History", historyScroll));

        currentPlayerLabel = new JLabel("Player: -");
        goldLabel = new JLabel("Gold: -");
        foodLabel = new JLabel("Food: -");
        unitSpaceLabel = new JLabel("Unit Space: -/-");


        turnTimerLabel = new JLabel("Time Left: --");

        add(createTitledPanel("Current Player", currentPlayerLabel, goldLabel, foodLabel, unitSpaceLabel, turnTimerLabel));
        add(Box.createRigidArea(new Dimension(0, 10)));

        selectionInfoArea = new JTextArea("No selection.");
        selectionInfoArea.setEditable(false);
        selectionInfoArea.setLineWrap(true);
        selectionInfoArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(selectionInfoArea);
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

    public void updateScores(Player player1, Player player2) {
        if (player1 != null && player2 != null) {
            player1ScoreLabel.setText(player1.getPlayerName() + ": " + player1.getScore());
            player2ScoreLabel.setText(player2.getPlayerName() + ": " + player2.getScore());
        }
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
    public void updateTimer(int timeLeft) {
        if (turnTimerLabel != null) {
            turnTimerLabel.setText("Time Left: " + timeLeft);
        }
    }


}