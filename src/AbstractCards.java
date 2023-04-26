// Abstract class used for generating decks and validating card values
public abstract class AbstractCards {
    // Declare our valid denominations
    public enum Denominations {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    }

    // Declare our valid suits
    public enum Suits {
        CLUBS, SPADES, DIAMONDS, HEARTS
    }
}
