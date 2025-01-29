package muyel.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.Id;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Represnts a player class in blackjack. The player has the additional accumulators that show career 
 * statistics of the player throughout their lifetime
 */
@Entity
public class Player extends Participant {
    // Fields to store personal information, such as username, password.
    @Id
    private String username;

    private String password; 

    // Stores the current bet of the player
    private int currBet;

    // Accumulators to store account totals, like rounds played, etc
    private int roundsPlayed, roundsWon, totalEarnings, gameEarnings;
    private double winPercentage;

    // Default constructor for api purposes (Do not call)
    public Player() {}

    // constructs a new player with given username and password, and default accumulators
    public Player(String username, String password) {
        this.username = username;
        setPassword(password);
        this.gameEarnings = 0;
        this.totalEarnings = 0;
        this.roundsPlayed = 0;
        this.roundsWon = 0;
        this.currBet = 0;
        this.winPercentage = 0;
    }

    /*
     * Updates the player's stats after they finish a round (after a win or loss).
     * If the player loses, they will get to answer a question. If they answer it correctly, only 
     * lost half of their bet. If they answer it incorrectly, lose all their bet
     * 
     * Parameter result: true if the player wins, false if the player loses.
     * Parameter correct: true if player answers question correctly, false if incorrect
     */
    public void updateStatistics(boolean result, boolean correct) {
        roundsPlayed++;
        if (result) {
            // Win case
            roundsWon++;
            gameEarnings += currBet;
            totalEarnings += currBet;
        } else {
            // Lose case
            if (correct) {
                // Case: answers question correctly
                gameEarnings -= currBet / 2;
                totalEarnings -= currBet / 2;
            } else {
                // Case: answers question incorrectly
                gameEarnings -= currBet;
                totalEarnings -= currBet;
            }
        }
        // Reset the current bet and update the win percentage
        currBet = 0;
        winPercentage = ((double) roundsWon / (double) roundsPlayed)*100;
    }

    /*
     * Changes the password upon request. First hashes it to ensure security
     * 
     * REQUIRES: The password string be between 4-15 chars long
     */
    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }

    // Reset game earnings (to be called once a player leaves the lobby)
    public void resetGameEarnings() {
        this.gameEarnings = 0;
    }

    // Sets bet 
    public void setCurrBet(int bet) {
        this.currBet = bet;
    }

    ///// GETTER METHODS /////
    
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getGameEarnings() { return gameEarnings; }
    public int getRoundsPlayed() { return roundsPlayed; }
    public int getRoundsWon() { return roundsWon; }
    public int getTotalEarnings() { return totalEarnings; }
    public int getcurrBet() { return currBet; }
    public double getWinPercentage() { return winPercentage; }
}
