package com.connect4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;

    @BeforeEach
    public void setUp() {
        // Beállítjuk a táblát 6 sorral és 7 oszloppal
        board = new Board(6, 7);
    }

    @Test
    public void testPlaceToken() {
        assertTrue(board.placeToken(0, 'X')); // Érvényes oszlop
        assertTrue(board.placeToken(0, 'O')); // Érvényes oszlop
        assertThrows(IllegalArgumentException.class, () -> board.placeToken(7, 'X')); // Érvénytelen oszlop
    }

    @Test
    public void testGetRows() {
        assertEquals(6, board.getRows());
    }

    @Test
    public void testGetColumns() {
        assertEquals(7, board.getColumns());
    }

    @Test
    public void testIsFull() {
        // Ellenőrizzük, hogy a tábla nem teljes
        assertFalse(board.isFull());

        // Töltsük fel a táblát, és ellenőrizzük, hogy a tábla tele van
        for (int col = 0; col < board.getColumns(); col++) {
            for (int row = 0; row < board.getRows(); row++) {
                board.placeToken(col, 'X'); // Helyezünk el tokent minden oszlopba
            }
        }
        assertTrue(board.isFull()); // A tábla most már tele van
    }

    @Test
    public void testCheckWin() {
        assertFalse(board.checkWin());

        // Hozzáadunk egy vízszintes győzelmet
        board.placeToken(0, 'X');
        board.placeToken(1, 'X');
        board.placeToken(2, 'X');
        board.placeToken(3, 'X');
        assertTrue(board.checkWin());
    }

    @Test
    public void testDisplayBoard() {
        board.displayBoard(); // Ellenőrizzük, hogy a metódus nem dob kivételt
    }

    @Test
    public void testEquals() {
        Board anotherBoard = new Board(6, 7);
        assertEquals(board, anotherBoard);

        board.placeToken(0, 'X');
        assertNotEquals(board, anotherBoard);
    }

    @Test
    public void testHashCode() {
        Board anotherBoard = new Board(6, 7);
        assertEquals(board.hashCode(), anotherBoard.hashCode());
    }

    @Test
    public void testCheckWinVertical() {
        // Hozzáadunk egy függőleges győzelmet
        board.placeToken(0, 'X');
        board.placeToken(0, 'X');
        board.placeToken(0, 'X');
        board.placeToken(0, 'X');
        assertTrue(board.checkWin());
    }
}
