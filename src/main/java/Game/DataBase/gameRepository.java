package Game.DataBase;

import Game.GameManager;
import Game.Player;
import Kingdom.Block.Block;
import Kingdom.Block.EmptyBlock;
import Kingdom.Block.ForestBlock;
import Kingdom.Block.VoidBlock;
import Kingdom.Structure.*;
import Kingdom.Unit.*;
import Utils.RuntimeTypeAdapter;
import Utils.LoggerManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class gameRepository {
    private static final Gson gson = createGsonInstance();
    private static Gson createGsonInstance() {
        // ساختن آداپتور مخصوص برای سلسله مراتب کلاس های Kingdom
        RuntimeTypeAdapter<Block> blockAdapterFactory = RuntimeTypeAdapter
                .of(Block.class, "type")
                .registerSubtype(EmptyBlock.class) // ثبت کلاس های فرزند
                .registerSubtype(ForestBlock.class)
                .registerSubtype(VoidBlock.class);


        RuntimeTypeAdapter<Structure> structureAdapterFactory = RuntimeTypeAdapter
                .of(Structure.class, "type")
                .registerSubtype(TownHall.class)
                .registerSubtype(Barrack.class)
                .registerSubtype(Farm.class)
                .registerSubtype(Market.class)
                .registerSubtype(Tower.class);


        RuntimeTypeAdapter<Unit> unitAdapterFactory = RuntimeTypeAdapter
                .of(Unit.class, "type")
                .registerSubtype(Peasant.class)
                .registerSubtype(Spearman.class)
                .registerSubtype(Swordman.class)
                .registerSubtype(Knight.class);


        return new GsonBuilder()
                .registerTypeAdapterFactory(blockAdapterFactory)
                .registerTypeAdapterFactory(structureAdapterFactory)
                .registerTypeAdapterFactory(unitAdapterFactory)
                .setPrettyPrinting()
                .create();
    }


    public static void saveGame(GameManager gameManager, String gameName) {
        String sql = "INSERT INTO saved_games (game_name, game_state) VALUES (?, ?::jsonb)";
        try (Connection conn = dataBaseConnection.getConnection(); // برای مدیریت خودکار اتصال
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String gameStateJson = gson.toJson(gameManager); // کل گیم منیجر تبدیل به یک رشته جیسون میکنه
            pstmt.setString(1, gameName);
            pstmt.setString(2, gameStateJson);
            pstmt.executeUpdate();
            System.out.println("Game saved successfully as '" + gameName + "'!");
            LoggerManager.info("Game saved successfully as '" + gameName + "'!");

        } catch (SQLException e) {
            System.err.println("Error saving game state: " + e.getMessage());
            LoggerManager.severe("Error saving game state: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static GameManager loadGame(String gameName) {
        String sql = "SELECT game_state FROM saved_games WHERE game_name = ? ORDER BY save_time DESC LIMIT 1"; // آخرین سیو با همون نام رو برمیگردونه
        GameManager loadedGameManager = null;
        try (Connection conn = dataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gameName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) { // بررسی میکنه ذخیره ایی پیدا شده یا نه
                String gameStateJson = rs.getString("game_state");
                loadedGameManager = gson.fromJson(gameStateJson, GameManager.class); // گیم منیجر از رشته متنی جیسون تبدیل به حالت قبل میشه
            }
        } catch (SQLException e) {
            System.err.println("Error loading game state: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        if (loadedGameManager != null) {
            loadedGameManager.relinkAfterLoad();
        }
        return loadedGameManager;
    }

    public static void saveWinner(Player winner) {
        String sql = "INSERT INTO game_results (player_name, score, result) VALUES (?, ?, 'WIN')";
        try (Connection conn = dataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, winner.getPlayerName());
            pstmt.setInt(2, winner.getScore());
            pstmt.executeUpdate();
            LoggerManager.info("Winner's results saved successfully!");
        } catch (SQLException e) {
            LoggerManager.severe("Error saving winner result: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static String getLastWinners() {
        String sql = "SELECT player_name, score, result FROM game_results ORDER BY game_date DESC LIMIT 10";
        StringBuilder history = new StringBuilder();
        try (Connection conn = dataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                history.append(rs.getString("player_name"))
                        .append(" (").append(rs.getString("result")).append(")")
                        .append(" - Score: ").append(rs.getInt("score"))
                        .append("\n");
            }
        } catch (SQLException e) {
            history.append("Error loading history");
            LoggerManager.info("Error loading game history: " + e.getMessage());
        }
        return history.toString();
    }
}