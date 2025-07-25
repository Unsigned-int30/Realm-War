package Game.DataBase;

import java.io.InputStream;
import java.util.Properties;

public class dataBaseConfig {
    // یک کلاس داخلی که وظیفه خوندن و نگداری اطلاعات رو داره
    public class DatabaseConfig {
        private static final Properties props = new Properties(); // فقط یک بار خونده میشه

        static {
            try (InputStream input = DatabaseConfig.class.getClassLoader()
                    .getResourceAsStream("db.properties")) {
                props.load(input); // محتوای فایل رو داخل شیء props لود میکنه
            } catch (Exception e) {
                throw new RuntimeException("error in reading DB ", e);
            }
        }

        public static String getUrl() { return props.getProperty("db.url"); }
        public static String getUser() { return props.getProperty("db.user"); }
        public static String getPassword() { return props.getProperty("db.password"); }
    }
}

