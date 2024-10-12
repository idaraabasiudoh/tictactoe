# Tic-Tac-Toe Game Project

This repository contains a classic Tic-Tac-Toe game implemented in Java. The game includes enhanced features such as sound effects, visual indicators for winning moves, and an "Undo" functionality for better user experience.

## Features

- **Undo Functionality**: Players can undo their last move using the keyboard shortcut `Ctrl/Cmd + Z`. Once undone, the undo option is disabled until a new move is made.
- **Sound Effects**: Different audio effects for player moves, game wins, and draws. 
  - `win.wav`: Sound effect when a player wins.
  - `move.wav`: Sound effect for each player's move.
  - `tie.wav`: Sound effect for a tie/draw.
- **Visual Enhancements**: Highlights winning combinations in rows, columns, or diagonals using color effects.

## Files

- **TicTacToe.java**: Core game logic and user interface implementation using Java.
- **TicTacToe.class**: Compiled version of the TicTacToe.java file.
- **package.bluej**: BlueJ package configuration file for easier integration with the BlueJ IDE.
- **tictactoe.iml**: IntelliJ IDEA configuration file for the project.
- **Audio Files**:
  - `move.wav`: Audio for each player’s move.
  - `win.wav`: Audio for a winning move.
  - `tie.wav`: Audio for a tie game.
  
## Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/idaraabasiudoh/tictactoe.git
    cd tictactoe
    ```

2. **Compile and Run the Java Files**:
    - If using BlueJ or IntelliJ IDEA, import the project using the respective configuration files (`package.bluej` for BlueJ and `tictactoe.iml` for IntelliJ IDEA).
    - If running from the command line:
      ```bash
      javac TicTacToe.java
      java TicTacToe
      ```

3. **Add Audio Files**:
    - Ensure that the `.wav` audio files (`move.wav`, `win.wav`, `tie.wav`) are placed in the same directory as the `TicTacToe.java` file for proper sound effects.

## Feedback and Improvements

This project has undergone multiple iterations based on peer feedback:
- **Rotimi Ajayi** suggested adding the Undo functionality. This was implemented, allowing players to cancel their last move with `Ctrl/Cmd + Z`.
- **Chanelle Edwards** recommended adding more engaging audio and color effects. In response, multiple sound effects were added for player moves, wins, and draws, and visual highlights were introduced for winning lines.

## Future Enhancements

- Implement a difficulty level for AI players.
- Add a multiplayer mode that allows two players to compete over a network.
- Further improve the UI/UX with advanced animations and theme options.

## License

This project is licensed under the MIT License.