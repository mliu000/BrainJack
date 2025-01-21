package muyel.model;

public class Dealer extends Player {
    
    // Constructor to initialize the dealer (uses Player's constructor)
    public Dealer() {
        super(0);
    }

    // Method for the dealer to play their turn
    // REQUIRES: this method can only be run after the dealer already has 2 cards. 
    public void playTurn(Deck deck) {
        // Dealer must draw cards until their score is 17 or higher
        while (getScore() < 17) {
            // Draw a card from the deck and add it to the dealer's hand
            PokerCard drawnCard = deck.drawCard();
            addCard(drawnCard);  // Add card to hand and recalculate score
        }
    }

    // Optional: Add a custom toString method to differentiate between player and dealer
    // @Override
    // public String toString() {
    //     return "Dealer's hand: " + super.toString();
    // }
}