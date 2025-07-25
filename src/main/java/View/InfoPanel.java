package View;

import Game.Player;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class InfoPanel extends JPanel {
    private JLabel currentPlayerLabel, goldLabel, foodLabel, unitSpaceLabel, turnTimerLabel, turnCountLabel;
    private JLabel player1ScoreLabel, player2ScoreLabel;
    private JTextArea winnersHistoryArea, selectionInfoArea;

    public InfoPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setPreferredSize(new Dimension(240, 0));

        JPanel contentPanel = new JPanel(); // یک پنل داخلی برای نگهداری محتوا
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // برای چیدمان عمودی و زیر هم

        player1ScoreLabel = new JLabel("Player 1 (Blue): 0");
        player2ScoreLabel = new JLabel("Player 2 (Red): 0");
        JPanel scoresPanel = new JPanel(new GridLayout(2, 1));
        scoresPanel.add(player1ScoreLabel);
        scoresPanel.add(player2ScoreLabel);

        winnersHistoryArea = new JTextArea(5, 15);
        winnersHistoryArea.setEditable(false);
        JScrollPane historyScroll = new JScrollPane(winnersHistoryArea);
        historyScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        currentPlayerLabel = new JLabel("Player: -");
        currentPlayerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        goldLabel = new JLabel("Gold: 0 (+0)");
        foodLabel = new JLabel("Food: 0 (+0)");
        unitSpaceLabel = new JLabel("Unit Space: 0/0");
        turnTimerLabel = new JLabel("Time Left: --");
        turnTimerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        turnTimerLabel.setForeground(Color.RED);
        turnCountLabel = new JLabel("Turn: 0 / 0");

        selectionInfoArea = new JTextArea("No selection.");
        selectionInfoArea.setEditable(false);
        selectionInfoArea.setLineWrap(true); // شکستن خطوط طولانی
        selectionInfoArea.setWrapStyleWord(true);
        JScrollPane selectionScroll = new JScrollPane(selectionInfoArea);

        contentPanel.add(createTitledPanel("Scores", scoresPanel));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5))); // ایجاد یک فاصله عمودی
        contentPanel.add(createTitledPanel("Winners History", historyScroll));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(createTitledPanel("Current Player", currentPlayerLabel, goldLabel, foodLabel, unitSpaceLabel, turnTimerLabel, turnCountLabel));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(createTitledPanel("Selection Info", selectionScroll));

        this.add(contentPanel, BorderLayout.NORTH);
    }

    private JPanel createTitledPanel(String title, Component... components) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Border lineBorder = BorderFactory.createLineBorder(new Color(178, 47, 221), 1);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lineBorder, " " + title + " ");
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 12));
        titledBorder.setTitleColor(Color.DARK_GRAY);
        panel.setBorder(titledBorder);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        for (Component comp : components) {
            ((JComponent) comp).setAlignmentX(Component.LEFT_ALIGNMENT);
            Box horizontalBox = Box.createHorizontalBox();
            horizontalBox.add(Box.createRigidArea(new Dimension(5, 0)));
            horizontalBox.add(comp);
            horizontalBox.add(Box.createHorizontalGlue());
            panel.add(horizontalBox);
        }
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        return panel;
    }

    private Color getPlayerColor(int playerId) {
        if (playerId == 0) return Color.BLUE;
        if (playerId == 1) return Color.RED;
        return Color.BLACK;
    }

    public void updatePlayerInfo(Player player) {
        if (player != null) {
            currentPlayerLabel.setText("Player: " + player.getPlayerName());
            currentPlayerLabel.setForeground(getPlayerColor(player.getPlayerId()));
            int netGold = player.getGoldIncomePerTurn() - player.getGoldExpensePerTurn();
            int netFood = player.getFoodIncomePerTurn() - player.getFoodExpensePerTurn();
            String goldSign = netGold >= 0 ? "+" : "";
            String foodSign = netFood >= 0 ? "+" : "";
            goldLabel.setText(String.format("Gold: %d (%s%d)", player.getGold(), goldSign, netGold));
            foodLabel.setText(String.format("Food: %d (%s%d)", player.getFood(), foodSign, netFood));
            unitSpaceLabel.setText("Unit Space: " + player.getCurrentUnitSpaceUsed() + "/" + player.getMaxUnitSpace());
        } else {
            currentPlayerLabel.setText("GAME OVER");
            currentPlayerLabel.setForeground(Color.BLACK);
        }
    }

    public void updateScores(Player p1, Player p2) {
        if (p1 != null) player1ScoreLabel.setText(p1.getPlayerName() + ": " + p1.getScore());
        if (p2 != null) player2ScoreLabel.setText(p2.getPlayerName() + ": " + p2.getScore());
    }

    public void updateWinnersHistory(String history) { winnersHistoryArea.setText(history); }
    public void updateTimer(int timeLeft) { if (turnTimerLabel != null) turnTimerLabel.setText("Time Left: " + timeLeft); }
    public void updateTurnInfo(int current, int max) { if (turnCountLabel != null) turnCountLabel.setText("Turn: " + current + " / " + max); }
    public void updateSelectionInfo(String info) { selectionInfoArea.setText(info); }
}