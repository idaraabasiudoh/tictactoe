import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.security.PrivateKey;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;

/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game.
 *
 * @author Lynn Marshall
 * @version November 8, 2012
 *
 * @author Idara-Abasi Udoh, 101244376
 * @version April 6, 2024
 */
public class TicTacToe implements ActionListener {
    public static final String PLAYER_X = "X"; // Player X
    public static final String PLAYER_O = "O"; // Player O
    public static final String EMPTY = " "; //Empty Cell
    public static final String TIE = "T"; // Game Tie
    private String player; // Current Player ("X" or "O")
    private String winner = EMPTY; // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress
    private int row;
    private int col;
    private int numFreeSquares; // Number of free squares left in the game
    private JPanel buttonPanel;
    private JButton board_buttons[][]; // 3x3 array representation of board
    private JLabel gameLabel;
    private JFrame frame;
    private Container contentPane;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JMenuItem newGameItem;
    private JMenuItem newGameItemOption2;
    private JMenuItem quitItem;
    private JMenuItem helpItem;
    private JMenuItem undoItem;
    private JPanel counterPanel;
    private JLabel scoreSheetLabel;
    private int xWins;
    private int oWins;
    private int ties;

    // Game sound fields
    private Clip moveSound;
    private Clip winSound;
    private Clip tieSound;

    private boolean undoUsed;

