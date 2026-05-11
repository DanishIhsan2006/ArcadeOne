package launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameLauncherUI extends JFrame implements ActionListener {

    private final GameLauncher gameLauncher;
    private final List<Game> games;

    private final JList<String> gameList;
    private final JTextArea gameInfoArea;
    private final JButton playButton;
    private final JButton exitButton;

    // Colours (matching the Tic-Tac-Toe vibe)
    private final Color backgroundColour = new Color(20, 20, 20);
    private final Color panelColour = new Color(30, 30, 30);
    private final Color highlightColour = new Color(79, 195, 247);
    private final Color accentColour = new Color(255, 138, 128);
    private final Color textColour = new Color(230, 230, 230);

    public GameLauncherUI() {
        this.gameLauncher = new GameLauncher();
        this.games = gameLauncher.getGames();

        setTitle("ArcadeOne - Game Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.setBackground(backgroundColour);
        setContentPane(content);

        // ===== Title =====
        JLabel titleLabel = new JLabel("ArcadeOne", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(textColour);
        titleLabel.setBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 70))
        );
        content.add(titleLabel, BorderLayout.NORTH);

        // ===== Center area: game list + info =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBackground(backgroundColour);

        // Left: game list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Game g : games) {
            listModel.addElement(g.getName());
        }

        gameList = new JList<>(listModel);
        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameList.setSelectedIndex(0);
        gameList.setBackground(panelColour);
        gameList.setForeground(textColour);
        gameList.setFont(new Font("Arial", Font.PLAIN, 16));
        gameList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                "Games",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                textColour
        ));
        gameList.addListSelectionListener(e -> updateGameInfo());

        JScrollPane listScroll = new JScrollPane(gameList);
        listScroll.getViewport().setBackground(panelColour);

        // Right: game info
        gameInfoArea = new JTextArea();
        gameInfoArea.setEditable(false);
        gameInfoArea.setBackground(panelColour);
        gameInfoArea.setForeground(textColour);
        gameInfoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        gameInfoArea.setLineWrap(true);
        gameInfoArea.setWrapStyleWord(true);
        gameInfoArea.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                "Game Info",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                textColour
        ));

        JScrollPane infoScroll = new JScrollPane(gameInfoArea);
        infoScroll.getViewport().setBackground(panelColour);

        centerPanel.add(listScroll);
        centerPanel.add(infoScroll);

        content.add(centerPanel, BorderLayout.CENTER);

        // ===== Bottom buttons =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        bottomPanel.setBackground(backgroundColour);

        playButton = new JButton("Play");
        playButton.setActionCommand("play");
        playButton.addActionListener(this);
        styleMainButton(playButton);

        exitButton = new JButton("Exit");
        exitButton.setActionCommand("exit");
        exitButton.addActionListener(this);
        styleSecondaryButton(exitButton);

        bottomPanel.add(playButton);
        bottomPanel.add(exitButton);

        content.add(bottomPanel, BorderLayout.SOUTH);

        // Initialize info
        updateGameInfo();

        setSize(650, 400);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void styleMainButton(JButton button) {
        button.setBackground(highlightColour);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 120)));
        button.setPreferredSize(new Dimension(100, 32));
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(textColour);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 120)));
        button.setPreferredSize(new Dimension(100, 32));
    }

    private void updateGameInfo() {
        int index = gameList.getSelectedIndex();
        if (index < 0 || index >= games.size()) {
            gameInfoArea.setText("Select a game to see details.");
            return;
        }

        String name = games.get(index).getName();
        String description;

        switch (name.toLowerCase()) {
            case "tic-tac-toe":
                description =
                        "Classic 3×3 Tic-Tac-Toe.\n\n" +
                        "Two players take turns placing X and O.\n" +
                        "Get three in a row to win.";
                break;

            case "chess":
                description =
                        "Chess: a strategic board game of kings, queens, and tactics.\n\n" +
                        "Control the board, capture pieces, and take the enemy king.";
                break;

            case "sudoku":
                description =
                        "Sudoku: a number-placement puzzle.\n\n" +
                        "Fill the grid so each row, column, and 3×3 box contains the digits 1–9.";
                break;

            case "dominos":
                description =
                        "Dominos: match tiles and outplay your opponent.\n\n" +
                        "Place tiles with matching values and control the board.";
                break;

            case "checkers":
                description =
                        "Checkers: a simple but tactical board game.\n\n" +
                        "Jump over opponent pieces to capture them and try to king your own.";
                break;

            case "samuraiduel":
                description =
                        "Samurai Duel: 1 v 1 text-based samurai fight.\n\n" +
                        "Choose your attacks wisely and reduce your opponent’s HP to 0.";
                break;

            case "rock paper scissors":
                description =
                        "Rock-Paper-Scissors: quick reaction game.\n\n" +
                        "Rock beats Scissors, Scissors beats Paper, Paper beats Rock.";
                break;

            case "hangman":
                description =
                        "Hangman: classic word guessing game.\n\n" +
                        "Guess the word one letter at a time before the man is fully drawn.";
                break;

            case "cookie clicker":
                description =
                        "Cookie Clicker: incremental clicker game.\n\n" +
                        "Click to earn cookies and buy upgrades to increase cookies per click.";
                break;

            case "minesweeper":
                description =
                        "Minesweeper: classic logic puzzle.\n\n" +
                        "Reveal safe tiles, mark mines with flags, and avoid exploding on a mine!";
                break;
                
            case "MiniCasinoGame":
                description =
                        "MiniCasino:Classic Casino Game.\n\n" +
                        "Beat the dealer in Blakck jack or try your luck in Roulette.";
         
                break;

            default:
                description =
                        "Launch and play " + name + ".\n\n" +
                        "More details coming soon.";
                break;
        }

        gameInfoArea.setText(description);
        gameInfoArea.setCaretPosition(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "play":
                int index = gameList.getSelectedIndex();
                if (index < 0 || index >= games.size()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Please select a game first.",
                            "No Game Selected",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                Game selected = games.get(index);
                selected.start(this);
                break;

            case "exit":
                dispose();
                System.exit(0);
                break;
        }
    }

    // Main entry point to run the launcher
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameLauncherUI ui = new GameLauncherUI();
            ui.setVisible(true);
        });
    }
}
