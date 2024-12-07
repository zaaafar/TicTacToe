import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToeGUI {

    private static char[][] board = { { ' ', ' ', ' ' }, { ' ', ' ', ' ' }, { ' ', ' ', ' ' } };
    private static char currentPlayer = 'X';
    private static boolean gameEnded = false;

    private static int scoreX = 0;
    private static int scoreO = 0;

    private static String playerXName = "Player X";
    private static String playerOName = "Player O";

    private static JFrame frame;
    private static JButton[][] buttons = new JButton[3][3];
    private static JLabel statusLabel;
    private static JLabel scoreLabel;

    private static boolean isSinglePlayer = false;

    public static void main(String[] args) {
        showModeSelectionScreen();
    }

    private static void showModeSelectionScreen() {
        JFrame modeFrame = new JFrame("Select Mode");
        modeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        modeFrame.setSize(400, 300);
        modeFrame.setLayout(new GridLayout(4, 1));
        modeFrame.getContentPane().setBackground(new Color(30, 30, 30));

        JLabel title = new JLabel("Choose Game Mode", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(255, 255, 255));
        modeFrame.add(title);

        JButton onePlayerButton = new JButton("One Player Mode");
        onePlayerButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        onePlayerButton.setBackground(new Color(70, 130, 180));
        onePlayerButton.setForeground(Color.WHITE);
        onePlayerButton.addActionListener(e -> {
            isSinglePlayer = true;
            modeFrame.dispose();
            askForSinglePlayerName();
            setupGUI();
        });
        modeFrame.add(onePlayerButton);

        JButton twoPlayerButton = new JButton("Two Player Mode");
        twoPlayerButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        twoPlayerButton.setBackground(new Color(178, 34, 34));
        twoPlayerButton.setForeground(Color.WHITE);
        twoPlayerButton.addActionListener(e -> {
            isSinglePlayer = false;
            modeFrame.dispose();
            askForPlayerNames();
            setupGUI();
        });
        modeFrame.add(twoPlayerButton);

        JLabel creditsLabel = new JLabel("Developed by Zafar, Amir, and Huzaifa", SwingConstants.CENTER);
        creditsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        creditsLabel.setForeground(new Color(200, 200, 200));
        modeFrame.add(creditsLabel);

        modeFrame.setLocationRelativeTo(null);
        modeFrame.setVisible(true);
    }

    private static void askForSinglePlayerName() {
        playerXName = JOptionPane.showInputDialog(frame, "Enter your name:");
        if (playerXName == null || playerXName.trim().isEmpty()) {
            playerXName = "Player X"; // Default name if none is provided
        }
        playerOName = "AI"; // Default AI name
    }

    private static void askForPlayerNames() {
        playerXName = JOptionPane.showInputDialog(frame, "Enter name for Player X:");
        if (playerXName == null || playerXName.trim().isEmpty()) {
            playerXName = "Player X"; // Default name if none is provided
        }

        playerOName = JOptionPane.showInputDialog(frame, "Enter name for Player O:");
        if (playerOName == null || playerOName.trim().isEmpty()) {
            playerOName = "Player O"; // Default name if none is provided
        }
    }

    public static void setupGUI() {
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(new Color(30, 30, 30));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Verdana", Font.BOLD, 60));
                buttons[i][j].setBackground(new Color(50, 50, 50));
                buttons[i][j].setForeground(new Color(200, 200, 200));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                boardPanel.add(buttons[i][j]);
            }
        }

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(45, 45, 45));

        statusLabel = new JLabel(playerXName + "'s Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        statusLabel.setForeground(new Color(255, 255, 255));
        topPanel.add(statusLabel, BorderLayout.NORTH);

        scoreLabel = new JLabel(playerXName + ": 0 | " + playerOName + ": 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        scoreLabel.setForeground(new Color(255, 255, 255));
        topPanel.add(scoreLabel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.setBackground(new Color(45, 45, 45));

        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        newGameButton.setBackground(new Color(70, 130, 180));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.addActionListener(e -> newGame());
        bottomPanel.add(newGameButton);

        JButton resetButton = new JButton("Reset Game");
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        resetButton.setBackground(new Color(178, 34, 34));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(e -> resetGame());
        bottomPanel.add(resetButton);

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        backButton.setBackground(new Color(50, 50, 50));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            frame.dispose();
            resetGame();
            showModeSelectionScreen();
        });
        bottomPanel.add(backButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setSize(600, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static class ButtonClickListener implements ActionListener {
        private int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameEnded || board[row][col] != ' ') {
                return;
            }

            board[row][col] = currentPlayer;
            buttons[row][col].setText(String.valueOf(currentPlayer));

            if (checkWinner()) {
                if (currentPlayer == 'X') {
                    scoreX++;
                } else {
                    scoreO++;
                }
                scoreLabel.setText(playerXName + ": " + scoreX + " | " + playerOName + ": " + scoreO);
                statusLabel.setText(currentPlayer == 'X' ? playerXName + " wins!" : playerOName + " wins!");
                gameEnded = true;
            } else if (isBoardFull()) {
                statusLabel.setText("The game is a draw!");
                gameEnded = true;
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                statusLabel.setText(currentPlayer == 'X' ? playerXName + "'s Turn" : playerOName + "'s Turn");

                if (isSinglePlayer && currentPlayer == 'O') {
                    makeAIMove();
                }
            }
        }
    }

    private static void makeAIMove() {
        Random random = new Random();
        int row, col;

        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (board[row][col] != ' ');

        board[row][col] = 'O';
        buttons[row][col].setText("O");

        if (checkWinner()) {
            scoreO++;
            scoreLabel.setText(playerXName + ": " + scoreX + " | " + playerOName + ": " + scoreO);
            statusLabel.setText(playerOName + " wins!");
            gameEnded = true;
        } else if (isBoardFull()) {
            statusLabel.setText("The game is a draw!");
            gameEnded = true;
        } else {
            currentPlayer = 'X';
            statusLabel.setText(playerXName + "'s Turn");
        }
    }

    public static boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer ||
                    board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                return true;
            }
        }

        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer ||
                board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            return true;
        }

        return false;
    }

    public static boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void newGame() {
        gameEnded = false;
        currentPlayer = 'X';
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
                buttons[i][j].setText(" ");
            }
        }
        statusLabel.setText(playerXName + "'s Turn");
    }

    public static void resetGame() {
        newGame();
        scoreX = 0;
        scoreO = 0;
        scoreLabel.setText(playerXName + ": 0 | " + playerOName + ": 0");
    }
}
