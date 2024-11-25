package com.connect4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testConstructorAndGetName() {
        Player player = new Player("Alice", 'X');
        assertEquals("Alice", player.getName());
        assertEquals('X', player.getToken());
    }

    @Test
    void testSetName() {
        Player player = new Player("Alice", 'X');
        player.setName("Bob");
        assertEquals("Bob", player.getName());
    }

    @Test
    void testEquals_SameObject() {
        Player player = new Player("Alice", 'X');
        assertTrue(player.equals(player));
    }

    @Test
    void testEquals_DifferentObject() {
        Player player1 = new Player("Alice", 'X');
        Player player2 = new Player("Alice", 'X');
        assertTrue(player1.equals(player2));
    }

    @Test
    void testEquals_DifferentToken() {
        Player player1 = new Player("Alice", 'X');
        Player player2 = new Player("Alice", 'O');
        assertFalse(player1.equals(player2));
    }

    @Test
    void testEquals_DifferentName() {
        Player player1 = new Player("Alice", 'X');
        Player player2 = new Player("Bob", 'X');
        assertFalse(player1.equals(player2));
    }

    @Test
    void testNotEquals_Null() {
        Player player = new Player("Alice", 'X');
        assertFalse(player.equals(null));
    }

    @Test
    void testHashCode() {
        Player player1 = new Player("Alice", 'X');
        Player player2 = new Player("Alice", 'X');
        assertEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    void testHashCode_DifferentToken() {
        Player player1 = new Player("Alice", 'X');
        Player player2 = new Player("Alice", 'O');
        assertNotEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    void testHashCode_DifferentName() {
        Player player1 = new Player("Alice", 'X');
        Player player2 = new Player("Bob", 'X');
        assertNotEquals(player1.hashCode(), player2.hashCode());
    }
}
