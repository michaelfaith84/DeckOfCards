import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

// This handles the game logic for our casino style black jack game.
public class CasinoBlackJack {
    private Scanner input = new Scanner(System.in);
    private Random random = new Random();
    private Shoe shoe;
    private LinkedList<Player> players = new LinkedList<>();
    private Hand dealerHand;
    private int startingMoney;
    private int minBet;
    private int maxBet;
    private LinkedList<Integer> bets = new LinkedList<>();

    // Constructor for basic setup.
    public CasinoBlackJack(int startingMoney, int minBet, int maxBet) {
        this.startingMoney = startingMoney;
        this.minBet = minBet;
        this.maxBet = maxBet;
    }

    // Constructor with defaults
    public CasinoBlackJack() {
        this.startingMoney = 100;
        this.minBet = 5;
        this.maxBet = 25;
    }

    // This is used for selecting a player to cut
    public Player getRandomPlayer() {
        return players.get(random.nextInt(players.size()));
    }

    // Prompt each player for their bets and add them to the array.
    //  Array indexing is symmetric to the players array.
    public void promptForBets() {
        int bet;
        System.out.printf("Place your bets (min: %d, max: %d)\n", minBet, maxBet);

        // Clear any existing bets
        if (bets.size() > 0) { bets.clear(); }

        for (Player player : players) {
            System.out.printf("%s, you have $%d. ", player.getName(), player.getMoney());

            while (true) {
                try {
                    System.out.print("Your bet? ");
                    bet = input.nextInt();
                    if (bet >= minBet && bet <= maxBet) {
                        player.removeMoney(bet);
                        bets.add(bet);
                        break;
                    }
                    System.out.printf("Invalid amount. (min: %d, max: %d)\n", minBet, maxBet);
                } catch (InputMismatchException e) {
                    System.out.println("Invalid entry.");
                    input.next();
                }
            }
        }
    }

    // Prompts and creates an arbitrary number of players.
    public void setupPlayers() {
        String addAnotherInput;
        System.out.print("Player name? ");
        players.add(new Player(input.next(), startingMoney));

        // Clear newline
        input.nextLine();

        while (true) {
            System.out.print("Add another player? y/[n] ");
            addAnotherInput = input.nextLine().toLowerCase();

            if (addAnotherInput.equals("y")) {
                System.out.print("Player name? ");
                players.add(new Player(input.next(), startingMoney));

            } else if (addAnotherInput.equals("n") || addAnotherInput.isEmpty()) {
                break;
            }
        }
    }

    // This returns true if the player opts to double down otherwise false.
    public boolean handleDoubleDown(Player player, Hand hand) {
        String textInput;
        boolean doubleDown = false;

        if (hand.canDoubleDown() && player.getMoney() >= bets.get(players.indexOf(player))) {
            while (true) {
                System.out.printf("%s, would you like to double down? y/n ", player.getName());
                textInput = input.next().toLowerCase();

                if (textInput.equals("y")) {
                    hand.addCard(shoe.dealCard());
                    System.out.printf("%s has been dealt another card.", player.getName());
                    doubleDown = true;
                    break;

                } else if (textInput.equals("n")) {
                    break;
                }
            }
        }
        return doubleDown;
    }

    public void handleSplit() {
        Card card;
        String textInput;

        // See if the player wants to split.
        for (Player player : players) {
            if (player.getHands().getFirst().canSplit()) {
                System.out.printf("%s, would you like to split your pair? y/n ", player.getName());

                while (true) {
                    textInput = input.next().toLowerCase();

                    if (textInput.equals("y")) {
                        player.addHand(player.getHands().getFirst().splitHand());
                        // Deduct another bet
                        player.removeMoney(bets.get(players.indexOf(player)));

                        // Deal second card to each hand
                        for (Hand hand : player.getHands()) {
                            card = shoe.dealCard();
                            System.out.printf("%s\n", card.getCardString());
                            hand.addCard(card);
                        }
                        break;

                    } else if (textInput.equals("n")) {
                        break;
                    }
                }
            }
        }
    }

    // Used to print an initial hand to the console.
    public void showHand(String name, Hand hand) {
        System.out.printf("%s, hand: %s and %s for %d.\n",
                name,
                hand.getHandStringArray()[0],
                hand.getHandStringArray()[1],
                hand.getValue());
    }

