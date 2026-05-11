package launcher;

import java.util.Scanner;

public class CasinoMainMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CasinoGame blackjack = new Blackjack(scanner);
        CasinoGame roulette = new Roulette(scanner);

        while (true) {
            System.out.println("\n===== Mini Casino =====");
            System.out.println("1) " + blackjack.getName());
            System.out.println("2) " + roulette.getName());
            System.out.println("3) Quit");
            System.out.print("Choose a game: ");

            String input = scanner.nextLine().trim();

            if (input.equals("1")) {
                blackjack.playRound();
            } else if (input.equals("2")) {
                roulette.playRound();
            } else if (input.equals("3")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}
