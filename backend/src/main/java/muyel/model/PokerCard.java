package muyel.model;

/*
 * @Mu Ye Liu
 * 
 * Represents the card class for a deck of cards
 */
public class PokerCard {

    // Stores the value of the card in blackjack
    private int value;

    // Stores the SUITE and NUMBER in string format
    private final String SUITE, NUMBER;

    // Constructs a new card
    // REQUIRES: Suites must be one of: "Hearts", "Spaces", "Clover", "Diamonds"
    //           Number must be one of: "2" to "10", "J", "Q", "K"
    public PokerCard(String SUITE, String NUMBER) {
        // Set the SUITE and NUMBER 
        this.SUITE = SUITE;
        this.NUMBER = NUMBER;

        // Set the value of the card
        switch (NUMBER) {
            case "A":
                this.value = 11;
                break;
            case "J":
            case "Q":
            case "K":
                this.value = 10;
                break;
            default: // NUMBER case
                this.value = Integer.parseInt(NUMBER);
                break;
        }
    }

    // Changes the value of A from: true: 11 -> 1, false: 1 -> 11 only if the card is an A
    // Otherwise, do nothing.
    public void changeValueOfA(boolean flag) {
        if (NUMBER.equals("A")) {
            value = flag ? 1 : 11;
        }
    }

    ///// GETTER METHODS /////
    
    public int getValue() { return value; }
    public String getSuite() { return SUITE; }
    public String getNumber() { return NUMBER; }
    
}
