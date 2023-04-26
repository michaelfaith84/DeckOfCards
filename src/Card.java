// This class represents a singular card in our Shoe or a Hand.
public class Card extends AbstractCards {
    // What denomination is the card? (1, 2, jack, etc)
    private Denominations denomination;
    // What suit is the card? (spade, diamond, etc)
    private Suits suit;
    // Should we count this (potential) ace as 11 or 1?
    private boolean aceHigh = true;

    // Constructor

    public Card(Denominations denomination, Suits suit) {
        this.denomination = denomination;
        this.suit = suit;
    }

    public Denominations getDenomination() {
        return denomination;
    }

    // Return numeric values for numeric denominations and convert and return the rest
    public String getDenominationString() {
        switch (denomination) {
            case TWO:
                return "2";
            case THREE:
                return "3";
            case FOUR:
                return "4";
            case FIVE:
                return "5";
            case SIX:
                return "6";
            case SEVEN:
                return "7";
            case EIGHT:
                return "8";
            case NINE:
                return "9";
            case TEN:
                return "10";
            default:
                return denomination.toString().toLowerCase();
        }
    }

    // Convert our suit enum into string and return it
    public String getSuitString() {
        return suit.toString().toLowerCase();
    }

    // Return a string representing our card
    public String getCardString() {
        return String.format("%s of %s", getDenominationString(), getSuitString());
    }

    // Return our aceHigh value
    public boolean getAceHigh() {
        return aceHigh;
    }

    // Sets our aceHigh value
    public void toggleAceHigh() {
        aceHigh = !aceHigh;
    }

    // Return our cards point value
    public int getValue() {
        switch (denomination) {
            case ACE:
                return aceHigh ? 11 : 1;
            case TEN:
            case JACK:
            case QUEEN:
            case KING:
                return 10;
            default:
                return Integer.parseInt(getDenominationString());
        }
    }
}
