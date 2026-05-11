package launcher;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.Collections;
import java.util.Random;

public class Sudoku extends JFrame implements Game, ActionListener {

    private static final long serialVersionUID = 1L;

    public static final int SIZE = 9;
    public static final int EMPTY = 0;

    // 9x9 grid of text fields the player can type into
    private JTextField[][] cellFields = new JTextField[SIZE][SIZE];

    private JComboBox<String> difficultyBox;
    private JButton newPuzzleButton;
    private JButton checkButton;          // <<< NEW
    private JButton showSolutionButton;

    // Current boards
    private int[][] fullSolution;
    private int[][] puzzleBoard;

    public Sudoku() {
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        root.setBackground(new Color(25, 25, 25));
        setContentPane(root);

        // ===== Top: title + difficulty =====
        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.setOpaque(false);

        JLabel title = new JLabel("Sudoku", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(240, 240, 240));
        topPanel.add(title, BorderLayout.NORTH);

        JPanel diffPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        diffPanel.setOpaque(false);

        JLabel diffLabel = new JLabel("Difficulty:");
        diffLabel.setForeground(new Color(180, 180, 180));
        diffLabel.setFont(new Font("Arial", Font.BOLD, 13));

        diffPanel.add(diffLabel);
        String[] diffs = {"Easy", "Medium", "Hard"};
        difficultyBox = new JComboBox<>(diffs);
        diffPanel.add(difficultyBox);

        topPanel.add(diffPanel, BorderLayout.SOUTH);
        root.add(topPanel, BorderLayout.NORTH);

        // ===== Center: 9x9 grid you can click/type =====
        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        gridPanel.setBackground(new Color(20, 20, 20));

        Font cellFont = new Font(Font.MONOSPACED, Font.BOLD, 20);

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                final int rr = r;
                final int cc = c;

                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(cellFont);
                tf.setBackground(new Color(35, 35, 35));
                tf.setForeground(new Color(220, 220, 220));
                tf.setCaretColor(Color.WHITE);

                // Borders so 3x3 blocks are visible
                int top = (r % 3 == 0) ? 2 : 1;
                int left = (c % 3 == 0) ? 2 : 1;
                int bottom = (r == SIZE - 1) ? 2 : ((r % 3 == 2) ? 2 : 1);
                int right = (c == SIZE - 1) ? 2 : ((c % 3 == 2) ? 2 : 1);
                tf.setBorder(BorderFactory.createMatteBorder(
                        top, left, bottom, right,
                        new Color(70, 70, 70)
                ));

                // Only allow digits 1–9 or empty, and update puzzleBoard
                tf.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        String text = tf.getText().trim();
                        if (text.isEmpty()) {
                            tf.setText("");
                            if (puzzleBoard != null) puzzleBoard[rr][cc] = EMPTY;
                            tf.setBackground(new Color(35, 35, 35)); // reset bg
                            return;
                        }
                        if (text.length() == 1 && text.charAt(0) >= '1' && text.charAt(0) <= '9') {
                            if (puzzleBoard != null) puzzleBoard[rr][cc] = text.charAt(0) - '0';
                            tf.setBackground(new Color(35, 35, 35)); // reset bg
                        } else {
                            // Invalid input → clear
                            tf.setText("");
                            if (puzzleBoard != null) puzzleBoard[rr][cc] = EMPTY;
                            tf.setBackground(new Color(35, 35, 35));
                        }
                    }
                });

                cellFields[r][c] = tf;
                gridPanel.add(tf);
            }
        }

        root.add(gridPanel, BorderLayout.CENTER);

        // ===== Bottom: buttons =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        bottomPanel.setOpaque(false);

        newPuzzleButton = new JButton("New Puzzle");
        newPuzzleButton.setActionCommand("newPuzzle");
        newPuzzleButton.addActionListener(this);
        styleButton(newPuzzleButton);
        bottomPanel.add(newPuzzleButton);

        // NEW "Check" button
        checkButton = new JButton("Check");
        checkButton.setActionCommand("check");
        checkButton.addActionListener(this);
        styleButton(checkButton);
        bottomPanel.add(checkButton);

        showSolutionButton = new JButton("Show Solution");
        showSolutionButton.setActionCommand("showSolution");
        showSolutionButton.addActionListener(this);
        styleButton(showSolutionButton);
        bottomPanel.add(showSolutionButton);

        root.add(bottomPanel, BorderLayout.SOUTH);

        // Generate first puzzle
        generateNewPuzzle();

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(new Color(240, 240, 240));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    // ===== Game interface implementation =====

    @Override
    public String getName() {
        return "Sudoku";
    }

    @Override
    public void start(JFrame parentFrame) {
        if (parentFrame != null) setLocationRelativeTo(parentFrame);
        else setLocationRelativeTo(null);
        setVisible(true);
    }

    // ===== Button actions =====

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "newPuzzle" -> generateNewPuzzle();
            case "showSolution" -> showSolution();
            case "check"      -> checkBoard();   // <<< NEW
        }
    }

    private void generateNewPuzzle() {
        String diff = ((String) difficultyBox.getSelectedItem()).toLowerCase();
        int removals = difficultyToRemovals(diff);

        fullSolution = Generator.generateFullBoard();
        puzzleBoard = Generator.makePuzzle(fullSolution, removals);

        updateGridFromBoard();
    }

    private void showSolution() {
        if (fullSolution == null) {
            JOptionPane.showMessageDialog(
                    this, "No puzzle generated yet.",
                    "Sudoku",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // Show the solved board directly on the grid
        puzzleBoard = deepCopy(fullSolution);
        updateGridFromBoard();
        JOptionPane.showMessageDialog(
                this, "Solution filled in.",
                "Sudoku",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // <<< NEW: check current board against solution >>>
    private void checkBoard() {
        if (fullSolution == null || puzzleBoard == null) {
            JOptionPane.showMessageDialog(
                    this, "No puzzle loaded.",
                    "Sudoku",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean anyWrong = false;
        boolean anyEmpty = false;

        Color clueColor = new Color(200, 200, 200);
        Color userColor = new Color(140, 220, 255);
        Color wrongColor = new Color(160, 60, 60); // dark red
        Color emptyColor = new Color(60, 60, 60);

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JTextField tf = cellFields[r][c];

                // If it's a clue (non-editable), don't mark as wrong
                if (!tf.isEditable()) {
                    tf.setBackground(new Color(35, 35, 35));
                    tf.setForeground(clueColor);
                    continue;
                }

                int val = puzzleBoard[r][c];

                if (val == EMPTY) {
                    anyEmpty = true;
                    tf.setBackground(emptyColor);
                    tf.setForeground(userColor);
                } else if (val != fullSolution[r][c]) {
                    anyWrong = true;
                    tf.setBackground(wrongColor);
                    tf.setForeground(Color.WHITE);
                } else {
                    // Correct so far
                    tf.setBackground(new Color(35, 35, 35));
                    tf.setForeground(userColor);
                }
            }
        }

        if (anyWrong) {
            JOptionPane.showMessageDialog(
                    this,
                    "There are some mistakes (red cells).",
                    "Check Result",
                    JOptionPane.WARNING_MESSAGE
            );
        } else if (anyEmpty) {
            JOptionPane.showMessageDialog(
                    this,
                    "Everything you've filled so far is correct!\nKeep going to finish the puzzle.",
                    "Check Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Perfect! You solved the Sudoku 🎉",
                    "Check Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // ===== Helpers =====

    static int difficultyToRemovals(String diff) {
        return switch (diff) {
            case "easy" -> 36;  // fewer removals → easier
            case "hard" -> 54;  // more removals → harder
            default -> 46;      // medium
        };
    }

    private void updateGridFromBoard() {
        if (puzzleBoard == null) return;

        // Colors: grey for clues, cyan for user
        Color clueColor = new Color(200, 200, 200);
        Color userColor = new Color(140, 220, 255);
        Color baseBg = new Color(35, 35, 35);

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JTextField tf = cellFields[r][c];
                int v = puzzleBoard[r][c];

                if (v == EMPTY) {
                    tf.setText("");
                    tf.setEditable(true);
                    tf.setForeground(userColor);
                    tf.setBackground(baseBg);
                } else {
                    tf.setText(String.valueOf(v));
                    tf.setEditable(false);      // given clue or solution
                    tf.setForeground(clueColor);
                    tf.setBackground(baseBg);
                }
            }
        }
    }

    public static int[][] deepCopy(int[][] src) {
        int[][] dst = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) System.arraycopy(src[i], 0, dst[i], 0, SIZE);
        return dst;
    }

    // ===== Generator: builds a full valid board then removes cells =====
    static class Generator {
        private static final Random rand = new Random();

        public static int[][] generateFullBoard() {
            int[][] board = new int[SIZE][SIZE];
            fillDiagonalBlocks(board);
            Solver.solve(board);
            return board;
        }

        private static void fillDiagonalBlocks(int[][] board) {
            for (int block = 0; block < SIZE; block += 3) {
                java.util.List<Integer> nums = new java.util.ArrayList<>();
                for (int n = 1; n <= SIZE; n++) nums.add(n);
                Collections.shuffle(nums, rand);
                int idx = 0;
                for (int r = 0; r < 3; r++)
                    for (int c = 0; c < 3; c++)
                        board[block + r][block + c] = nums.get(idx++);
            }
        }

        public static int[][] makePuzzle(int[][] full, int removals) {
            int[][] puzzle = deepCopy(full);
            java.util.List<int[]> cells = new java.util.ArrayList<>();
            for (int r = 0; r < SIZE; r++)
                for (int c = 0; c < SIZE; c++)
                    cells.add(new int[]{r, c});

            Collections.shuffle(cells, rand);

            int removed = 0;
            for (int[] rc : cells) {
                if (removed >= removals) break;
                puzzle[rc[0]][rc[1]] = EMPTY;
                removed++;
            }
            return puzzle;
        }
    }

    // ===== Backtracking solver =====
    static class Solver {
        public static boolean solve(int[][] board) {
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (board[r][c] == EMPTY) {
                        for (int val = 1; val <= SIZE; val++) {
                            if (isSafe(board, r, c, val)) {
                                board[r][c] = val;
                                if (solve(board)) return true;
                                board[r][c] = EMPTY;
                            }
                        }
                        return false;
                    }
                }
            }
            return true;
        }

        private static boolean isSafe(int[][] board, int row, int col, int val) {
            // Row & column
            for (int i = 0; i < SIZE; i++) {
                if (board[row][i] == val) return false;
                if (board[i][col] == val) return false;
            }

            int br = (row / 3) * 3;
            int bc = (col / 3) * 3;

            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++)
                    if (board[br + r][bc + c] == val) return false;

            return true;
        }
    }
}
