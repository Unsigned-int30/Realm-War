package Game.DataBase;

import java.io.InputStream;
import java.util.Properties;

public class dataBaseConfig {



    public class DatabaseConfig {
        private static final Properties props = new Properties();

        static {
            try (InputStream input = DatabaseConfig.class.getClassLoader()
                    .getResourceAsStream("db.properties")) {
                props.load(input);
            } catch (Exception e) {
                throw new RuntimeException("error in reading DB ", e);
            }
        }

        public static String getUrl() { return props.getProperty("db.url"); }
        public static String getUser() { return props.getProperty("db.user"); }
        public static String getPassword() { return props.getProperty("db.password"); }
    }
}