    /**
     * Constructs a new TicTacToe Board.
     */
    public TicTacToe() {
        frame = new JFrame();
        contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        // --------- Menu Items ---------
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("Game");
        menuBar.add(fileMenu);

        newGameItem = new JMenuItem("New");
        fileMenu.add(newGameItem);

        undoItem = new JMenuItem("Undo Last Move");
        fileMenu.add(undoItem);

        fileMenu.addSeparator(); // Separator within game menu

        newGameItemOption2 = new JMenuItem("Restart with Player O");
        fileMenu.add(newGameItemOption2);

        fileMenu.addSeparator(); // Separator within game menu

        helpItem = new JMenuItem("Help");
        fileMenu.add(helpItem);

        quitItem = new JMenuItem("Quit");
        fileMenu.add(quitItem);

        // --------- Game Shortcuts ---------
        final int SHORCUT = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORCUT));
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORCUT));
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, SHORCUT));

        newGameItem.addActionListener(this);
        undoItem.addActionListener(this);
        newGameItemOption2.addActionListener(this);
        helpItem.addActionListener(this);
        quitItem.addActionListener(this);

        // --------- Scoresheet Counter ---------
        counterPanel = new JPanel();
        scoreSheetLabel = new JLabel("X Wins: " + xWins + "      |      O Wins: " + oWins + "      |      Ties: " + ties);
        scoreSheetLabel.setFont(new Font(null, 0, 15));
        counterPanel.add(scoreSheetLabel);
        contentPane.add(counterPanel, BorderLayout.NORTH);

        // --------- GUI Buttons ---------
        board_buttons = new JButton[3][3];
        buttonPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < board_buttons.length; i++) {
            for (int j = 0; j < board_buttons[i].length; j++) {
                board_buttons[i][j] = new JButton("");
                board_buttons[i][j].setFont(new Font(null, Font.BOLD, 20));
                board_buttons[i][j].addActionListener(this);
                board_buttons[i][j].setPreferredSize(new Dimension(100, 100));
                board_buttons[i][j].setOpaque(true); // Ensure button is opaque
                buttonPanel.add(board_buttons[i][j]);
            }
        }
        contentPane.add(buttonPanel);


        // --------- Game Label ---------
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        gameLabel = new JLabel();
        bottomPanel.add(gameLabel, BorderLayout.NORTH);
        bottomPanel.setPreferredSize(new Dimension(2000, 100));
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        frame.setPreferredSize(new Dimension(2000, 1000)); // Adjust frame dimensions
        loadSounds();// Load sound files into respective fields
    }

    /**
     * Displays the help GUI.
     */
    private void showHelpGUI() {
        // Create a new JFrame for the help GUI
        JFrame helpFrame = new JFrame("Help");
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpFrame.setSize(505, 250);
        helpFrame.setResizable(false);

        // Create a JTextArea to display the help text
        JTextArea helpTextArea = new JTextArea();
        helpTextArea.setText("TIC TAC TOE\n\n" +
                "Welcome to Tic Tac Toe! This game allows two players to take turns marking\n" +
                "the spaces in a 3x3 grid with X or O. The player who succeeds in placing three\n" +
                "of their marks in a horizontal, vertical, or diagonal row wins the game. If all\n" +
                "spaces on the board are filled and no player has won, the game ends in a tie.\n\n" +
                "To start a new game, click on 'Game' in the menu bar and select 'New'. You can\n" +
                "also restart the game with Player O making the first move by selecting 'Restart\n" +
                "with Player O'. Use 'Undo' to undo last move. Enjoy playing!");

        // Make the text area non-editable and add it to a scroll pane
        helpTextArea.setEditable(false);
        helpTextArea.setBackground(Color.LIGHT_GRAY);
        helpTextArea.setOpaque(true);
        JScrollPane scrollPane = new JScrollPane(helpTextArea);

        // Add the scroll pane to the helpFrame
        helpFrame.getContentPane().add(scrollPane);

        // Center the helpFrame on the screen
        helpFrame.setLocationRelativeTo(null);

        // Make the helpFrame visible
        helpFrame.setVisible(true);
    }
    /**
     * Checks if the Tic Tac Toe board is empty.
     *
     * @return true if the board is empty, false otherwise.
     */
    private boolean isBoardEmpty() {
        // Iterate through each cell of the Tic Tac Toe board
        for (int i = 0; i < board_buttons.length; i++) {
            for (int j = 0; j < board_buttons[i].length; j++) {
                // If any cell is marked with "X" or "O", return false indicating that the board is not empty
                if (!board_buttons[i][j].getText().equals(EMPTY)) {
                    return false;
                }
            }
        }
        // If no cell is marked with "X" or "O", return true indicating that the board is empty
        return true;
    }

    /**
     * Undo the last move made in the game.
     */
    private void undoLastMove(int row, int col) {
        if (!(isBoardEmpty()) && (winner == EMPTY)) {
            if (undoUsed)
            {
                gameLabel.setText("Undo already used. Can't use again");
                return;
            }
            // Clear the cell corresponding to the last move
            board_buttons[row][col].setText(EMPTY);
            board_buttons[row][col].setEnabled(true);
            numFreeSquares++;

            // Switch the player's turn back to the previous player
            if (player == PLAYER_X) player = PLAYER_O;
            else player = PLAYER_X;
            gameLabel.setText("Undo: " + player + "'s turn");
            undoUsed = true;
        } else {
            gameLabel.setText("No move to undo");
        }
    }

    /**
     * Clears the Tic Tac Toe board, resets game variables, and sets the player for the new game.
     * Marks all squares in the Tic Tac Toe board as empty and enables them.
     * Sets the game label to indicate the current player's turn.
     *
     * @param option An integer representing the option chosen for starting the new game:
     *               - If option is 1, the player for the new game will be PLAYER_X.
     *               - If option is not 1, the player for the new game will be PLAYER_O.
     */
    private void clearBoard(int option) {
        clearWinningStreakHighlight(); // Clear any previous winning streak highlighting

        // Clear the Tic Tac Toe board by setting all buttons to empty and enabling them
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board_buttons[i][j].setText(EMPTY);
                board_buttons[i][j].setEnabled(true);
                buttonPanel.add(board_buttons[i][j]);
            }
        }
        // Reset game variables
        winner = EMPTY;
        numFreeSquares = 9;
        undoUsed = false;

        // Determine the player for the new game
        if (option == 1) player = PLAYER_X;
        else player = PLAYER_O;

        // Update the game label to indicate the current player's turn
        gameLabel.setText("Game in progress. " + player + "'s turn.");
        gameLabel.setFont(new Font(null, 0, 18));
    }

    /**
     * Plays one game of Tic Tac Toe.
     */
    public void playGame() {
        clearBoard(1);


        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);
    }

    /**
     * Checks if there is a winner in the Tic Tac Toe game.
     *
     * @param row The row index of the last move.
     * @param col The column index of the last move.
     * @return true if there is a winner, false otherwise.
     */
    private boolean haveWinner(int row, int col) {
        // Requires 4 or less free squares to have a winner.
        if (numFreeSquares > 4) return false;

        // Check if the current player has won horizontally
        if (board_buttons[row][0].getText().equals(board_buttons[row][1].getText()) && board_buttons[row][0].getText().equals(board_buttons[row][2].getText()))
        {
            highlightWinningStreak(new int[][]{{row, 0}, {row, 1}, {row, 2}});
            return true;
        }

        // Check if the current player has won vertically
        if (board_buttons[0][col].getText().equals(board_buttons[1][col].getText()) && board_buttons[0][col].getText().equals(board_buttons[2][col].getText()))
        {
            highlightWinningStreak(new int[][]{{0, col}, {1, col}, {2, col}});
            return true;
        }

        // Check if the current player has won diagonally from top-left to bottom-right
        if (row == col)
            if (board_buttons[0][0].getText().equals(board_buttons[1][1].getText()) && board_buttons[0][0].getText().equals(board_buttons[2][2].getText()))
            {
                highlightWinningStreak(new int[][]{{0, 0}, {1, 1}, {2, 2}});
                return true;
            }

        // Check if the current player has won diagonally from top-right to bottom-left
        if (row == 2 - col)
            if (board_buttons[0][2].getText().equals(board_buttons[1][1].getText()) && board_buttons[0][2].getText().equals(board_buttons[2][0].getText()))
            {
                highlightWinningStreak(new int[][]{{0, 2}, {1, 1}, {2, 0}});
                return true;
            }

        // No condition met
        return false;
    }

    /**
     * Updates the counters for X and O wins.
     */
    private void updateCounters() {
        scoreSheetLabel.setText("X Wins: " + xWins + "      |      O Wins: " + oWins + "      |      Ties: " + ties);
    }


    /**
     * Loads sound into variables
     */
    private void loadSounds() {
        try {
            // Load move sound
            AudioInputStream moveStream = AudioSystem.getAudioInputStream(new File("move.wav"));
            moveSound = AudioSystem.getClip();
            moveSound.open(moveStream);

            // Load win sound
            AudioInputStream winStream = AudioSystem.getAudioInputStream(new File("win.wav"));
            winSound = AudioSystem.getClip();
            winSound.open(winStream);

            // Load tie sound
            AudioInputStream tieStream = AudioSystem.getAudioInputStream(new File("tie.wav"));
            tieSound = AudioSystem.getClip();
            tieSound.open(tieStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to play move sound
     */

    private void playMoveSound() {
        if (moveSound != null) {
            moveSound.setFramePosition(0);
            moveSound.start();
        }
    }

    /**
     * Method to play win sound
     */
    private void playWinSound() {
        if (winSound != null) {
            winSound.setFramePosition(0);
            winSound.start();
        }
    }

    /**
     * Method to play tie sound
     */
    private void playTieSound() {
        if (tieSound != null) {
            tieSound.setFramePosition(0);
            tieSound.start();
        }
    }

    /**
     * Highlights the winning streak by changing the appearance of the winning cells.
     *
     * @param winningCells The array of winning cells to be highlighted.
     */
    private void highlightWinningStreak(int[][] winningCells) {
        for (int[] cell : winningCells) {
            int row = cell[0];
            int col = cell[1];
            board_buttons[row][col].setBackground(Color.GREEN); // Change the background color of winning cells
        }
    }

    /**
     * Clears the highlighting of the winning streak by resetting the appearance of the cells.
     */
    private void clearWinningStreakHighlight() {
        for (int i = 0; i < board_buttons.length; i++) {
            for (int j = 0; j < board_buttons[i].length; j++) {
                board_buttons[i][j].setBackground(null); // Reset the background color of all cells
            }
        }
    }

    /**
     * Performs an action based on the user's interaction with the GUI components.
     * If the action is triggered by a button click, it updates the Tic Tac Toe board
     * and checks for a winner or a tie. If the action is triggered by a menu item,
     * it starts a new game or quits the application accordingly.
     *
     * @param e The ActionEvent representing the user's action.
     */
    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();

        // Action triggered by button click
        if (object instanceof JButton) {
            JButton button = (JButton) object;
            for (int i = 0; i < board_buttons.length; i++)
                for (int j = 0; j < board_buttons[i].length; j++) {
                    if (button == board_buttons[i][j]) {
                        row = i;
                        col = j;

                        // No winner yet
                        if (winner == EMPTY)
                        {
                            // Update the board with the current player's symbol
                            board_buttons[i][j].setText(player);
                            board_buttons[i][j].setEnabled(false);
                            numFreeSquares--;
                        }
                        // Check if the current player has won
                        if (haveWinner(row, col)) {
                            winner = player;
                            gameLabel.setText("Player " + player + " wins!!!");
                            playWinSound();
                            disableButtons();


                            // Update scoresheet counters
                            if (winner == PLAYER_X)
                            {
                                xWins ++;
                                updateCounters();
                            }
                            else
                            {
                                oWins ++;
                                updateCounters();
                            }
                            // Declare Tie
                        } else if (numFreeSquares == 0) {
                            winner = TIE;
                            gameLabel.setText("Game is a tie");
                            ties++;
                            updateCounters();
                            playTieSound();


                            // Game still in play. Switch Turns
                        } else if (winner == EMPTY){
                            if (player == PLAYER_X) player = PLAYER_O;
                            else player = PLAYER_X;
                            gameLabel.setText("Game is in progress " + player + "'s turn");
                            playMoveSound();
                            undoUsed = false;

                        }

                    }
                }

            // Action triggered by menu item
        } else {
            JMenuItem option = (JMenuItem) object;
            if (option == newGameItem) clearBoard(1); // Start a new game with player X
            else if (option == newGameItemOption2) clearBoard(2); // Start a new game with player O
            else if (option == helpItem) showHelpGUI();
            else if (option == undoItem) undoLastMove(row, col);

            else if (option == quitItem){ // Quit the application
                frame.setVisible(false);
                frame.dispose();
            }
        }
    }

    /**
     * Method to disable buttons
     */
    public void disableButtons(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board_buttons[i][j].setEnabled(false);
            }
        }

    }
}