package Game;

import Game.DataBase.dataBaseInitializer;
import View.MainPanel;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        dataBaseInitializer.init();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainPanel();
            }
        });
    }
}

