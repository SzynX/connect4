package com.connect4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * A játékot kezelő osztály,
 * amely a játék
 * logikáját tartalmazza.
 */
public class Game {
    /**
     * játék pálya.
     */
    private final Board board;
    /**
     * játékos1.
     */
    private final Player player1;
    /**
     * játékos2.
     */
    private final Player player2;
    /**
     * egymás elleni.
     */
    private final boolean isHumanVsHuman;
    /**
     * gép elleni.
     */
    private final AIPlayer aiPlayer;

    /**
     * Konstruktor a Game osztály példányosításához.
     *
     * @param rows Az oszlopok száma a játéktáblán.
     * @param columns A sorok száma a játéktáblán.
     * @param name1 Az első játékos neve.
     * @param name2 A második játékos neve.
     * @param humanVsHuman Ha igaz, akkor
     *                     ember-ember játék, ha hamis,
     *                     akkor ember-gyep játék.
     */
    public Game(final int rows, final int columns, final String name1,
                final String name2, final boolean humanVsHuman) {
        this.board = new Board(rows, columns);
        this.player1 = new Player(name1, 'X');
        this.player2 = new Player(name2, 'O');
        this.isHumanVsHuman = humanVsHuman;
        this.aiPlayer = new AIPlayer(name2, 'O');

        // Adatbázis inicializálása és játékosok hozzáadása
        DatabaseHelper.createTable();
        DatabaseHelper.addPlayer(name1);
        DatabaseHelper.addPlayer(name2);
    }

    /**
     * Visszaadja a játéktáblát.
     *
     * @return A játéktábla.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Betölti a játék állapotát a fájlból.
     *
     * @param filename A fájl neve,
     *                amelyből beolvasunk.
     */
    public void loadGameFromFile(
    final String filename) {
        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(filename))) {
            for (int row = 0;
                 row < board.getRows(); row++) {
                String line = reader.readLine();
                for (int col = 0; col < board.getColumns(); col++) {
                    board.getGrid()[row][col] = line.charAt(col);
                }
            }
        } catch (IOException e) {
            System.out.println("Hiba a fájl "
                    +
                    "beolvasása során: "
                    +
                    e.getMessage());
        }
    }

    /**
     * Elmenti a játék
     * állapotát a fájlba.
     *
     * @param filename A fájl neve, ahová menteni kell.
     */
    public void saveGameToFile(final String filename) {
        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(filename))) {
            for (int row = 0; row < board.getRows(); row++) {
                for (int col = 0; col < board.getColumns(); col++) {
                    writer.write(
                            board.getGrid()[row][col] == '\0'
                            ? '.'
                            : board.getGrid()[row][col]);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Hiba a "
                    +
                    "fájl mentése során: "
                    +
                    e.getMessage());
        }
    }

    /**
     * A játék indítása és végrehajtása.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("A játék kezdődik!");

        while (true) {
            board.displayBoard();
            if (isTurnSuccessful(scanner, player1)) {
                break;
            }
            if (isHumanVsHuman) {
                if (isTurnSuccessful(scanner, player2)) {
                    break;
                }
            } else {
                int aiColumn = aiPlayer.makeMove(board);
                if (processMove(aiPlayer, aiColumn)) {
                    break;
                }
            }
        }
    }

    /**
     * A játékos lépésének kezelése.
     *
     * @param scanner A
     *                bemenetet olvasó Scanner.
     * @param player A játékos,
     *               akinek a lépését kezeljük.
     * @return Ha sikeres a
     * lépés, igazat ad vissza.
     */
    private boolean isTurnSuccessful(
    final Scanner scanner, final Player player) {
        System.out.println(player.getName()
                +
                " lépése (oszlop: 0-"
                +
                (board.getColumns() - 1) + "):");
        if (scanner.hasNextInt()) {
            int column = scanner.nextInt();
            if (processMove(player, column)) {
                return true;
            }
        } else {
            System.out.println("Nincs több bemenet "
                    +
                    "a játékos lépéséhez.");
            return true;
        }
        return false;
    }

    /**
     * A lépés érvényesítése és
     * a játéktábla frissítése.
     *
     * @param player A játékos,
     *               aki lép.
     * @param column Az oszlop,
     *               amelyben a játékos lép.
     * @return Ha a lépés sikeres,
     * igazat ad vissza.
     */
    private boolean processMove(
    final Player player,
    final int column) {
        if (board.isColumnValid(column)) {
            board.placeToken(column, player.getToken());
            if (board.checkWin()) {
                board.displayBoard();
                System.out.println(player.getName() + " nyert!");
                DatabaseHelper.updateWins(player.getName());
                // A győztes
                // nyereményének frissítése
                return true;
            }
            if (board.isFull()) {
                board.displayBoard();
                System.out.println("A játék döntetlennel zárult!");
                return true;
            }
        } else {
            System.out.println("Érvénytelen lépés! "
                    +
                    "Próbáld újra.");
        }
        return false;
    }

    /**
     * A program belépési pontja.
     * Itt indul el a játék.
     *
     * @param args Parancssori argumentumok.
     */
    public static void main(final String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Adja meg az első játékos nevét:");
        String name1 = scanner.nextLine();
        System.out.println("Adja meg a második "
                +
                "játékos nevét "
                +
                "(ha gép, írjon 'Gép'): ");
        String name2 = scanner.nextLine();

        Game game = new Game(Constants.DEFAULT_ROWS,
                Constants.DEFAULT_COLUMNS,
                name1, name2, !name2.equalsIgnoreCase("Gép"));

        System.out.println("Kérjük, adja meg, hogy "
                +
                "menteni szeretné-e a "
                +
                "játékot? (igen/nem)");
        String saveChoice = scanner.nextLine();
        if (saveChoice.equalsIgnoreCase("igen")) {
            System.out.println("Adja meg a fájlnevet: ");
            String filename = scanner.nextLine();
            game.saveGameToFile(filename);
        }

        game.start();
        DatabaseHelper.displayHighScores();
        // Megjelenítjük a high score táblázatot
    }
}
