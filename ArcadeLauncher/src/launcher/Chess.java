package launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple 2-player chess.
 * - Basic legal moves enforced (no check/checkmate logic).
 * - No castling, no en passant.
 * - Pawns auto-promote to queen on last rank.
 * - First move: White, then Black.
 */
public class Chess extends JFrame implements Game, ActionListener {

    private static final int SIZE = 8;

    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private Piece[][] board = new Piece[SIZE][SIZE];

    private boolean whiteTurn = true;
    private int selectedRow = -1;
    private int selectedCol = -1;

    private JLabel statusLabel;

    // Colours
    private final Color darkSquare = new Color(80, 80, 80);
    private final Color lightSquare = new Color(200, 200, 200);
    private final Color selectedSquare = new Color(255, 235, 59);
    private final Color backgroundColour = new Color(20, 20, 20);
    private final Color uiPanelColour = new Color(30, 30, 30);
    private final Color textColour = new Color(230, 230, 230);

    public Chess() {
        setTitle("Chess");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.setBackground(backgroundColour);
        setContentPane(content);

        JLabel title = new JLabel("Chess", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(textColour);
        title.setBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 70))
        );
        content.add(title, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        boardPanel.setBackground(backgroundColour);

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JButton b = new JButton();
                b.setMargin(new Insets(0, 0, 0, 0));
                b.setFont(new Font("SansSerif", Font.PLAIN, 32));
                b.setFocusPainted(false);
                b.setOpaque(true);
                b.setBorderPainted(false);
                b.setActionCommand("square_" + r + "_" + c);
                b.addActionListener(this);
                buttons[r][c] = b;
                boardPanel.add(b);
            }
        }

        content.add(boardPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(uiPanelColour);
        bottomPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 70, 70)),
                        BorderFactory.createEmptyBorder(6, 8, 6, 8)
                )
        );

        statusLabel = new JLabel("Turn: White");
        statusLabel.setForeground(textColour);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        JButton resetButton = new JButton("Reset");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(this);
        resetButton.setBackground(new Color(60, 60, 60));
        resetButton.setForeground(textColour);
        resetButton.setFocusPainted(false);
        bottomPanel.add(resetButton, BorderLayout.EAST);

        content.add(bottomPanel, BorderLayout.SOUTH);

        initBoard();
        updateBoardUI();

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    // ===== Game interface =====

    @Override
    public String getName() {
        return "Chess";
    }

    @Override
    public void start(JFrame parentFrame) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }

    // ===== Board / pieces =====

    private enum PieceType {
        KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
    }

    private static class Piece {
        PieceType type;
        boolean white;

        Piece(PieceType type, boolean white) {
            this.type = type;
            this.white = white;
        }
    }

    private void initBoard() {
        board = new Piece[SIZE][SIZE];
        whiteTurn = true;
        selectedRow = -1;
        selectedCol = -1;

        // Back ranks: White bottom (7), Black top (0)
        board[7][0] = new Piece(PieceType.ROOK, true);
        board[7][7] = new Piece(PieceType.ROOK, true);
        board[0][0] = new Piece(PieceType.ROOK, false);
        board[0][7] = new Piece(PieceType.ROOK, false);

        board[7][1] = new Piece(PieceType.KNIGHT, true);
        board[7][6] = new Piece(PieceType.KNIGHT, true);
        board[0][1] = new Piece(PieceType.KNIGHT, false);
        board[0][6] = new Piece(PieceType.KNIGHT, false);

        board[7][2] = new Piece(PieceType.BISHOP, true);
        board[7][5] = new Piece(PieceType.BISHOP, true);
        board[0][2] = new Piece(PieceType.BISHOP, false);
        board[0][5] = new Piece(PieceType.BISHOP, false);

        board[7][3] = new Piece(PieceType.QUEEN, true);
        board[0][3] = new Piece(PieceType.QUEEN, false);

        board[7][4] = new Piece(PieceType.KING, true);
        board[0][4] = new Piece(PieceType.KING, false);

        // Pawns
        for (int c = 0; c < SIZE; c++) {
            board[6][c] = new Piece(PieceType.PAWN, true);
            board[1][c] = new Piece(PieceType.PAWN, false);
        }

        statusLabel.setText("Turn: White");
    }

    private void updateBoardUI() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JButton b = buttons[r][c];
                Piece p = board[r][c];

                // Background
                if ((r + c) % 2 == 0) {
                    b.setBackground(lightSquare);
                } else {
                    b.setBackground(darkSquare);
                }

                // Selection highlight
                if (r == selectedRow && c == selectedCol) {
                    b.setBackground(selectedSquare);
                }

                // Piece
                if (p == null) {
                    b.setText("");
                } else {
                    b.setText(pieceToUnicode(p));
                }
            }
        }
    }

    private String pieceToUnicode(Piece p) {
        if (p.type == PieceType.KING) {
            return p.white ? "♔" : "♚";
        } else if (p.type == PieceType.QUEEN) {
            return p.white ? "♕" : "♛";
        } else if (p.type == PieceType.ROOK) {
            return p.white ? "♖" : "♜";
        } else if (p.type == PieceType.BISHOP) {
            return p.white ? "♗" : "♝";
        } else if (p.type == PieceType.KNIGHT) {
            return p.white ? "♘" : "♞";
        } else { // PAWN
            return p.white ? "♙" : "♟";
        }
    }

    // ===== Input handling =====

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if ("reset".equals(cmd)) {
            initBoard();
            updateBoardUI();
            return;
        }

        if (cmd.startsWith("square_")) {
            String[] parts = cmd.split("_");
            int r = Integer.parseInt(parts[1]);
            int c = Integer.parseInt(parts[2]);
            handleSquareClick(r, c);
        }
    }

    private void handleSquareClick(int row, int col) {
        Piece clicked = board[row][col];

        // No selection yet
        if (selectedRow == -1) {
            if (clicked == null) return;            // empty square
            if (clicked.white != whiteTurn) return; // not your piece
            selectedRow = row;
            selectedCol = col;
            updateBoardUI();
            return;
        }

        // We already have something selected
        Piece selectedPiece = board[selectedRow][selectedCol];

        // Clicked own piece again → change selection
        if (clicked != null && clicked.white == selectedPiece.white &&
                !(row == selectedRow && col == selectedCol)) {
            selectedRow = row;
            selectedCol = col;
            updateBoardUI();
            return;
        }

        // Try to move
        if (tryMove(selectedRow, selectedCol, row, col)) {
            selectedRow = -1;
            selectedCol = -1;
            whiteTurn = !whiteTurn;
            statusLabel.setText(whiteTurn ? "Turn: White" : "Turn: Black");
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Illegal move for that piece.",
                    "Invalid Move",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        updateBoardUI();
    }

    private boolean tryMove(int sr, int sc, int dr, int dc) {
        if (sr == dr && sc == dc) return false;

        Piece p = board[sr][sc];
        if (p == null) return false;

        Piece dest = board[dr][dc];
        if (dest != null && dest.white == p.white) return false;

        if (!isValidMove(p, sr, sc, dr, dc)) return false;

        // Capture king = win + reset
        if (dest != null && dest.type == PieceType.KING) {
            board[dr][dc] = p;
            board[sr][sc] = null;
            updateBoardUI();
            JOptionPane.showMessageDialog(
                    this,
                    (p.white ? "White" : "Black") + " wins! King captured.",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE
            );
            initBoard();
            return true;
        }

        board[dr][dc] = p;
        board[sr][sc] = null;

        // Pawn promotion
        if (p.type == PieceType.PAWN) {
            if (p.white && dr == 0) {
                board[dr][dc] = new Piece(PieceType.QUEEN, true);
            } else if (!p.white && dr == 7) {
                board[dr][dc] = new Piece(PieceType.QUEEN, false);
            }
        }

        return true;
    }

    private boolean isValidMove(Piece p, int sr, int sc, int dr, int dc) {
        int drDiff = dr - sr;
        int dcDiff = dc - sc;
        int absDr = Math.abs(drDiff);
        int absDc = Math.abs(dcDiff);

        if (p.type == PieceType.ROOK) {
            if (sr != dr && sc != dc) return false;
            return pathClear(sr, sc, dr, dc);
        } else if (p.type == PieceType.BISHOP) {
            if (absDr != absDc) return false;
            return pathClear(sr, sc, dr, dc);
        } else if (p.type == PieceType.QUEEN) {
            if (!(sr == dr || sc == dc || absDr == absDc)) return false;
            return pathClear(sr, sc, dr, dc);
        } else if (p.type == PieceType.KNIGHT) {
            return (absDr == 2 && absDc == 1) || (absDr == 1 && absDc == 2);
        } else if (p.type == PieceType.KING) {
            return absDr <= 1 && absDc <= 1;
        } else if (p.type == PieceType.PAWN) {
            return isValidPawnMove(p, sr, sc, dr, dc);
        }
        return false;
    }

    private boolean isValidPawnMove(Piece p, int sr, int sc, int dr, int dc) {
        int dir = p.white ? -1 : 1;        // white moves up, black down
        int startRow = p.white ? 6 : 1;

        Piece dest = board[dr][dc];
        int drDiff = dr - sr;
        int dcDiff = dc - sc;

        // Forward (no capture)
        if (dcDiff == 0) {
            if (drDiff == dir && dest == null) {
                return true;
            }
            if (sr == startRow && drDiff == 2 * dir && dest == null) {
                int midRow = sr + dir;
                return board[midRow][sc] == null;
            }
            return false;
        }

        // Diagonal capture
        if (Math.abs(dcDiff) == 1 && drDiff == dir) {
            return dest != null && dest.white != p.white;
        }

        return false;
    }

    private boolean pathClear(int sr, int sc, int dr, int dc) {
        int stepR = Integer.compare(dr, sr);
        int stepC = Integer.compare(dc, sc);
        int r = sr + stepR;
        int c = sc + stepC;

        while (r != dr || c != dc) {
            if (board[r][c] != null) return false;
            r += stepR;
            c += stepC;
        }
        return true;
    }

    // Standalone test (you can run Chess by itself)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Chess c = new Chess();
                c.setVisible(true);
            }
        });
    }
}
