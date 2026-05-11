package launcher;

/**
 * Simple console-based game interface for the Mini Casino (Blackjack/Roulette).
 * This is completely separate from the GUI Game interface used by the
 * ArcadeOne launcher.
 */
public interface CasinoGame {
    String getName();
    void playRound();
}
