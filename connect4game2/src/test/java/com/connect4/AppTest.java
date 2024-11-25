package com.connect4.app;

import com.connect4.Board; // Ellenőrizd, hogy importálva van a Board osztály
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void testPlaceToken() {
        Board board = new Board(6, 7); // A Board osztály létrehozása
        assertTrue(board.placeToken(0, 'X')); // Ellenőrzi, hogy a token elhelyezése sikeres
        assertEquals('X', board.getGrid()[5][0]); // Ellenőrizd, hogy a token megfelelően van elhelyezve
    }

    // További tesztek hozzáadása az üzleti logika ellenőrzésére
}
