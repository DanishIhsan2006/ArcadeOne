package launcher;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.*;

/**
 * Console-based Hangman that plugs into the Game launcher.
 */
public class Hangman implements Game {

    private static final String[] WORDS = {
            "computer", "java", "hangman", "program",
            "school", "mouse", "keyboard", "laptop"
    };

    @Override
    public String getName() {
        return "Hangman";
    }

    @Override
    public void start(JFrame parentFrame) {
        // IMPORTANT: run console game in a separate thread
        new Thread(this::runGame).start();

        // Optional: tell the user to look at the console
        JOptionPane.showMessageDialog(
                parentFrame,
                "Hangman is a console game.\n" +
                "Check the console window to play.",
                "Hangman",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // All your existing game logic, moved out of main()
    private void runGame() {
        Scanner in = new Scanner(System.in);
        Random rand = new Random();

        String word = WORDS[rand.nextInt(WORDS.length)];
        char[] progress = new char[word.length()];
        Arrays.fill(progress, '_');
        HashSet<Character> guesses = new HashSet<>();

        int lives = 6;

        System.out.println("=== Hangman ===");

        while (lives > 0 && new String(progress).contains("_")) {
            System.out.println("\nWord: " + String.valueOf(progress));
            System.out.println("Lives left: " + lives);
            System.out.println("Guessed letters: " + guesses);
            System.out.print("Guess a letter: ");

            String input = in.nextLine().toLowerCase();
            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.println("Invalid guess. Enter a single letter.");
                continue;
            }

            char guess = input.charAt(0);

            if (guesses.contains(guess)) {
                System.out.println("You already guessed that.");
                continue;
            }
            guesses.add(guess);

            if (word.indexOf(guess) >= 0) {
                // correct guess
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == guess) {
                        progress[i] = guess;
                    }
                }
                System.out.println("Correct!");
            } else {
                lives--;
                System.out.println("Wrong!");
            }
        }

        if (lives > 0) {
            System.out.println("\nYou WIN! The word was: " + word);
        } else {
            System.out.println("\nYou LOST! The word was: " + word);
        }

        // don't close System.in here globally if you might use it elsewhere
    }

    // Still allow running Hangman standalone if you want
    public static void main(String[] args) {
        new Hangman().start(null);
    }
}
