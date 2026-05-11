package launcher;

import javax.swing.*;
import java.util.Random;

public class RockPaperScissors implements Game {

    private final Random random = new Random();

    // ASCII art
    private final String ROCK = """
        _______
    ---'   ____)
          (_____)
          (_____)
          (____)
    ---.__(___)
    """;

    private final String PAPER = """
         _______
    ---'    ____)____
               ______)
              _______)
             ________)
    ---.___________)
    """;

    private final String SCISSORS = """
        _______
    ---'   ____)____
              ______)
           __________)
          (____)
    ---.__(___)
    """;

    @Override
    public String getName() {
        return "Rock Paper Scissors";
    }

    @Override
    public void start(JFrame parentFrame) {

        String[] options = {"Rock", "Paper", "Scissors"};

        int userChoice = JOptionPane.showOptionDialog(
                parentFrame,
                "Choose one:",
                "Rock Paper Scissors",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        // User closed the window
        if (userChoice == -1) {
            return;
        }

        int compChoice = random.nextInt(3);

        // Convert to strings
        String userPick = toStringChoice(userChoice);
        String compPick = toStringChoice(compChoice);

        // Get ASCII art
        String userArt = ascii(userChoice);
        String compArt = ascii(compChoice);

        // Determine winner
        String result = getWinner(userChoice, compChoice);

        // Show final result
        JOptionPane.showMessageDialog(
                parentFrame,
                "You picked: " + userPick + "\n" + userArt +
                "\nComputer picked: " + compPick + "\n" + compArt +
                "\nResult: " + result,
                "Rock Paper Scissors",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private String ascii(int c) {
        return switch (c) {
            case 0 -> ROCK;
            case 1 -> PAPER;
            case 2 -> SCISSORS;
            default -> "";
        };
    }

    private String toStringChoice(int c) {
        return switch (c) {
            case 0 -> "Rock";
            case 1 -> "Paper";
            case 2 -> "Scissors";
            default -> "";
        };
    }

    private String getWinner(int user, int comp) {
        if (user == comp) {
            return "It's a tie!";
        }

        if ((user == 0 && comp == 2) ||   // Rock beats Scissors
            (user == 1 && comp == 0) ||   // Paper beats Rock
            (user == 2 && comp == 1)) {   // Scissors beats Paper
            return "You win!";
        }

        return "Computer wins!";
    }
}
