package Utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerManager {
    private static final Logger logger = Logger.getLogger("RealmWarLogger");
    private static FileHandler fileHandler;

    public static void setup() {
        try {

            Logger rootLogger = Logger.getLogger("");
            if (rootLogger.getHandlers().length > 0) {
                rootLogger.removeHandler(rootLogger.getHandlers()[0]);
            }

            fileHandler = new FileHandler("realm-war.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);

            info("Logger initialized successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warning(message);
    }

    public static void severe(String message) {
        logger.severe(message);
    }
}