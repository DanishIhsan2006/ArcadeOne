package launcher;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;

public class GameLauncher {

    private final List<Game> games;

    public GameLauncher() {
        games = new ArrayList<>();

        games.add(new Chess());
        games.add(new TicTacToe());
        games.add(new Sudoku());
        games.add(new Minesweeper());
        games.add(new cookieclicker());
        games.add(new SamuraiDuel());
        games.add(new RockPaperScissors());
        games.add(new Hangman());
        games.add(new MiniCasinoGame()); // ✅ GUI casino game
    }


    public List<Game> getGames() {
        return games;
    }

    // Optional: not really used
    private static class MessageGame implements Game {

        private final String name;

        public MessageGame(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void start(JFrame parentFrame) {
            javax.swing.JOptionPane.showMessageDialog(
                    parentFrame,
                    "This is where " + name + " would run.",
                    name,
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
