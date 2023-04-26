import java.util.LinkedList;

// This represents a hand of cards in blackjack. It has methods for helping with
//  aces, splitting, and doubling down in addition to your typical accessor/mutators.
public class Hand extends AbstractCards {
    private LinkedList<Card> cards = new LinkedList<>();

    public void addCard(Card card) {
        cards.add(card);
    }

    // Return the value of the hand
    public int getValue() {
        int score = 0;
        for (Card card: cards) {
            score += card.getValue();
        }
        return score;
    }

    public boolean canDoubleDown() {
        return getValue() == 9 || getValue() == 10 || getValue() == 11;
    }

    public boolean canSplit() {
        return cards.getFirst().getDenomination() == cards.getLast().getDenomination();
    }

    // Split the hand or except
    public Hand splitHand() {
        Hand hand = new Hand();
        if (cards.size() != 2) {
            throw new IllegalArgumentException("a split can only occur before the player hits");
        }

        if (!canSplit()) {
            throw new IllegalArgumentException("you can only split matching denominations");
        }

        hand.addCard(cards.pop());

        return hand;
    }

    // Build and return an array of strings for each card in the hand.
    public String[] getHandStringArray() {
        String[] handStringArray = new String[cards.size()];

        for (int i = 0; i < cards.size(); i++) {
            handStringArray[i] = cards.get(i).getCardString();
        }

        return handStringArray;
    }

    // Does the initially dealt hand contain a natural? (an ace and a "ten-card")
    public boolean hasNatural() {
        return cards.size() == 2 && getValue() == 21;
    }

    public boolean hasAce() {
        for (Card card : cards) {
            if (card.getDenomination() == Denominations.ACE) {
                return true;
            }
        }
        return false;
    }

    // Return the index of an ace or -1 if not found.
    //  While a player could potentially have two aces, there should only be reason to toggle one.
    public int getAce() {
        int aceIndex = -1;
        for (Card card : cards) {
            if  (card.getDenomination() == Denominations.ACE) {
                return cards.indexOf(card);
            }
        }
        return aceIndex;
    }

    public LinkedList<Card> getCards() {
        return cards;
    }
}