package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import muyel.model.*;

import java.util.List;

public class PlayerTest {
    public Player player;

    @BeforeEach
    public void setUp() {
        player = new Player(1);
    }

    @Test
    public void testConstructorInitializesEmptyHandAndZeroScore() {
        assertTrue(player.getHand().isEmpty());
        assertEquals(0, player.getScore(), "Score should be 0 upon initialization");
        assertEquals(1, player.getId());
        assertEquals(0, player.getBalance());
    }

    @Test
    public void testAddCardUpdatesHandAndScore() {
        PokerCard card = new PokerCard("Hearts", "10"); // Assuming getValue() returns 10
        player.addCard(card);

        List<PokerCard> hand = player.getHand();
        assertEquals(1, hand.size(), "Hand should contain one card");
        assertEquals(10, player.getScore(), "Score should update to the value of the card added");
        assertTrue(hand.contains(card), "Hand should contain the added card");
    }

    @Test
    public void testCalculateScoreWithMultipleCards() {
        player.addCard(new PokerCard("Diamonds", "10")); // Value = 10
        player.addCard(new PokerCard("Spades", "5"));    // Value = 5

        assertEquals(15, player.getScore(), "Score should be the sum of card values");
    }

    @Test
    public void testHandleAcesProperly() {
        player.addCard(new PokerCard("Hearts", "A"));    // Value = 11
        player.addCard(new PokerCard("Clubs", "10"));   // Value = 10
        player.addCard(new PokerCard("Diamonds", "A")); // Value = 11 initially, but adjusted to 1

        assertEquals(12, player.getScore(), "Score should account for one Ace being adjusted to 1");
    }

    @Test
    public void testBustedCondition() {
        player.addCard(new PokerCard("Spades", "10"));
        player.addCard(new PokerCard("Hearts", "9"));
        player.addCard(new PokerCard("Diamonds", "5")); // Total = 24

        assertTrue(player.isBusted(), "Player should be busted when score exceeds 21");
    }

    @Test
    public void testNotBustedCondition() {
        player.addCard(new PokerCard("Spades", "10"));
        player.addCard(new PokerCard("Hearts", "7"));

        assertFalse(player.isBusted(), "Player should not be busted when score is 21 or less");
    }

    @Test
    public void testGetHandReturnsCorrectCards() {
        PokerCard card1 = new PokerCard("Diamonds", "K"); // Value = 10
        PokerCard card2 = new PokerCard("Clover", "7");   // Value = 7
        player.addCard(card1);
        player.addCard(card2);

        List<PokerCard> hand = player.getHand();
        assertEquals(2, hand.size(), "Hand should contain two cards");
        assertTrue(hand.contains(card1) && hand.contains(card2), "Hand should contain the added cards");
    }

    @Test
    public void testChangeBalance() {
        player.changeBalance(100);
        assertEquals(100, player.getBalance());
    }
}
