package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import muyel.model.Player;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Tests the Player class
 */
public class PlayerTest {
    
    // Sample player
    Player player;

    // Password encoder to check password field
    BCryptPasswordEncoder encoder;

    // Constructs a new player for each test
    @BeforeEach
    public void constructPlayer() {
        this.player = new Player("user1", "abcdefg");
        this.encoder = new BCryptPasswordEncoder();
    }

    // Tests the constructor
    @Test
    public void constructorTest() {
        assertEquals("user1", player.getUsername());
        assertTrue(encoder.matches("abcdefg", player.getPassword()));
        checkPlayerFields(0, 0, 0, 0, 0, 0);
    }

    // Tests the setCurrBet method
    @Test
    public void setCurrBetTest() {
        player.setCurrBet(100);
        assertEquals(100, player.getcurrBet());
    }

    // Tests the update statistics method
    @Test
    public void updateStatisticsTest() {
        // Case 1: player wins game
        player.setCurrBet(1000);
        player.updateStatistics(true, true);
        checkPlayerFields(0, 1, 1, 1000, 1000, 100);
        player.resetGameEarnings();
        // Case 2: player loses game, but answers question correctly
        player.setCurrBet(1000);
        player.updateStatistics(false, true);
        checkPlayerFields(0, 2, 1, -500, 500, 50);
        player.resetGameEarnings();
        // Case 3: player loses and answers question incorrectly
        player.setCurrBet(200);
        player.updateStatistics(false, false);
        checkPlayerFields(0, 3, 1, -200, 300, 33.333333);
    }

    // Tests the method where the player leaves the room
    @Test
    public void resetGameEarningsTest() {
        player.setCurrBet(100);
        player.updateStatistics(false, false);
        player.getGameEarnings();
        assertEquals(0, player.getcurrBet());
    }

    ///// HELPER METHODS /////
    
    // Helper method to check the values of the fields of player
    public void checkPlayerFields(int a, int b, int c, int d, int e, double f) {
        assertEquals(a, player.getcurrBet());
        assertEquals(b, player.getRoundsPlayed());
        assertEquals(c, player.getRoundsWon());
        assertEquals(d, player.getGameEarnings());
        assertEquals(e, player.getTotalEarnings());
        assertEquals(f, player.getWinPercentage(), 0.01); // Delta for double comparison
    }

}
