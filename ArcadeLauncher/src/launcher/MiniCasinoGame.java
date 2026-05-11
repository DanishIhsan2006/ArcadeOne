package launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Mini Casino GUI – provides Blackjack and Roulette
 * as a single Game usable from the ArcadeOne launcher.
 */
public class MiniCasinoGame extends JFrame implements Game, ActionListener {

    private static final long serialVersionUID = 1L;

    // UI components
    private final JTextArea outputArea;

    private final JButton bjNewRoundButton;
    private final JButton bjHitButton;
    private final JButton bjStandButton;

    private final JButton rouRedButton;
    private final JButton rouBlackButton;
    private final JButton rouEvenButton;
    private final JButton rouOddButton;

    private final JButton closeButton;

    // Blackjack state
    private Deck deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private boolean bjInRound = false;

    // Roulette state
    private final Random random = new Random();

    // Colours
    private final Color backgroundColour = new Color(20, 20, 20);
    private final Color panelColour = new Color(30, 30, 30);
    private final Color buttonColour = new Color(50, 50, 50);
    private final Color buttonTextColour = new Color(230, 230, 230);
    private final Color accentColour = new Color(79, 195, 247);

    public MiniCasinoGame() {
        setTitle("ArcadeOne - Mini Casino");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.setBackground(backgroundColour);
        setContentPane(root);

        // ===== Top title =====
        JLabel titleLabel = new JLabel("Mini Casino", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        root.add(titleLabel, BorderLayout.NORTH);

        // ===== Left: controls panel =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(panelColour);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Blackjack controls
        JLabel bjLabel = new JLabel("Blackjack");
        bjLabel.setForeground(accentColour);
        bjLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bjLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        bjNewRoundButton = createButton("New Round", "BJ_NEW");
        bjHitButton = createButton("Hit", "BJ_HIT");
        bjStandButton = createButton("Stand", "BJ_STAND");

        // Start with hit/stand disabled
        bjHitButton.setEnabled(false);
        bjStandButton.setEnabled(false);

        leftPanel.add(bjLabel);
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(bjNewRoundButton);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(bjHitButton);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(bjStandButton);
        leftPanel.add(Box.createVerticalStrut(16));

        // Roulette controls
        JLabel rouLabel = new JLabel("Roulette");
        rouLabel.setForeground(accentColour);
        rouLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rouLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        rouRedButton = createButton("Bet Red", "ROU_RED");
        rouBlackButton = createButton("Bet Black", "ROU_BLACK");
        rouEvenButton = createButton("Bet Even", "ROU_EVEN");
        rouOddButton = createButton("Bet Odd", "ROU_ODD");

        leftPanel.add(rouLabel);
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(rouRedButton);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(rouBlackButton);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(rouEvenButton);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(rouOddButton);
        leftPanel.add(Box.createVerticalStrut(16));

        // Close button at bottom
        closeButton = createButton("Close Casino", "CLOSE");
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(closeButton);

        root.add(leftPanel, BorderLayout.WEST);

        // ===== Center: output area =====
        outputArea = new JTextArea(18, 40);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setBackground(new Color(15, 15, 15));
        outputArea.setForeground(new Color(230, 230, 230));
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
        root.add(scrollPane, BorderLayout.CENTER);

        appendLine("Welcome to the Mini Casino!");
        appendLine("Use the buttons on the left to play Blackjack or Roulette.");

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private JButton createButton(String text, String command) {
        JButton btn = new JButton(text);
        btn.setActionCommand(command);
        btn.addActionListener(this);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(buttonColour);
        btn.setForeground(buttonTextColour);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        return btn;
    }

    private void appendLine(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    // ===== Game interface =====
    @Override
    public String getName() {
        return "Mini Casino";
    }

    @Override
    public void start(JFrame parentFrame) {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    // ===== Actions =====
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "BJ_NEW" -> startBlackjackRound();
            case "BJ_HIT" -> blackjackHit();
            case "BJ_STAND" -> blackjackStand();

            case "ROU_RED" -> playRoulette("RED");
            case "ROU_BLACK" -> playRoulette("BLACK");
            case "ROU_EVEN" -> playRoulette("EVEN");
            case "ROU_ODD" -> playRoulette("ODD");

            case "CLOSE" -> dispose();
        }
    }

    // ===== BLACKJACK LOGIC =====
    private void startBlackjackRound() {
        deck = new Deck();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();

        playerHand.add(deck.deal());
        playerHand.add(deck.deal());
        dealerHand.add(deck.deal());
        dealerHand.add(deck.deal());

        bjInRound = true;
        bjHitButton.setEnabled(true);
        bjStandButton.setEnabled(true);

        appendLine("\n=== New Blackjack Round ===");
        appendLine("Your hand: " + handToString(playerHand)
                + " (total: " + handValue(playerHand) + ")");
        appendLine("Dealer shows: " + dealerHand.get(0));

        // In case deck runs out (very unlikely for one round but just in case)
        if (deck.size() < 10) {
            appendLine("Deck is getting low. It will be reshuffled next round.");
        }
    }

    private void blackjackHit() {
        if (!bjInRound) {
            appendLine("Start a new round first.");
            return;
        }

        Card drawn = deck.deal();
        playerHand.add(drawn);
        appendLine("You draw: " + drawn);
        int total = handValue(playerHand);
        appendLine("Your total is now: " + total);

        if (total > 21) {
            appendLine("You bust! Dealer wins.");
            endBlackjackRound();
        }
    }

    private void blackjackStand() {
        if (!bjInRound) {
            appendLine("Start a new round first.");
            return;
        }

        appendLine("\nDealer's turn...");
        appendLine("Dealer's hand: " + handToString(dealerHand)
                + " (total: " + handValue(dealerHand) + ")");

        while (handValue(dealerHand) < 17) {
            Card drawn = deck.deal();
            dealerHand.add(drawn);
            appendLine("Dealer draws: " + drawn
                    + " (total: " + handValue(dealerHand) + ")");
        }

        int playerTotal = handValue(playerHand);
        int dealerTotal = handValue(dealerHand);

        appendLine("\nFinal totals:");
        appendLine("You: " + playerTotal);
        appendLine("Dealer: " + dealerTotal);

        if (dealerTotal > 21 || playerTotal > dealerTotal) {
            appendLine("You win!");
        } else if (playerTotal == dealerTotal) {
            appendLine("Push (tie).");
        } else {
            appendLine("Dealer wins.");
        }

        endBlackjackRound();
    }

    private void endBlackjackRound() {
        bjInRound = false;
        bjHitButton.setEnabled(false);
        bjStandButton.setEnabled(false);
        appendLine("=== Round over – press \"New Round\" to play again. ===");
    }

    private String handToString(List<Card> hand) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < hand.size(); i++) {
            sb.append(hand.get(i));
            if (i < hand.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private int handValue(List<Card> hand) {
        int total = 0;
        int aces = 0;

        for (Card c : hand) {
            String r = c.getRank();

            if (r.equals("A")) {
                total += 11;
                aces++;
            } else if (r.equals("K") || r.equals("Q") || r.equals("J") || r.equals("10")) {
                total += 10;
            } else {
                total += Integer.parseInt(r);
            }
        }

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }

    // ===== ROULETTE LOGIC =====
    private void playRoulette(String betType) {
        int number = random.nextInt(37); // 0–36
        String color;

        if (number == 0) {
            color = "GREEN";
        } else if (isRedNumber(number)) {
            color = "RED";
        } else {
            color = "BLACK";
        }

        appendLine("\n=== Roulette Spin ===");
        appendLine("Ball landed on: " + number + " (" + color + ")");
        appendLine("You bet: " + betType);

        boolean win = false;
        switch (betType) {
            case "RED" -> win = (number != 0 && color.equals("RED"));
            case "BLACK" -> win = (number != 0 && color.equals("BLACK"));
            case "EVEN" -> win = (number != 0 && number % 2 == 0);
            case "ODD" -> win = (number % 2 == 1);
        }

        if (win) {
            appendLine("Result: YOU WIN!");
        } else {
            appendLine("Result: You lose.");
        }
    }

    // Simple rule: odd = red, even = black (0 = green)
    private boolean isRedNumber(int n) {
        return n != 0 && n % 2 == 1;
    }
}
