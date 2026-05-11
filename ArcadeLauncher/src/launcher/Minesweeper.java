package launcher;

import javax.swing.JFrame;
import java.util.*;

public class Minesweeper implements Game {
    private final int rows;
    private final int cols;
    private final int mines;
    private final Cell[][] board;
    private boolean lost = false;
    private boolean won = false;
    private int cellsToReveal;

    private static class Cell {
        boolean mine = false;
        boolean revealed = false;
        boolean flagged = false;
        int neighborMines = 0;
    }

    // Default constructor for launcher
    public Minesweeper() {
        this(9, 9, 10);
    }

    public Minesweeper(int rows, int cols, int mines) {
        if (mines >= rows * cols) throw new IllegalArgumentException("Too many mines");
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.board = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = new Cell();
            }
        }
        this.cellsToReveal = rows * cols - mines;
        placeMines();
        computeNeighbors();
    }

    private void placeMines() {
        Random rand = new Random();
        int placed = 0;
        while (placed < mines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (!board[r][c].mine) {
                board[r][c].mine = true;
                placed++;
            }
        }
    }

    private void computeNeighbors() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c].mine) continue;
                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        int nr = r + dr, nc = c + dc;
                        if (inBounds(nr, nc) && board[nr][nc].mine) count++;
                    }
                }
                board[r][c].neighborMines = count;
            }
        }
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public void reveal(int r, int c) {
        if (!inBounds(r, c) || board[r][c].revealed || board[r][c].flagged || lost || won) return;
        if (board[r][c].mine) {
            board[r][c].revealed = true;
            lost = true;
            revealAllMines();
            return;
        }
        floodReveal(r, c);
        if (cellsToReveal == 0) won = true;
    }

    private void revealAllMines() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c].mine) board[r][c].revealed = true;
            }
        }
    }

    private void floodReveal(int r, int c) {
        Queue<int[]> q = new ArrayDeque<>();
        if (!board[r][c].revealed && !board[r][c].flagged) {
            q.add(new int[]{r, c});
        }
        while (!q.isEmpty()) {
            int[] cur = q.remove();
            int cr = cur[0], cc = cur[1];
            Cell cell = board[cr][cc];
            if (cell.revealed || cell.flagged) continue;
            cell.revealed = true;
            cellsToReveal--;
            if (cell.neighborMines == 0) {
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        int nr = cr + dr, nc = cc + dc;
                        if (inBounds(nr, nc) && !board[nr][nc].revealed && !board[nr][nc].flagged) {
                            q.add(new int[]{nr, nc});
                        }
                    }
                }
            }
        }
    }

    public void toggleFlag(int r, int c) {
        if (!inBounds(r, c) || board[r][c].revealed || lost || won) return;
        board[r][c].flagged = !board[r][c].flagged;
    }

    public void printBoard(boolean revealAll) {
        System.out.print("    ");
        for (int c = 0; c < cols; c++) System.out.printf("%3d", c);
        System.out.println();
        System.out.print("    ");
        for (int c = 0; c < cols; c++) System.out.print("---");
        System.out.println();
        for (int r = 0; r < rows; r++) {
            System.out.printf("%3d|", r);
            for (int c = 0; c < cols; c++) {
                Cell cell = board[r][c];
                String s;
                if (revealAll) {
                    if (cell.mine) s = "*";
                    else if (cell.neighborMines > 0) s = Integer.toString(cell.neighborMines);
                    else s = " ";
                } else {
                    if (cell.revealed) {
                        if (cell.mine) s = "*";
                        else if (cell.neighborMines > 0) s = Integer.toString(cell.neighborMines);
                        else s = " ";
                    } else if (cell.flagged) {
                        s = "F";
                    } else {
                        s = "#";
                    }
                }
                System.out.printf("%3s", s);
            }
            System.out.println();
        }
    }

    public boolean isLost() { return lost; }
    public boolean isWon() { return won; }

    // ===== Game interface methods =====
    @Override
    public String getName() {
        return "Minesweeper";
    }

    @Override
    public void start(JFrame parentFrame) {
        // Just run the console version
        main(new String[0]);
    }

    // Console main entry
    public static void main(String[] args) {
        int rows = 9, cols = 9, mines = 10;
        try {
            if (args.length >= 3) {
                rows = Integer.parseInt(args[0]);
                cols = Integer.parseInt(args[1]);
                mines = Integer.parseInt(args[2]);
            }
        } catch (Exception e) {
            System.out.println("Invalid args. Usage: java Minesweeper [rows cols mines]");
            return;
        }

        Minesweeper game = new Minesweeper(rows, cols, mines);
        Scanner in = new Scanner(System.in);

        System.out.println("Welcome to Minesweeper (console)");
        System.out.println("Commands: r row col  -> reveal | f row col -> flag/unflag | q -> quit");

        while (true) {
            game.printBoard(false);
            if (game.isLost()) {
                System.out.println("BOOM! You hit a mine. Game over.");
                game.printBoard(true);
                break;
            }
            if (game.isWon()) {
                System.out.println("Congratulations! You cleared the board!");
                game.printBoard(true);
                break;
            }
            System.out.print("Enter command: ");
            String line = in.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            String cmd = parts[0].toLowerCase();
            if (cmd.equals("q")) {
                System.out.println("Quitting. Bye!");
                break;
            } else if ((cmd.equals("r") || cmd.equals("f")) && parts.length >= 3) {
                try {
                    int r = Integer.parseInt(parts[1]);
                    int c = Integer.parseInt(parts[2]);
                    if (!game.inBounds(r, c)) {
                        System.out.println("Coordinates out of bounds.");
                        continue;
                    }
                    if (cmd.equals("r")) game.reveal(r, c);
                    else game.toggleFlag(r, c);
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid coordinates. Use integers.");
                }
            } else {
                System.out.println("Unknown command. Use r/f row col or q to quit.");
            }
        }
        in.close();
    }
}
