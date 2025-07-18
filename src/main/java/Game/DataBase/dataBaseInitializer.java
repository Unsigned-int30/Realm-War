package Game.DataBase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class dataBaseInitializer {
    public static void init() {

        String createGameResultsTableSql = """
       
                CREATE TABLE IF NOT EXISTS game_results (
           result_id SERIAL PRIMARY KEY,
           player_name VARCHAR(100) NOT NULL,
           score INTEGER NOT NULL,
           result VARCHAR(20) NOT NULL,
           game_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
       );
        """;


        String createSavedGamesTableSql  = """
CREATE TABLE IF NOT EXISTS saved_games (
    save_id SERIAL PRIMARY KEY,
    game_name VARCHAR(100) NOT NULL,
    save_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    game_state JSONB NOT NULL,
    is_autosave BOOLEAN DEFAULT FALSE
);
""";




        String createGameSnapshotsTableSql = """
        CREATE TABLE IF NOT EXISTS game_snapshots (
            snapshot_id SERIAL PRIMARY KEY,
            save_id INTEGER REFERENCES saved_games(save_id),
            turn_number INTEGER NOT NULL,
            snapshot_data JSONB NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );
        """;

        try (Connection conn = dataBaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Execute all table creation statements
            stmt.execute(createGameResultsTableSql);
            stmt.execute(createSavedGamesTableSql);
            stmt.execute(createGameSnapshotsTableSql);

            System.out.println("Database tables checked/created successfully.");

        } catch (SQLException e) {

            throw new RuntimeException("Error initializing the database!", e);
        }
    }
}