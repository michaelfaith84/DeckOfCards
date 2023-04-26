import java.util.Collections;
import java.util.Stack;

// This represents the shoe which we deal from. It contains multiple decks. Also
//  supports deck cutting which introduces an element of true randomosity.
public class Shoe {
    private Stack<Card> cards = new Stack<>();

    // Constructor that takes an arbitrary number of decks
    public Shoe(int numberOfDecks) {
        for (int i = 0; i < numberOfDecks; i++) {
            cards.addAll(new Deck().getDeck());
        }
    }

    // Default to six decks a la casino style
    public Shoe() {
        for (int i = 0; i < 6; i++) {
            cards.addAll(new Deck().getDeck());
        }
    }

    // Shuffle our cards
    public void shuffleCards() {
        Collections.shuffle(cards);
        System.out.println("Dealer shuffles the cards.");
    }

    // Cut the cards. "position" represents a percentage of the deck.
    public void cutCards(int position) {
        int positionIndex;
        Stack<Card> newCards = new Stack<>();

        // Validate our selection. Let's make it an effective cut.
        if (position < 5 || position > 95) {
            throw new IllegalArgumentException("position must be between 5 and 95");
        }

        // Get the index of our cut
        positionIndex = cards.size() - (int) (((float) position/100) * cards.size());

        // Add our cut to the new cards stack
        for (int i = 0; i < positionIndex; i++) {
            newCards.push(cards.pop());
        }

        // Because they're now backwards, let's flip them
        Collections.reverse(newCards);

        newCards.addAll(cards);

        // Assign our cut cards to "cards"
        cards = newCards;
    }

    // Remove the top card
    public void burnCard() {
        cards.pop();
        System.out.println("Dealer burns a card.");
    }

    // Return the top card
    public Card dealCard() {
        return cards.pop();
    }
}
