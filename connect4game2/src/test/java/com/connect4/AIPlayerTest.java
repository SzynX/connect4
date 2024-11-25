package com.connect4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AIPlayerTest {

    private AIPlayer aiPlayer;
    private Board mockBoard;

    @BeforeEach
    public void setUp() {
        aiPlayer = new AIPlayer("AI", 'X');
        mockBoard = mock(Board.class);
    }

    @Test
    public void testMakeMove_ValidColumn() {
        // Mock the behavior of the board
        when(mockBoard.getColumns()).thenReturn(7); // Assuming 7 columns
        when(mockBoard.isColumnValid(0)).thenReturn(false);
        when(mockBoard.isColumnValid(1)).thenReturn(true); // First valid column is 1
        when(mockBoard.isColumnValid(2)).thenReturn(false);
        when(mockBoard.isColumnValid(3)).thenReturn(false);
        when(mockBoard.isColumnValid(4)).thenReturn(false);
        when(mockBoard.isColumnValid(5)).thenReturn(false);
        when(mockBoard.isColumnValid(6)).thenReturn(false);

        // Call the method under test
        int column = aiPlayer.makeMove(mockBoard);

        // Assert that the AI player chose the first valid column (1)
        assertEquals(1, column);
    }

    @Test
    public void testMakeMove_NoValidColumn() {
        // Mock the behavior of the board
        when(mockBoard.getColumns()).thenReturn(7);
        when(mockBoard.isColumnValid(anyInt())).thenReturn(false); // No valid columns

        // Call the method under test
        int column = aiPlayer.makeMove(mockBoard);

        // Assert that the AI player returned -1 (no valid column)
        assertEquals(-1, column);
    }
}
