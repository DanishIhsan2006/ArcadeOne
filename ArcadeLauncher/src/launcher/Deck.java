package launcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        reset();
    }

    public final void reset() {
        cards.clear();
        String[] ranks = { "2","3","4","5","6","7","8","9","10","J","Q","K","A" };
        String[] suits = { "Hearts","Diamonds","Clubs","Spades" };

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(rank, suit));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No more cards in the deck");
        }
        return cards.remove(0);
    }

    public int size() {
        return cards.size();
    }
}
