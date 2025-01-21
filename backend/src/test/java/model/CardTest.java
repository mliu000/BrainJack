package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import muyel.model.*;


// placeholder rename this once we start implementation
public class CardTest {
    public PokerCard cardA;
    public PokerCard card2;
    public PokerCard cardJ;
    public PokerCard cardQ;
    public PokerCard cardK;

    // Creates and initializes cards
    @BeforeEach
    public void createCards() {
        cardA = new PokerCard("Diamonds", "A");
        card2 = new PokerCard("Hearts", "2");
        cardJ = new PokerCard("Spades", "J");
        cardQ = new PokerCard("Clover", "Q");
        cardK = new PokerCard("Diamonds", "K");
    }

    
    // Tests the constructor
    @Test
    public void constructorTest() {
        assertEquals("Diamonds", cardA.getSuite());
        assertEquals("A", cardA.getNumber());
        assertEquals(11, cardA.getValue());

        assertEquals(2, card2.getValue());
        assertEquals(10, cardJ.getValue());
        assertEquals(10, cardK.getValue());
        assertEquals(10, cardK.getValue());
    }

    // Tests changing the value of A, both from 1 to 11 and vice versa.
    // Also tests not changing the value for any other card
    @Test
    public void changeValueOfATest() {
        // Attempt to change non A card
        card2.changeValueOfA(true);
        assertEquals(2, card2.getValue());

        // Change value of A from 1 -> 11
        cardA.changeValueOfA(true);
        assertEquals(1, cardA.getValue());

        // Change value of A from 11 -> 11
        cardA.changeValueOfA(false);
        assertEquals(11, cardA.getValue());
    }

}
