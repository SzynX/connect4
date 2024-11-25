package com.connect4;

/**
 * A Board osztály felelős a Connect4 játék táblájának kezeléséért.
 */
public class Board {
    /** A táblázat sorainak száma. */
    private final int rows;

    /** A táblázat oszlopainak száma. */
    private final int columns;

    /** A táblázat mátrixa. */
    private final char[][] grid;

    /** A nyeréshez szükséges sorozatok száma. */
    private static final int WINNING_COUNT = 4;

    /** HashCode mágikus szám. */
    private static final int HASH_MULTIPLIER = 31;

    /** A játékosok tokenjei. */
    private static final char EMPTY_SLOT = '\0';

    /** Ellenőrzéshez szükséges mágikus számok. */
    private static final int THREE = 3;

    /**
     * Konstruktor a Board osztályhoz.
     *
     * @param rowsParam A sorok száma a táblán.
     * @param columnsParam Az oszlopok száma a táblán.
     */
    public Board(final int rowsParam, final int columnsParam) {
        this.rows = rowsParam;
        this.columns = columnsParam;
        this.grid = new char[rows][columns];
    }

    /**
     * Elhelyezi a játékos tokenjét a megadott oszlopban.
     *
     * @param column Az oszlop, ahova a token kerül.
     * @param token  A játékos tokenje.
     * @return True, ha a token sikeresen elhelyezve, különben false.
     */
    public boolean placeToken(final int column, final char token) {
        if (column < 0 || column >= columns) {
            throw new IllegalArgumentException("Invalid column index.");
        }

        for (int row = rows - 1; row >= 0; row--) {
            if (grid[row][column] == EMPTY_SLOT) {
                grid[row][column] = token;
                return true;
            }
        }
        return false; // Oszlop tele van
    }

    /**
     * Visszaadja a táblázatot.
     *
     * @return A táblázat mátrixa.
     */
    public char[][] getGrid() {
        return grid;
    }

    /**
     * Visszaadja a táblázat sorainak számát.
     *
     * @return A táblázat sorainak száma.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Visszaadja a táblázat oszlopainak számát.
     *
     * @return A táblázat oszlopainak száma.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Ellenőrzés, hogy a megadott oszlop érvényes-e.
     *
     * @param column Az oszlop, amelyet ellenőrizni kell.
     * @return True, ha az oszlop érvényes, különben false.
     */
    public boolean isColumnValid(final int column) {
        return column >= 0
                && column < columns
                && grid[0][column] == EMPTY_SLOT; // Az első sor ellenőrzése
    }

    /**
     * Ellenőrzi, hogy a táblázat tele van-e.
     *
     * @return True, ha a tábla tele van, különben false.
     */
    public boolean isFull() {
        for (int col = 0; col < columns; col++) {
            if (grid[0][col] == EMPTY_SLOT) {
                return false; // Ha bármelyik oszlopban van üres hely, nem tele
            }
        }
        return true; // Minden oszlop tele van
    }

    /**
     * Ellenőrzi, hogy van-e győztes a táblázatban.
     *
     * @return True, ha van győztes, különben false.
     */
    public boolean checkWin() {
        // Ellenőrzés vízszintesen
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col <= columns - WINNING_COUNT; col++) {
                if (grid[row][col] != EMPTY_SLOT) {
                    if (grid[row][col] == grid[row][col + 1]
                            && grid[row][col] == grid[row][col + 2]
                            && grid[row][col] == grid[row][col + THREE]) {
                        return true;
                    }
                }
            }
        }

        // Ellenőrzés függőlegesen
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row <= rows - WINNING_COUNT; row++) {
                if (grid[row][col] != EMPTY_SLOT) {
                    if (grid[row][col] == grid[row + 1][col]
                            && grid[row][col] == grid[row + 2][col]
                            && grid[row][col] == grid[row + THREE][col]) {
                        return true;
                    }
                }
            }
        }

        // Ellenőrzés átlósan (\)
        for (int row = 0; row <= rows - WINNING_COUNT; row++) {
            for (int col = 0; col <= columns - WINNING_COUNT; col++) {
                if (grid[row][col] != EMPTY_SLOT) {
                    if (grid[row][col] == grid[row + 1][col + 1]
                            && grid[row][col] == grid[row + 2][col + 2]
                            && grid[row][col] == grid[row + THREE]
                            [col + THREE]) {
                        return true;
                    }
                }
            }
        }

        // Ellenőrzés átlósan (/)
        for (int row = WINNING_COUNT - 1; row < rows; row++) {
            for (int col = 0; col <= columns - WINNING_COUNT; col++) {
                if (grid[row][col] != EMPTY_SLOT) {
                    if (grid[row][col] == grid[row - 1][col + 1]
                            && grid[row][col] == grid[row - 2][col + 2]
                            && grid[row][col] == grid[row - WINNING_COUNT + 1]
                            [col + WINNING_COUNT - 1]) {
                        return true;
                    }
                }
            }
        }

        return false; // Nincs győztes
    }

    /**
     * Kiírja a táblát a konzolra.
     */
    public void displayBoard() {
        System.out.println("Tábla:");
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                char displayChar = (grid[row][col] == EMPTY_SLOT)
                        ? '.' : grid[row][col];
                System.out.print(displayChar + " ");
            }
            System.out.println();
        }
    }

    /**
     * Ellenőrzi, hogy két Board objektum egyenlő-e.
     *
     * @param o Az összehasonlítandó objektum.
     * @return True, ha egyenlő, különben false.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Board)) {
            return false;
        }
        Board board = (Board) o;

        // Ellenőrzi, hogy a méretek megegyeznek-e
        if (this.rows != board.rows || this.columns != board.columns) {
            return false;
        }

        // Ellenőrzi, hogy a mátrixok megegyeznek-e
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (this.grid[row][col] != board.grid[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Visszaadja a Board objektum hash kódját.
     *
     * @return A Board hash kódja.
     */
    @Override
    public int hashCode() {
        int result = HASH_MULTIPLIER * rows + columns;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                result = HASH_MULTIPLIER * result + grid[row][col];
            }
        }
        return result;
    }
}
