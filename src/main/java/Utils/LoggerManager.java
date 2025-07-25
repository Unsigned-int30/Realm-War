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

            Logger rootLogger = Logger.getLogger(""); // برای جلوگیری از نوشتن لاگ در کنسول استفاده میشه
            if (rootLogger.getHandlers().length > 0) {
                rootLogger.removeHandler(rootLogger.getHandlers()[0]);
            }

            fileHandler = new FileHandler("realm-war.log", false); // ایجاد فایل لوگر
            SimpleFormatter formatter = new SimpleFormatter(); // یه فرمت برای قالب بندی و زیبایی پیام های لاگه
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);

            info("Logger initialized successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // برای ثبت اطلاعات عمومی و وقایع کم اهمیت از اینفو استفاده میشه مثل شروع نوبت
    public static void info(String message) {
        logger.info(message);
    }
    // نیاز به توجه دارن مثل کمبود غذا و آسیب دیدن نیرو ها
    public static void warning(String message) {
        logger.warning(message);
    }
    // برای مشکلات و ارور های برنامه مثل وصل نشدن به دیتابیس یا سیو نشدن
    public static void severe(String message) {
        logger.severe(message);
    }
}