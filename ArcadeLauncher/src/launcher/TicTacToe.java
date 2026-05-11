package launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame implements Game, ActionListener {

    // Game screen
    private JLabel turnLabel;
    private JButton a, b, c, d, ee, f, g, h, i;
    private char[][] board = {
            { 'b', 'b', 'b' },
            { 'b', 'b', 'b' },
            { 'b', 'b', 'b' }
    };
    private char turn = 'x';

    // Gamer theme colors
    private final Color backgroundColour = new Color(30, 30, 30);
    private final Color boardButtonColour = new Color(45, 45, 45);
    private final Color uiPanelColour = new Color(25, 25, 25);
    private final Color controlButtonColour = new Color(60, 60, 60);
    private final Color controlButtonTextColour = new Color(230, 230, 230);

    private final Color titleColour = new Color(240, 240, 240);
    private final Color labelColour = new Color(210, 210, 210);

    // Neon X and O
    private final Color xColor = new Color(79, 195, 247);
    private final Color oColor = new Color(255, 138, 128);

    // Fonts
    private final Font titleFont = new Font("Arial", Font.BOLD, 28);
    private final Font promptFont = new Font("Arial", Font.PLAIN, 18);
    private final Font buttonFont = new Font("Arial", Font.BOLD, 32);

    // Button size
    private final Dimension boardSquare = new Dimension(80, 80);

    public TicTacToe() {
        setTitle("Tic-Tac-Toe");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.setBackground(backgroundColour);
        setContentPane(content);

        JLabel title = new JLabel("Tic-Tac-Toe", SwingConstants.CENTER);
        title.setFont(titleFont);
        title.setForeground(titleColour);
        title.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 70)));
        content.add(title, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        boardPanel.setBackground(backgroundColour);

        a = createBoardButton("a");
        boardPanel.add(a);
        b = createBoardButton("b");
        boardPanel.add(b);
        c = createBoardButton("c");
        boardPanel.add(c);

        d = createBoardButton("d");
        boardPanel.add(d);
        ee = createBoardButton("ee");
        boardPanel.add(ee);
        f = createBoardButton("f");
        boardPanel.add(f);

        g = createBoardButton("g");
        boardPanel.add(g);
        h = createBoardButton("h");
        boardPanel.add(h);
        i = createBoardButton("i");
        boardPanel.add(i);

        content.add(boardPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setBackground(uiPanelColour);
        bottomPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 70, 70)),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                )
        );

        JPanel turnPanel = new JPanel();
        turnPanel.setBackground(uiPanelColour);
        turnLabel = new JLabel("Current Turn: X", SwingConstants.CENTER);
        turnLabel.setFont(promptFont);
        turnLabel.setForeground(labelColour);
        turnPanel.add(turnLabel);
        bottomPanel.add(turnPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controlPanel.setBackground(uiPanelColour);

        JButton reset = createControlButton("Again", "reset");
        controlPanel.add(reset);

        JButton instruct = createControlButton("Instructions", "instruct");
        controlPanel.add(instruct);

        JButton creator = createControlButton("Creator", "creator");
        controlPanel.add(creator);

        bottomPanel.add(controlPanel, BorderLayout.SOUTH);
        content.add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private JButton createBoardButton(String command) {
        JButton btn = new JButton("");
        btn.setActionCommand(command);
        btn.setBackground(boardButtonColour);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        btn.setPreferredSize(boardSquare);
        btn.setFont(buttonFont);
        btn.setForeground(xColor);
        btn.addActionListener(this);
        return btn;
    }

    private JButton createControlButton(String text, String command) {
        JButton btn = new JButton(text);
        btn.setActionCommand(command);
        btn.setPreferredSize(new Dimension(130, 32));
        btn.setBackground(controlButtonColour);
        btn.setForeground(controlButtonTextColour);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "reset" -> resetBoard();

            case "creator" -> JOptionPane.showMessageDialog(
                    this,
                    "ArcadeOne Tic-Tac-Toe\nDesigned by our project team.",
                    "Creator",
                    JOptionPane.INFORMATION_MESSAGE
            );

            case "instruct" -> JOptionPane.showMessageDialog(
                    this,
                    "How to Play:\n\n"
                            + "• Check whose turn it is at the bottom.\n"
                            + "• Click a square to place your piece.\n"
                            + "• Get three in a row (row, column, or diagonal).\n"
                            + "• Block your opponent from doing the same!",
                    "Instructions",
                    JOptionPane.INFORMATION_MESSAGE
            );

            case "a" -> handleMove(0, 0, a);
            case "b" -> handleMove(0, 1, b);
            case "c" -> handleMove(0, 2, c);
            case "d" -> handleMove(1, 0, d);
            case "ee" -> handleMove(1, 1, ee);
            case "f" -> handleMove(1, 2, f);
            case "g" -> handleMove(2, 0, g);
            case "h" -> handleMove(2, 1, h);
            case "i" -> handleMove(2, 2, i);
        }
    }

    private void handleMove(int row, int col, JButton button) {
        if (board[row][col] == 'b') {
            board[row][col] = turn;

            if (turn == 'x') {
                button.setText("X");
                button.setForeground(xColor);
            } else {
                button.setText("O");
                button.setForeground(oColor);
            }

            if (checkWinner()) {
                return;
            }

            if (isDraw()) {
                JOptionPane.showMessageDialog(
                        this,
                        "It's a draw!",
                        "Draw",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            if (turn == 'x') {
                turn = 'o';
                turnLabel.setText("Current Turn: O");
            } else {
                turn = 'x';
                turnLabel.setText("Current Turn: X");
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "That square is already taken.",
                    "Invalid Move",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private boolean checkWinner() {
        // Rows
        for (int r = 0; r < 3; r++) {
            if (board[r][0] != 'b' &&
                    board[r][0] == board[r][1] &&
                    board[r][1] == board[r][2]) {
                showWinner(board[r][0]);
                return true;
            }
        }
        // Columns
        for (int c = 0; c < 3; c++) {
            if (board[0][c] != 'b' &&
                    board[0][c] == board[1][c] &&
                    board[1][c] == board[2][c]) {
                showWinner(board[0][c]);
                return true;
            }
        }
        // Diagonals
        if (board[0][0] != 'b' &&
                board[0][0] == board[1][1] &&
                board[1][1] == board[2][2]) {
            showWinner(board[0][0]);
            return true;
        }
        if (board[0][2] != 'b' &&
                board[0][2] == board[1][1] &&
                board[1][1] == board[2][0]) {
            showWinner(board[0][2]);
            return true;
        }

        return false;
    }

    private boolean isDraw() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == 'b') {
                    return false;
                }
            }
        }
        return true;
    }

    private void showWinner(char winner) {
        String player = (winner == 'x') ? "X" : "O";
        JOptionPane.showMessageDialog(
                this,
                "Player " + player + " has won!",
                "Winner",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void resetBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c] = 'b';
            }
        }

        a.setText("");
        b.setText("");
        c.setText("");
        d.setText("");
        ee.setText("");
        f.setText("");
        g.setText("");
        h.setText("");
        i.setText("");

        turn = 'x';
        turnLabel.setText("Current Turn: X");
    }

    // ===== Game interface implementation =====
    @Override
    public String getName() {
        return "Tic-Tac-Toe";
    }

    @Override
    public void start(JFrame parentFrame) {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    // Keep main ONLY for standalone testing (optional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicTacToe app = new TicTacToe();
            app.setVisible(true);
        });
    }
}
