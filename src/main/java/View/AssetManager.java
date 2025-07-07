package View;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    private static Map<String, BufferedImage> assets = new HashMap<>();

    public static void loadAssets() {
        loadAsset("farm.png");
        loadAsset("market.png");
        loadAsset("barrack.png");
        loadAsset("tower.png");
        loadAsset("townhall.png");
        loadAsset("peasant.png");
        loadAsset("spearman.png");
        loadAsset("swordman.png");
        loadAsset("knight.png");
    }

    private static void loadAsset(String fileName) {
        try {
            InputStream stream = AssetManager.class.getResourceAsStream("/images/" + fileName);
            if (stream == null) {
                System.err.println("Could not find asset: /images/" + fileName);
                return;
            }
            assets.put(fileName, ImageIO.read(stream));
            System.out.println("Loaded asset: " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to load asset: " + fileName);
            e.printStackTrace();
        }
    }

    public static BufferedImage getAsset(String fileName) {
        return assets.get(fileName);
    }
}
