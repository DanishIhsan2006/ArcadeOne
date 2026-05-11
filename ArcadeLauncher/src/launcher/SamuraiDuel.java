package launcher;

import javax.swing.JFrame;
import java.util.Random;
import java.util.Scanner;

public class SamuraiDuel implements Game {

    @Override
    public String getName() {
        return "Samurai Duel";
    }

    @Override
    public void start(JFrame parentFrame) {
        main(new String[0]); // run console version
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Random rand = new Random();

        int playerHP = 20;
        int enemyHP = 20;

        System.out.println("==================================");
        System.out.println("        SAMURAI 1v1 DUEL");
        System.out.println("==================================\n");

        System.out.println("You are a young samurai, facing a rival warrior in a duel.");
        System.out.println("First to drop the other to 0 HP wins.\n");

        System.out.println("   You                  Enemy");
        System.out.println("  (0_0)                (x_x)");
        System.out.println("   /|\\       VS        /|\\");
        System.out.println("   / \\                 / \\\n");

        while (playerHP > 0 && enemyHP > 0) {

            System.out.println("----------------------------------");
            System.out.println("Your HP:  " + playerHP);
            System.out.println("Enemy HP: " + enemyHP);
            System.out.println("----------------------------------");
            System.out.println("Choose your move:");
            System.out.println("1) Quick Slash  (3 damage, always hits)");
            System.out.println("2) Heavy Strike (6 damage, 50% chance to hit)");
            System.out.println("3) Block        (take less damage this turn)");

            int choice = 0;
            while (choice < 1 || choice > 3) {
                System.out.print("> ");
                if (in.hasNextInt()) {
                    choice = in.nextInt();
                } else {
                    in.next(); // clear invalid input
                }
            }

            boolean blocking = false;

            if (choice == 1) {
                System.out.println("\nYou use Quick Slash!");
                enemyHP -= 3;
                System.out.println("You hit the enemy for 3 damage!");
            } else if (choice == 2) {
                System.out.println("\nYou use Heavy Strike!");
                if (rand.nextBoolean()) {
                    enemyHP -= 6;
                    System.out.println("CRITICAL HIT! You deal 6 damage!");
                } else {
                    System.out.println("You miss! Your blade cuts only air.");
                }
            } else {
                System.out.println("\nYou raise your sword to block.");
                blocking = true;
            }

            if (enemyHP <= 0) {
                break;
            }

            int enemyMove = rand.nextInt(2) + 1;
            int enemyDamage;

            if (enemyMove == 1) {
                System.out.println("\nEnemy uses Slash!");
                enemyDamage = 4;
            } else {
                System.out.println("\nEnemy uses Thrust!");
                enemyDamage = 5;
            }

            if (blocking) {
                enemyDamage /= 2;
                System.out.println("You block part of the attack!");
            }

            playerHP -= enemyDamage;
            System.out.println("You take " + enemyDamage + " damage.\n");
        }

        System.out.println("==================================");
        if (playerHP <= 0 && enemyHP <= 0) {
            System.out.println("Both samurai fall at the same time...");
            System.out.println("It's a DRAW.");
        } else if (playerHP <= 0) {
            System.out.println("You fall to your knees. Your rival wins the duel.");
            System.out.println("DEFEAT.");
        } else {
            System.out.println("Your opponent collapses. You win the duel!");
            System.out.println("VICTORY!");
        }
        System.out.println("==================================");

        in.close();
    }
}
