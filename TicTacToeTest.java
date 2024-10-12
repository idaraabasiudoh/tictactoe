import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TicTacToeTest {

    private TicTacToe game;

    @Before
    public void setUp() {
        game = new TicTacToe();
    }

    @Test
    public void testInitialBoardIsEmpty() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(TicTacToe.EMPTY, game.getBoardCell(row, col));
            }
        }
    }

    @Test
    public void testMakeMove() {
        game.makeMove(0, 0, TicTacToe.PLAYER_X);
        assertEquals(TicTacToe.PLAYER_X, game.getBoardCell(0, 0));
    }

    @Test
    public void testCheckWinRow() {
        game.makeMove(0, 0, TicTacToe.PLAYER_X);
        game.makeMove(0, 1, TicTacToe.PLAYER_X);
        game.makeMove(0, 2, TicTacToe.PLAYER_X);
        assertTrue(game.checkWin(TicTacToe.PLAYER_X));
    }

    @Test
    public void testCheckWinColumn() {
        game.makeMove(0, 0, TicTacToe.PLAYER_O);
        game.makeMove(1, 0, TicTacToe.PLAYER_O);
        game.makeMove(2, 0, TicTacToe.PLAYER_O);
        assertTrue(game.checkWin(TicTacToe.PLAYER_O));
    }

    @Test
    public void testCheckWinDiagonal() {
        game.makeMove(0, 0, TicTacToe.PLAYER_X);
        game.makeMove(1, 1, TicTacToe.PLAYER_X);
        game.makeMove(2, 2, TicTacToe.PLAYER_X);
        assertTrue(game.checkWin(TicTacToe.PLAYER_X));
    }

    @Test
    public void testBoardIsFull() {
        game.makeMove(0, 0, TicTacToe.PLAYER_X);
        game.makeMove(0, 1, TicTacToe.PLAYER_O);
        game.makeMove(0, 2, TicTacToe.PLAYER_X);
        game.makeMove(1, 0, TicTacToe.PLAYER_O);
        game.makeMove(1, 1, TicTacToe.PLAYER_X);
        game.makeMove(1, 2, TicTacToe.PLAYER_O);
        game.makeMove(2, 0, TicTacToe.PLAYER_X);
        game.makeMove(2, 1, TicTacToe.PLAYER_O);
        game.makeMove(2, 2, TicTacToe.PLAYER_X);

        assertTrue(game.isBoardFull());
        assertFalse(game.checkWin(TicTacToe.PLAYER_X));
        assertFalse(game.checkWin(TicTacToe.PLAYER_O));
    }

    @Test
    public void testUndoMove() {
        game.makeMove(0, 0, TicTacToe.PLAYER_X);
        game.undoMove();
        assertEquals(TicTacToe.EMPTY, game.getBoardCell(0, 0));
    }

}