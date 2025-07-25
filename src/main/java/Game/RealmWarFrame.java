package Game;

import View.MainMenuPanel;
import View.MainPanel;
import javax.swing.*;
import java.awt.*;

public class RealmWarFrame {
    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // برای جلوگیری از قفل کردن صفحه بازی
            frame = new JFrame("Realm War");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            showMainMenu();

            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }

    private static void showMainMenu() {
        MainMenuPanel mainMenu = new MainMenuPanel();
        mainMenu.getNewGameButton().addActionListener(e -> startGame());
        mainMenu.getExitButton().addActionListener(e -> System.exit(0));

        frame.getContentPane().removeAll();
        frame.getContentPane().add(mainMenu);
        frame.pack(); // اندازه پنجره رو متناسب با محتوای داخلش اوکی میکنه
        frame.revalidate(); // این دو تا هم برای اطمینان از نمایش درست پنجره اصلی استفاده شدن
        frame.repaint();
    }

    private static void startGame() {
        MainPanel gamePanel = new MainPanel(frame);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(gamePanel);
        frame.revalidate();
        frame.repaint();
    }
}