    // Handle the (potential) payouts. Original bets have already been deducted.
    public void settleBets() {
        int payout;
        for (Player player : players) {
            for (Hand hand : player.getHands()) {
                if (hand.hasNatural() && !dealerHand.hasNatural()) {
                    payout = (int) (bets.get(players.indexOf(player)) * 1.5);
                    player.addMoney(payout + bets.get(players.indexOf(player)));
                    System.out.printf("%s has a natural! Payout: $%d.\n",
                            player.getName(),
                            payout);

                } else if (hand.hasNatural() && dealerHand.hasNatural()) {
                    System.out.printf("%s, you and the dealer have naturals. You break even.\n", player.getName());
                    player.addMoney(bets.get(players.indexOf(player)));

                } else if (dealerHand.hasNatural() && !hand.hasNatural()) {
                    System.out.println("Dealer has a natural.");

                } else if (dealerHand.getValue() > 21 && hand.getValue() < 22) {
                    payout = bets.get(players.indexOf(player));
                    player.addMoney(payout * 2);
                    System.out.printf("%s, dealer busted! Payout: $%d.\n",
                            player.getName(),
                            payout);

                } else if (hand.getValue() < 22 &&
                        dealerHand.getValue() > hand.getValue() &&
                        dealerHand.getValue() < 22) {
                    System.out.printf("%s, dealer's %d beats your %d.\n",
                            player.getName(),
                            dealerHand.getValue(),
                            hand.getValue());

                } else if (hand.getValue() < 22 && dealerHand.getValue() == hand.getValue()) {
                    System.out.printf("%s, tie goes to the dealer.\n", player.getName());

                } else if (hand.getValue() < 22 && dealerHand.getValue() < hand.getValue()) {
                    payout = bets.get(players.indexOf(player));
                    player.addMoney(payout * 2);
                    System.out.printf("%s, your %d beats the dealer's %d. Payout: $%d.\n",
                            player.getName(),
                            hand.getValue(),
                            dealerHand.getValue(),
                            payout);

                } else if (hand.getValue() > 21) {
                    System.out.printf("%s, you busted.\n", player.getName());
                }
            }
        }

    }

    // Offer the option of toggling the ace's value.
    public void handleAce(Scanner input, Hand hand) {
        String textInput;

        if (hand.hasAce()) {
            System.out.printf("Your ace is currently counted as %s. Would you like to change that? y/n ",
                    hand.getCards().get(hand.getAce()).getAceHigh() ? "high" : "low");

            while (true) {
                textInput = input.next().toLowerCase();
                if (textInput.equals("y")) {
                    hand.getCards().get(hand.getAce()).toggleAceHigh();
                    System.out.printf("New hand value: %d", hand.getValue());
                    break;

                } else if  (textInput.equals("n")) {
                    break;
                }
            }
        }
    }

