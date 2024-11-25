package com.connect4;

/**
 * Az AIPlayer osztály az AI játékost reprezentálja.
 */
public class AIPlayer extends Player {

    /**
     * Konstruktor az AIPlayer osztályhoz.
     *
     * @param name  Az AI játékos neve.
     * @param token Az AI játékos tokenje.
     */
    public AIPlayer(final String name, final char token) {
        super(name, token);
    }

    /**
     * Az AI játékos dönt a következő lépéséről.
     *
     * @param board A játék tábla.
     * @return Az oszlop, ahol az AI játékos lehelyezi a tokent.
     */
    public int makeMove(final Board board) {
        for (int col = 0; col < board.getColumns(); col++) {
            if (board.isColumnValid(col)) {
                return col; // Egyszerű AI, az első érvényes oszlopot választja
            }
        }
        return -1; // Nincs érvényes oszlop
    }
}
