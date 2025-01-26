package muyel.model;

/*
 * @Mu Ye Liu
 * 
 * Represents the card class for a deck of cards
 */
public class PokerCard {

    // Stores the value of the card in blackjack
    private int value;

    // Stores the suite and number in string format
    private String suite;
    private String number;

    // Constructs a new card
    // REQUIRES: Suites must be one of: "Hearts", "Spaces", "Clover", "Diamonds"
    //           Number must be one of: "2" to "10", "J", "Q", "K"
    public PokerCard(String suite, String number) {
        // Set the suite and number 
        this.suite = suite;
        this.number = number;

        // Set the value of the card
        switch (number) {
            case "A":
                this.value = 11;
                break;
            case "J":
            case "Q":
            case "K":
                this.value = 10;
                break;
            default: // number case
                this.value = Integer.parseInt(number);
                break;
        }
    }

    // Changes the value of A from: true: 11 -> 1, false: 1 -> 11 only if the card is an A
    // Otherwise, do nothing.
    public void changeValueOfA(boolean flag) {
        if (number.equals("A")) {
            value = flag ? 1 : 11;
        }
    }

    ///// GETTER METHODS /////
    
    public int getValue() { return value; }
    public String getSuite() { return suite; }
    public String getNumber() { return number; }
    
}