    // Runs the players through a round of the game. Setup to create a new shoe everytime.
    public void playRound() {
        Card card;
        Player playerToCut = getRandomPlayer();
        int cut;
        String textInput;
        boolean doubleDown;

        // New shoe and shuffle.
        shoe = new Shoe();
        shoe.shuffleCards();

        // Let's cut.
        while (true) {
            System.out.printf("%s, please cut the deck: 5-95 ", playerToCut.getName());
            try {
                cut = input.nextInt();
                if (cut >= 5 && cut <= 95) {
                    break;
                } else {
                    System.out.println("Invalid cut. " +
                            "Enter a whole number equal to or greater than 5 and less than or equal to 95.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid entry.");
                input.next();
            }
        }
        shoe.cutCards(cut);

        System.out.println();

        // Get bets.
        promptForBets();

        shoe.burnCard();
        System.out.println();

        // Create a hand for each player
        for (Player player : players) {
            // If there are existing hands, clear them.
            if (player.getHands().size() > 0) { player.getHands().clear(); }

            player.addHand(new Hand());
        }

        dealPlayers();

        dealerHand = new Hand();
        card = shoe.dealCard();
        dealerHand.addCard(card);
        System.out.printf("Dealer's first card is %s.\n", card.getCardString());

        System.out.println();
        dealPlayers();

        dealerHand.addCard(shoe.dealCard());
        System.out.println("Dealer's second card is hidden.");

        System.out.println();

        handleSplit();

        // And finally... The main event.
        for (Player player : players) {
            for (Hand hand : player.getHands()) {
                // Winner!
                if (hand.hasNatural()) {
                    System.out.printf("%s, you have a natural!\n", player.getName());

                // Let's play.
                } else {
                    showHand(player.getName(), hand);

                    handleAce(input, hand);

                    // It's a bit simpler to handle this on the fly rather than a preprocess like splitting.
                    doubleDown = handleDoubleDown(player, hand);
                    if (doubleDown) {
                        bets.set(players.indexOf(player), bets.get(players.indexOf(player)) * 2);
                        player.removeMoney(bets.get(players.indexOf(player)));

                    } else  {
                        while (true) {
                            System.out.print("\t[H]it or [s]tand? ");
                            textInput = input.next().toLowerCase();

                            // Hit for another card
                            if (textInput.equals("h")) {
                                card = shoe.dealCard();
                                hand.addCard(card);
                                System.out.printf("Card: %s for %s.\n",
                                        card.getCardString(),
                                        hand.getValue());

                                handleAce(input, hand);

                                if (hand.getValue() > 21) {
                                    System.out.println("You busted!");
                                    break;
                                }

                                // Stand. No more cards.
                            } else if (textInput.equals("s")) {
                                break;

                            } else {
                                System.out.println("Invalid selection.");
                            }
                        }
                    }
                }
            }
            System.out.println();
        }

        // Handle Dealer
        showHand("Dealer", dealerHand);
        while (dealerHand.getValue() < 17) {
            card = shoe.dealCard();
            dealerHand.addCard(card);
            System.out.printf("Dealer hits. Card: %s.\n", card.getCardString());
            System.out.printf("Dealer has %d.\n", dealerHand.getValue());
        }
        if (dealerHand.getValue() < 22) {
            System.out.println("Dealer stands.");
        } else {
            System.out.println("Dealer busted!");
        }

        System.out.println();

        // Settle Bets
        settleBets();
    }

    // Runs round after round until there are no more players.
    //  Handles removing players due to being broke or them opting out.
    public void playGame() {
        LinkedList<Player> leavingPlayers = new LinkedList<>();
        String keepPlaying;

        setupPlayers();

        System.out.println();

        // Keep playing until all the players have busted or quit.
        while (players.size() > 0) {
            playRound();

            System.out.println();

            input.nextLine();

            for (Player player : players) {
                if (!player.canBet(minBet)) {
                    System.out.printf("Sorry, %s, you are too broke to continue!\n", player.getName());
                    leavingPlayers.add(player);

                } else {
                    System.out.printf("%s has $%d, would you like to continue? \"n\" to quit ",
                            player.getName(),
                            player.getMoney());
                    keepPlaying = input.nextLine().toLowerCase();

                    if (keepPlaying.equals("n")) {
                        System.out.printf("%s walks away with $%d.\n", player.getName(), player.getMoney());
                        leavingPlayers.add(player);
                    }
                }
            }

            for (Player player : leavingPlayers) {
                players.remove(player);
            }
            System.out.println();
        }
        System.out.println("Thanks for playing!");
    }

    public void dealPlayers() {
        Card card;

        for (Player player : players) {
            card = shoe.dealCard();
            player.getHands().getFirst().addCard(card);
            System.out.printf("%s gets %s.\n", player.getName(), card.getCardString());
        }
    }

    public static void printRules() {
        System.out.println("Objective");
        System.out.println("To get as close to 21 points as you can without going over.");
        System.out.println();
        System.out.println("Scoring");
        System.out.println("Number cards are worth their denominations. All other cards,");
        System.out.println("except aces, are worth ten points. An ace can be counted as");
        System.out.println("one or eleven points.");
        System.out.println();
        System.out.println("Natural Hand");
        System.out.println("When someone is dealt an ace and \"ten\" card they start off");
        System.out.println("at 21. This pays out 150% of their bet.");
        System.out.println();
        System.out.println("Splitting");
        System.out.println("When a player is dealt a pair, they may split them into two");
        System.out.println("hands if they have enough money for a second bet.");
        System.out.println();
        System.out.println("Double Down");
        System.out.println("When a player has a hand worth 9, 10, or 11, they may double");
        System.out.println("their initial bet and take one blind card.");
        System.out.println();
        System.out.println("Order of Play");
        System.out.println("A shoe containing six decks of cards is created and shuffled.");
        System.out.println("A random player is asked to cut the deck by entering a number");
        System.out.println("representing a percentage of the deck to separate. The players");
        System.out.println("are prompted for bets then the if players can split or double down");
        System.out.println("they are prompted. Following that, player's are asked to hit (take");
        System.out.println("another card) or stand (pass). The dealer will then hit until they");
        System.out.println("reach 17 or greater. Bets are then settled.");
    }
}
