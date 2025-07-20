package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;

public class MainMenuPanel extends JPanel {
    private JButton newGameButton;
    private JButton exitButton;
    private Image backgroundImage;

    public MainMenuPanel() {
        try {
            InputStream stream = getClass().getResourceAsStream("/images/menu_background.png");
            if (stream != null) {
                backgroundImage = javax.imageio.ImageIO.read(stream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setPreferredSize(new Dimension(800, 600));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(20, 0, 20, 0);

        JLabel titleLabel = new JLabel("Realm War");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 72));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        newGameButton = createStyledButton("New Game");
        exitButton = createStyledButton("Exit");

        add(titleLabel, gbc);
        add(Box.createVerticalStrut(50));
        add(newGameButton, gbc);
        add(exitButton, gbc);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);


        Color normalColor = new Color(25, 25, 80, 200);
        Color hoverColor = new Color(50, 50, 150, 220);
        Color borderColor = new Color(255, 150, 206);

        button.setPreferredSize(new Dimension(250, 50));
        button.setFont(new Font("Serif", Font.BOLD, 22));
        button.setForeground(Color.WHITE);
        button.setBackground(normalColor);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}