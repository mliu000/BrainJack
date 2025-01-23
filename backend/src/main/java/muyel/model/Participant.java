package muyel.model;

import java.util.List;
import java.util.ArrayList;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Represnts a participant abstract class in java, which will be extended by the Player and Dealer 
 * classes
 */
public abstract class Participant {
    
    // The player's current hand, as well its current score.
    protected List<PokerCard> hand; 
    protected int score;
    protected boolean busted;
    

    // Constructs a new participant with 0 game balance, 0 score, and empty hand, and not busted
    public Participant() {
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    // At the start of a game, draw 2 cards for both the dealer and the player
    public void startDraw(Deck cardDeck) {
        // Simply call draw card twice
        drawCard(cardDeck);
        drawCard(cardDeck);
    }

    /*
     * Draws a new card, and updates score, and check if the player is busted
     * 
     * REQUIRES: Only a player can call this method. A dealer must call playHand() in subclass
     */
    public void drawCard(Deck cardDeck) {
        PokerCard card = cardDeck.drawCard();
        hand.add(card);
        updateScore(card);
        setBusted();
    }

    /*
     * After each round, clear player's hands, score to 0 and busted status to false
     * 
     * First, Iterate through all the players cards. If there are A's, check to see if they are
     * value 1. If so, change value back to 11
     */
    public void reset(Deck cardDeck) {
        for (PokerCard card: hand) {
            if (card.getNumber().equals("A") && card.getValue() == 1) {
                card.changeValueOfA(false);
            }
            cardDeck.addCardBackToDeck(card);
        }

        hand.clear();
        busted = false;
        score = 0;
    }

    ///// HELPER METHODS /////

    /*
     * Updates the score based on the card. If the card is an ace and you are about to bust when the 
     * value is 11, change value to 1
     * 
     * If you are about to bust, iterate through hand, then check if there are any A with value 11
     * If there are A, then set the value to 1 (if there are multiple, just set the first one)
     * 
     * REQUIRES: New drawn card must be added to hand first
     */
    private void updateScore(PokerCard card) {
        score += card.getValue();

        if (score > 21) {
            for (PokerCard currCard: hand) {
                if (currCard.getNumber().equals("A") && currCard.getValue() == 11) {
                    card.changeValueOfA(true);
                    score -= 10;
                    break;
                }
            }
        }
    }

    // Updates the busted status based on the current score
    private void setBusted() {
        busted = score > 21;
    }
 

    ///// GETTER METHODS /////

    public List<PokerCard> getHand() { return hand; }
    public int getScore() { return score; }
    public boolean getBusted() { return busted; }

}
