package launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Blackjack implements CasinoGame {

    private final Scanner scanner;
    private Deck deck;

    public Blackjack(Scanner scanner) {
        this.scanner = scanner;
        this.deck = new Deck();
    }

    @Override
    public String getName() {
        return "Blackjack";
    }

    @Override
    public void playRound() {
        if (deck.size() < 15) { // reshuffle if low
            deck = new Deck();
        }

        List<Card> playerHand = new ArrayList<>();
        List<Card> dealerHand = new ArrayList<>();

        // deal initial cards
        playerHand.add(deck.deal());
        playerHand.add(deck.deal());
        dealerHand.add(deck.deal());
        dealerHand.add(deck.deal());

        System.out.println("\n=== Blackjack ===");
        System.out.println("Your hand: " + playerHand);
        System.out.println("Dealer shows: " + dealerHand.get(0));

        // PLAYER TURN
        while (true) {
            int playerValue = handValue(playerHand);
            System.out.println("Your total: " + playerValue);

            if (playerValue > 21) {
                System.out.println("You bust! Dealer wins.");
                return;
            }

            System.out.print("Hit or stand (h/s)? ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("h")) {
                Card drawn = deck.deal();
                playerHand.add(drawn);
                System.out.println("You draw: " + drawn);
            } else if (choice.equals("s")) {
                break;
            } else {
                System.out.println("Please enter 'h' or 's'.");
            }
        }

        // DEALER TURN
        System.out.println("\nDealer's hand: " + dealerHand);
        while (handValue(dealerHand) < 17) {
            Card c = deck.deal();
            dealerHand.add(c);
            System.out.println("Dealer draws: " + c);
        }

        int playerValue = handValue(playerHand);
        int dealerValue = handValue(dealerHand);

        System.out.println("\nYour total: " + playerValue);
        System.out.println("Dealer total: " + dealerValue);

        if (dealerValue > 21 || playerValue > dealerValue) {
            System.out.println("You win!");
        } else if (playerValue == dealerValue) {
            System.out.println("Push (tie).");
        } else {
            System.out.println("Dealer wins.");
        }
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

        // Convert Aces from 11 → 1 if needed
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }
}
