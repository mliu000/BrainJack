package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import muyel.model.*;

// Tests the dealer subclass
public class DealerTest {

    Deck deck;

    // Tests the dealer subclass. 
    @Test 
    public void dealerTest() {
        deck = new Deck();
        Dealer dealer = new Dealer();
        dealer.addCard(new PokerCard("Diamonds", "2"));
        dealer.addCard(new PokerCard("Diamonds", "4"));
        dealer.addCard(new PokerCard("Diamonds", "3")); 
        assertEquals(9, dealer.getScore());
        assertEquals(0, dealer.getId());

        dealer.playTurn(deck);

        assertTrue(dealer.getScore() >= 17);

    }


}
