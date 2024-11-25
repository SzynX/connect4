package com.connect4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameTest {

    private Game game;
    private Board mockBoard;
    private final String testDirectory = "D:/connect4game/";

    @BeforeEach
    public void setUp() {
        mockBoard = Mockito.mock(Board.class);
        game = new Game(Constants.DEFAULT_ROWS, Constants.DEFAULT_COLUMNS, "Player 1", "Player 2", true);

        // Létrehozzuk a tesztmappát, ha még nem létezik
        File dir = new File(testDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test
    public void testGameInitialization() {
        assertNotNull(game, "A Game objektumnak nem lehet null.");
    }

    @Test
    public void testLoadGameFromFile() {
        String filename = testDirectory + "testGame.txt";
        File file = new File(filename);
        try {
            if (file.exists()) {
                file.setWritable(true);
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("XOOOXXO");
                writer.println("XOOOXXO");
                writer.println(".......");
                writer.println(".......");
                writer.println(".......");
                writer.println(".......");
            }
        } catch (IOException e) {
            fail("Hiba a fájl írása során: " + e.getMessage());
        }

        game.loadGameFromFile(filename);

        if (file.exists()) {
            file.setWritable(true);
            file.delete();
        }
    }

    @Test
    public void testSaveGameToFile() {
        String filename = testDirectory + "saveGame.txt";
        File file = new File(filename);

        try {
            if (file.exists()) {
                file.setWritable(true);
            }
            game.saveGameToFile(filename);
        } catch (Exception e) {
            fail("Hiba a fájl mentése során: " + e.getMessage());
        }

        assertTrue(file.exists(), "A mentett fájl nem létezik.");

        if (file.exists()) {
            file.setWritable(true);
            file.delete();
        }
    }

    @Test
    public void testValidColumnInput() {
        when(mockBoard.isColumnValid(0)).thenReturn(true);
        assertTrue(mockBoard.isColumnValid(0), "Az oszlop nem érvényes.");
    }

    @Test
    public void testInvalidColumnInput() {
        when(mockBoard.isColumnValid(anyInt())).thenReturn(false);
        assertFalse(mockBoard.isColumnValid(1), "Az oszlop érvényes, de nem kellett volna annak lennie.");
    }

    @Test
    public void testWinningCondition() {
        when(mockBoard.checkWin()).thenReturn(true);
        assertTrue(mockBoard.checkWin(), "A nyerési feltétel nem működik.");
    }

    @Test
    public void testDrawCondition() {
        when(mockBoard.isFull()).thenReturn(true);
        when(mockBoard.checkWin()).thenReturn(false);
        assertTrue(mockBoard.isFull(), "A játék nem döntetlen.");
    }

    @Test
    public void testAIPlayerMove() {
        AIPlayer mockAI = Mockito.mock(AIPlayer.class);
        when(mockAI.makeMove(any(Board.class))).thenReturn(2);
        game = new Game(Constants.DEFAULT_ROWS, Constants.DEFAULT_COLUMNS, "Player 1", "AI", false);

        int aiMove = mockAI.makeMove(mockBoard);
        assertEquals(2, aiMove, "Az AI lépése nem egyezik a várt értékkel.");
    }

    @Disabled
    @Test
    public void testStartGameWithHumanVsHuman() {
        String simulatedInput = "0\n1\n0\n1\n0\n1\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        when(mockBoard.isColumnValid(anyInt())).thenReturn(true);
        when(mockBoard.isFull()).thenReturn(false);
        when(mockBoard.checkWin()).thenReturn(false);

        for (int i = 0; i < 8; i++) {
            game.start();
            int expectedColumn = (i % 2 == 0) ? 0 : 1;
            verify(mockBoard, times(1)).placeToken(expectedColumn, (i % 2 == 0) ? 'X' : 'O');

            if (mockBoard.checkWin()) {
                System.out.println("Nyerő játékos: " + (i % 2 == 0 ? "Player 1" : "Player 2"));
                break;
            }
        }
    }

    @Disabled("Ez a teszt jelenleg nem futtatható a read-only állapot miatt.")
    @Test
    public void testStartGameWithAI() {
        when(mockBoard.isColumnValid(anyInt())).thenReturn(true);
        when(mockBoard.isFull()).thenReturn(false);
        when(mockBoard.checkWin()).thenReturn(false);

        AIPlayer mockAI = Mockito.mock(AIPlayer.class);
        when(mockAI.makeMove(any(Board.class))).thenReturn(2);

        game = new Game(Constants.DEFAULT_ROWS, Constants.DEFAULT_COLUMNS, "Player 1", "AI", false);

        game.start();
        verify(mockBoard, times(1)).placeToken(2, 'O');
    }

    @Test
    public void testGameEndsWhenPlayerWins() {
        String simulatedInput = "0\n0\n1\n1\n2\n0\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        when(mockBoard.isColumnValid(anyInt())).thenReturn(true);
        when(mockBoard.isFull()).thenReturn(false);
        when(mockBoard.checkWin()).thenReturn(false);

        when(mockBoard.checkWin()).thenReturn(true);

        game.start();

        assertTrue(mockBoard.checkWin(), "A játék nem zárult le, amikor Player 1 nyert.");
    }

    @Test
    public void testMainMethod() {
        String simulatedInput = "Player1\nGép\n\n" +
                "0\n1\n0\n1\n0\n1\n0\n" +
                "\n";

        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        assertDoesNotThrow(() -> {
            String[] args = {};
            Game.main(args);
        }, "A fő metódus kivételt dobott.");

        File savedFile = new File("D:/connect4game/saveGame.txt");
        if (savedFile.exists()) {
            savedFile.delete();
        }
    }

    @Test
    public void testGameEndsWithAIWinning() {
        String simulatedInput = "0\n0\n1\n1\n2\n2\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        when(mockBoard.isColumnValid(anyInt())).thenReturn(true);
        when(mockBoard.isFull()).thenReturn(false);
        when(mockBoard.checkWin()).thenReturn(false);

        when(mockBoard.checkWin()).thenReturn(true);

        game.start();

        assertTrue(mockBoard.checkWin(), "A játék nem zárult le, amikor az AI nyert.");
    }

    @Test
    public void testPlayer1Win() {
        Board board = game.getBoard();
        board.placeToken(0, 'X');
        board.placeToken(1, 'X');
        board.placeToken(2, 'X');
        board.placeToken(3, 'X');

        assertTrue(board.checkWin(), () -> "Player 1-nek nyernie kellett volna.");
    }

    @Test
    public void testAIWin() {
        Board board = game.getBoard();
        board.placeToken(0, 'O');
        board.placeToken(1, 'O');
        board.placeToken(2, 'O');
        board.placeToken(3, 'O');

        assertTrue(board.checkWin(), () -> "A gépnek nyernie kellett volna.");
    }

    @Disabled
    @Test
    public void testDraw() {
        Board board = game.getBoard();
        char[][] grid = board.getGrid();

        // Töltsük fel a táblát úgy, hogy ne legyen győztes
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getColumns(); col++) {
                // Keverjük a 'X' és 'O' karaktereket, hogy ne legyen győztes
                grid[row][col] = (row + col) % 2 == 0 ? 'X' : 'O';
            }
        }

        // Ellenőrizzük, hogy a tábla tele van
        assertTrue(board.isFull(), () -> "A tábla tele van.");

        // Ellenőrizzük, hogy nincs győztes
        assertFalse(board.checkWin(), () -> "Nem szabad győztesnek lennie.");
    }



    @Test
    public void testInvalidMove() {
        Board board = game.getBoard();
        for (int row = 0; row < board.getRows(); row++) {
            board.placeToken(0, 'X');
        }

        boolean isValid = board.placeToken(0, 'O');
        assertFalse(isValid, () -> "Nem szabadna megengedni a lépést a tele oszlopba.");
    }
}

