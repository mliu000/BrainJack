package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import muyel.model.*;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Tests the Deck class
 */
public class DeckTest {
    
    // Sample deck
    public Deck deck;

    // Gets the instance of the Deck class, or creates a new one if it hasn't been initialized yet. 
    @BeforeEach
    public void initialize() {
        deck = new Deck();
    }

    // Test to see whether or not card deck was constructed properly
    @Test
    public void deckOfCardsConstructionTest() {
        checkDeckState(deck);
    }

    // Tests drawing random card from deck. Also, makes sure that drawn cards are not still in deck
    @Test 
    public void drawRandomCardTest() {
        PokerCard card1 = deck.drawCard();
        PokerCard card2 = deck.drawCard();
        assertEquals(50, deck.getDeckOfCards().size());
        assertFalse(deck.getDeckOfCards().contains(card1));
        assertFalse(deck.getDeckOfCards().contains(card2));

        while (!deck.getDeckOfCards().isEmpty()) {
            deck.drawCard();
        }

        assertNull(deck.drawCard());
    }

    // Tests the method where you return card to deck
    @Test
    public void addCardBackToDeckTest() {
        // Case 1: all cards to put back into deck are not already in the deck
        PokerCard card1 = deck.drawCard();
        PokerCard card2 = deck.drawCard();
        deck.addCardBackToDeck(card2);
        deck.addCardBackToDeck(card1);
        assertTrue(deck.getDeckOfCards().contains(card2));
        assertTrue(deck.getDeckOfCards().contains(card1));
        assertEquals(52, deck.getDeckOfCards().size());
        // Case 2: Trying to add card back into deck that is already there
        deck.addCardBackToDeck(card2);
        assertEquals(52, deck.getDeckOfCards().size());
    }

    // Helper method to check that all cards in the deck are restored
    public static void checkDeckState(Deck deckParam) {
        int diamondCount = 0;
        int cloverCount = 0;
        int heartsCount = 0;
        int spadesCount = 0;
        int numAcount = 0;
        int num2count = 0;
        int num3count = 0;
        int num4count = 0;
        int num5count = 0;
        int num6count = 0;
        int num7count = 0;
        int num8count = 0;
        int num9count = 0;
        int num10count = 0;
        int numJcount = 0;
        int numQcount = 0;
        int numKcount = 0;

        for (PokerCard card: deckParam.getDeckOfCards()) {
            String suite = card.getSuite();
            String number = card.getNumber();
            switch(suite) {
                case "Diamonds":
                    diamondCount++;
                    break;
                case "Hearts":
                    heartsCount++;
                    break;
                case "Clover":
                    cloverCount++;
                    break;
                default: // Case spades
                    spadesCount++;
                    break;

            }

            switch(number) {
                case "A":
                    numAcount++;
                    break;
                case "2":
                    num2count++;
                    break;
                case "3":
                    num3count++;
                    break;
                case "4":
                    num4count++;
                    break;
                case "5":
                    num5count++;
                    break;
                case "6":
                    num6count++;
                    break;
                case "7":
                    num7count++;
                    break;
                case "8":
                    num8count++;
                    break;
                case "9":
                    num9count++;
                    break;
                case "10":
                    num10count++;
                    break;
                case "J":
                    numJcount++;
                    break;
                case "Q":
                    numQcount++;
                    break;
                default: // Case "K"
                    numKcount++;
                    break;
            }
        }

        assertEquals(4, numAcount);
        assertEquals(4, num2count);
        assertEquals(4, num3count);
        assertEquals(4, num4count);
        assertEquals(4, num5count);
        assertEquals(4, num6count);
        assertEquals(4, num7count);
        assertEquals(4, num8count);
        assertEquals(4, num9count);
        assertEquals(4, num10count);
        assertEquals(4, numJcount);
        assertEquals(4, numQcount);
        assertEquals(4, numKcount);
        assertEquals(13, diamondCount);
        assertEquals(13, heartsCount);
        assertEquals(13, spadesCount);
        assertEquals(13, cloverCount);
        assertEquals(52, deckParam.getDeckOfCards().size());
    }
    
}
