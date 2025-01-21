package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import muyel.model.*;

public class DeckTest {
    
    public Deck instance1;

    // Gets the instance of the Deck class, or creates a new one if it hasn't been initialized yet. 
    @BeforeEach
    public void initialize() {
        instance1 = new Deck();
    }

    // Test to see whether or not card deck was constructed properly
    @Test
    public void deckOfCardsConstructionTest() {
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

        for (PokerCard card: instance1.getDeckOfCards()) {
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
        assertEquals(52, instance1.getDeckOfCards().size());
    }

    // Tests drawing random card from deck
    @Test 
    public void drawRandomCardTest() {
        instance1.drawCard();
        instance1.drawCard();
        assertEquals(50, instance1.getDeckOfCards().size());

        while (!instance1.getDeckOfCards().isEmpty()) {
            instance1.drawCard();
        }

        assertNull(instance1.drawCard());
    }

}
