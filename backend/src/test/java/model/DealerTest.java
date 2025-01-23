package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import muyel.model.*;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Tests the Dealer class
 */
public class DealerTest {
    
    // To test the dealer, we need both a dealer and a deck. 
    // To control randomness, set a seed to the deck.
    Dealer dealer;
    Deck deck;

    // Constructs a new dealer and deck to check for randomness
    @BeforeEach
    public void constructDealer() {
        this.dealer = new Dealer();
        this.deck = new Deck();
        this.deck.getRandomGenerator().setSeed(52);
    }

    // Tests the dealer constructor
    @Test
    public void constructorTest() {
        assertEquals(0, dealer.getScore());
        assertFalse(dealer.getBusted());
        assertTrue(dealer.getHand().isEmpty());
    }

    /*
     * Tests the method the draws 2 cards at the beginning. 
     * Makes sure that the dealer has 2 cards, and checks the score of the dealer
     * Also makes sure that the cards in the dealer's hands are no longer in the deck
     */
    @Test
    public void startDrawTest() {
        // Checks the fields size and busted status of dealer (busted should be false)
        dealer.startDraw(deck);
        assertEquals(2, dealer.getHand().size());
        assertFalse(dealer.getBusted());
        // Makes sure no cards in dealer's hands are still in deck
        for (PokerCard card: dealer.getHand()) {
            assertFalse(deck.getDeckOfCards().contains(card));
        }
        // (10 hearts and 6 hearts)
        assertEquals(16, dealer.getScore());

    }

    /*
     * Tests the playHand method for the dealer
     */
    @Test
    public void playHandTest() {
        // Checks the dealer's status
        dealer.startDraw(deck);
        assertFalse(dealer.getBusted()); 
        dealer.playHand(deck);
        assertTrue(dealer.getScore() > 17);
        // In our seed case, the dealer is busted with score = 24, so the busted should be true
        assertTrue(dealer.getScore() > 21 && dealer.getBusted());
        // Check to make sure that 
        for (PokerCard card: dealer.getHand()) {
            assertFalse(deck.getDeckOfCards().contains(card));
        }
        // Checks the score (6 hearts, 10 hearts and 8 diamonds)
        assertEquals(24, dealer.getScore());
    }

    /*
     * Tests the reset method for the dealer, which ensures that all their cards are returned to 
     * deck, and the dealer's score 0 and hand is empty. Also, tests to make sure the A is 
     * restored from 1 to 11
     */
    @Test
    public void resetTest() {
        deck.getRandomGenerator().setSeed(15);
        dealer.startDraw(deck);
        dealer.playHand(deck); // CARDS: A, A, 2, 6 where first A is 11, second A is 1
        dealer.reset(deck);
        // Check dealer fields
        assertTrue(dealer.getHand().isEmpty());
        assertEquals(0, dealer.getScore());
        // Check to make sure that card is reinitialized properly
        DeckTest.checkDeckState(deck);
        // Check to make sure that all A cards are 11
        for (PokerCard card: deck.getDeckOfCards()) {
            if (card.getNumber().equals("A")) {
                assertEquals(11, card.getValue());
            }
        }
    }

}
