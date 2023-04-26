import java.util.ArrayList;
import java.util.LinkedList;

// This represents a player. It stores their name, the amount of
//  money they have available, and their current hand(s).
public class Player {
    private String name;
    private int money;
    private LinkedList<Hand> hands = new LinkedList<>();

    // Create our player
    public Player(String name, int money) {
        // Make sure they have a name and at least one dollar...
        if (name.equals("") || money < 1) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public boolean canBet(int bet) {
        return money >= bet;
    }

    public void addMoney(int money) {
        if (money < 1) {
            throw new IllegalArgumentException("you can only add values greater than zero");
        }

        this.money += money;
    }

    public void removeMoney(int money) {
        if (money < 1) {
            throw new IllegalArgumentException("you can only remove values greater than zero");
        }

        this.money -= money;
    }

    public void addHand(Hand hand) {
        hands.add(hand);
    }

    public LinkedList<Hand> getHands() {
        return hands;
    }
}
