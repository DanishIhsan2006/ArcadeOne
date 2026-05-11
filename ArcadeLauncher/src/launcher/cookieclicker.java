package launcher;

import javax.swing.JFrame;
import java.util.Scanner;

/* Simple Console Cookie Clicker */
public class cookieclicker implements Game {

    @Override
    public String getName() {
        return "Cookie Clicker";
    }

    @Override
    public void start(JFrame parentFrame) {
        main(new String[0]); // run console version
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        long cookies = 0;
        long cookiesPerClick = 1;

        long upgradeCost = 50;
        long upgradeIncrease = 1;

        System.out.println("=== COOKIE CLICKER (Console Edition) ===");

        while (true) {
            System.out.println("\nCookies: " + cookies);
            System.out.println("Cookies per click: " + cookiesPerClick);
            System.out.println("Upgrade cost: " + upgradeCost + " (adds +" + upgradeIncrease + " per click)");
            System.out.println("\nOptions:");
            System.out.println("1. Click cookie");
            System.out.println("2. Buy upgrade");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            String choice = in.nextLine();

            if (choice.equals("1")) {
                cookies += cookiesPerClick;
                System.out.println("You clicked! +" + cookiesPerClick);
            } else if (choice.equals("2")) {
                if (cookies >= upgradeCost) {
                    cookies -= upgradeCost;
                    cookiesPerClick += upgradeIncrease;
                    upgradeCost *= 2;
                    System.out.println("Upgrade purchased! Cookies per click increased.");
                } else {
                    System.out.println("Not enough cookies!");
                }
            } else if (choice.equals("3")) {
                System.out.println("Thanks for playing!");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }

        in.close();
    }
}
