package muyel.model;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Represents a dealer class that extends the participant class. The dealer cannot draw cards 
 * individually, they must draw cards until bust or 17
 */
public class Dealer extends Participant {
    
    /*
     * Creates a new dealer
     * Default constructor (same as participant) because dealer doesn't have any additional fields
     */
    public Dealer() {}

    // Plays hand until it reaches 17
    public void playHand(Deck cardDeck) {
        while (score < 17) {
            drawCard(cardDeck);
        }
    }
    
}