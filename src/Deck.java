import java.util.Stack;

// This represents a deck of cards (excluding jokers).
public class Deck extends AbstractCards {
    // LIFO Stack because we deal off the top... Not that it matters here.
    private Stack<Card> deck = new Stack<>();

    // Lets create a new deck
    public Deck() {
        for (Suits suit : Suits.values()) {
            for (Denominations denomination : Denominations.values()) {
                deck.add(new Card(denomination, suit));
            }
        }
    }

    // Return our deck
    public Stack<Card> getDeck() {
        return deck;
    }
}
