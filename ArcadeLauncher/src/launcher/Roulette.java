package launcher;

import java.util.Random;
import java.util.Scanner;

public class Roulette implements CasinoGame {

    private final Scanner scanner;
    private final Random random = new Random();

    public Roulette(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String getName() {
        return "Roulette";
    }

    @Override
    public void playRound() {
        System.out.println("\n=== Roulette ===");
        System.out.println("Choose bet type:");
        System.out.println("1) Red");
        System.out.println("2) Black");
        System.out.println("3) Even");
        System.out.println("4) Odd");
        System.out.print("Choice: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        int number = random.nextInt(37); // 0–36
        boolean isRed = isRedNumber(number);

        String color = (number == 0) ? "Green" : (isRed ? "Red" : "Black");
        System.out.println("Ball landed on: " + number + " (" + color + ")");

        boolean win = false;
        switch (choice) {
            case 1: // Red
                win = (number != 0 && isRed);
                break;
            case 2: // Black
                win = (number != 0 && !isRed);
                break;
            case 3: // Even
                win = (number != 0 && number % 2 == 0);
                break;
            case 4: // Odd
                win = (number % 2 == 1);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (win) {
            System.out.println("You win!");
        } else {
            System.out.println("You lose.");
        }
    }

    // Simple color logic: odd = red, even = black (0 = green)
    private boolean isRedNumber(int n) {
        return n != 0 && n % 2 == 1;
    }
}
