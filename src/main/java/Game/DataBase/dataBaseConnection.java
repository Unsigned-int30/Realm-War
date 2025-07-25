package Game.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
// برای اتصال به پایگاه داده
public class dataBaseConnection {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                dataBaseConfig.DatabaseConfig.getUrl(),
                dataBaseConfig.DatabaseConfig.getUser(),
                dataBaseConfig.DatabaseConfig.getPassword()
        );
    }
}
