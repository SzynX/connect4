package com.connect4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility osztály az adatbázis műveletekhez.
 */
public final class DatabaseHelper {
    /**
     * Database.
     */
    private static final String DATABASE_URL = "jdbc:sqlite:connect4.db";
    /**
     * Várakozási idő.
     */
    private static final int TIMEOUT = 5000; // 5 másodperc várakozás

    /**
     * Privát konstruktor, hogy ne lehessen példányosítani.
     */
    private DatabaseHelper() {
        // Privát konstruktor, hogy ne lehessen példányosítani
    }

    /**
     * Kapcsolódik az SQLite adatbázishoz.
     *
     * @return a Connection objektum,
     * amely az adatbázis kapcsolatot reprezentálja
     * @throws SQLException ha hiba történik a kapcsolat létrehozása során
     */
    public static Connection connect() throws SQLException {
        try {
            // Betöltjük az SQLite JDBC drivert
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASE_URL);
            // Beállítjuk a kapcsolatot
            conn.setAutoCommit(false); // Kizárjuk az automatikus commitot
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setNetworkTimeout(null, TIMEOUT); // 5 másodperc várakozás
            return conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("SQLite JDBC driver nem található.", e);
        }
    }

    /**
     * Táblák létrehozása, ha még nem léteznek.
     */
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS players ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                +
                "wins "
                +
                "INTEGER DEFAULT 0);";
        try (Connection conn = connect(); Statement stmt =
                conn.createStatement()) {
            stmt.execute(sql);
            conn.commit(); // Módosítások commitálása
            System.out.println("Tábla létrehozása sikeres "
                    +
                    "vagy már létezik.");
        } catch (SQLException e) {
            System.out.println("Hiba a tábla létrehozása során: "
                    +
                    e.getMessage());
        }
    }

    /**
     * Az adatbázis nullázása
     * (tábla törlés és
     * újra létrehozás).
     */
    public static void resetDatabase() {
        String deleteSql = "DROP TABLE IF "
                +
                "EXISTS players"; // Töröljük a táblát
        try (Connection conn = connect(); Statement stmt =
                conn.createStatement()) {
            stmt.execute(deleteSql); // Táblát töröljük
            createTable(); // Újra
            // létrehozzuk a táblát
            conn.commit(); // Módosítások commitálása
            System.out.println("Az adatbázis sikeresen "
                    +
                    "nullázva lett.");
        } catch (SQLException e) {
            System.out.println("Hiba az adatbázis "
                    +
                    "nullázása során: "
                    +
                    e.getMessage());
        }
    }

    /**
     * Új játékos hozzáadása vagy
     * győzelem frissítése,
     * ha már létezik a játékos.
     *
     * @param name a játékos neve
     */
    public static void
    addPlayer(
    final String name) {
        String checkSql = "SELECT id FROM players WHERE name = ?";
        try (Connection conn = connect(); PreparedStatement checkStmt =
                conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, name);
            ResultSet rs =
                    checkStmt.executeQuery();

            // Ha létezik a játékos,
            // frissítjük a győzelmeit
            if (rs.next()) {
                updateWins(name);
            } else {
                // Ha nem létezik,
                // új játékos hozzáadása
                String insertSql = "INSERT INTO players "
                        +
                        "(name, wins) VALUES (?, 0)";
                try (PreparedStatement pstmt =
                             conn.prepareStatement(insertSql)) {
                    pstmt.setString(1, name);
                    pstmt.executeUpdate();
                    conn.commit(); // Módosítások commitálása
                }
            }
        } catch (SQLException e) {
            System.out.println("Hiba a játékos hozzáadása "
                    +
                    "vagy frissítése során: "
                    +
                    e.getMessage());
        }
    }

    /**
     * A játékos győzelmeinek
     * frissítése.
     *
     * @param name a játékos neve,
     *             akinek a
     *             győzelmét frissítjük
     */
    public static void updateWins(
    final String name) {
        String sql = "UPDATE players SET wins = "
                +
                "wins + 1 WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt =
                conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            conn.commit(); // Módosítások commitálása
        } catch (SQLException e) {
            System.out.println("Hiba a győzelmek "
                    +
                    "frissítése "
                    +
                    "során: " + e.getMessage());
        }
    }

    /**
     * A high score
     * táblázat lekérdezése.
     */
    public static void displayHighScores() {
        String sql = "SELECT name, wins FROM players"
                +
                " ORDER BY wins DESC LIMIT 10";
        try (Connection conn = connect();
             Statement stmt =
                conn.createStatement();
             ResultSet rs =
                     stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getString("name") + ": "
                        +
                        rs.getInt("wins")
                        +
                        " wins");
            }
        } catch (SQLException e) {
            System.out.println("Hiba a high score "
                    +
                    "táblázat lekérdezése során: "
                    +
                    e.getMessage());
        }
    }

    /**
     * A fő program, amely bemutatja a
     * metódusok működését.
     *
     * @param args a parancssori argumentumok
     */
    public static void main(final String[] args) {
        createTable(); // Tábla létrehozása
        addPlayer("Alice"); // Játékos hozzáadása
        addPlayer("Bob"); // Játékos hozzáadása
        updateWins("Alice"); // Alice győzelmének frissítése
        displayHighScores(); // High score táblázat megjelenítése
        resetDatabase(); // Adatbázis nullázása
        displayHighScores(); // High score táblázat újra megjelenítése
    }
}
