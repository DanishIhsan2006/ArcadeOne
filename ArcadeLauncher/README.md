# ArcadeOne

A desktop arcade built in Java with Swing. One launcher window opens nine games, and every game plugs into the launcher through a shared `Game` interface.

<!-- Add a screenshot here: drag an image into the GitHub editor and it will paste a link -->

## Games

| Game | Notes |
|---|---|
| Chess | Full board with piece types and move handling |
| Sudoku | Puzzles are generated and solved by the program |
| Minesweeper | Grid of cells with hidden mines |
| Tic-Tac-Toe | Classic 3×3 |
| Hangman | Guess the word letter by letter |
| Rock Paper Scissors | Play against the computer |
| Samurai Duel | Reaction-based duel |
| Cookie Clicker | Click to score |
| Mini Casino | Blackjack and Roulette with a shared deck/card system |

## How it works

- `Game` is an interface with `getName()` and `start(JFrame)`. Each game implements it.
- `GameLauncher` registers the games in a list, and `GameLauncherUI` builds the menu from that list. Adding a new game only means writing one class and adding one line.
- The casino games share `Card`, `Deck` and a `CasinoGame` base, so Blackjack and Roulette reuse the same pieces.

## Run it

Requires Java 8 or newer.

```bash
cd ArcadeLauncher/src
javac launcher/*.java
java launcher.GameLauncherUI
```

Or open the `ArcadeLauncher` folder in Eclipse and run `GameLauncherUI`.

## What I learned

- Designing with interfaces so new features plug in without changing existing code
- Building GUIs with Java Swing and event listeners
- Game logic: puzzle generation and solving (Sudoku), board state (Chess, Minesweeper) and card handling (Blackjack)
