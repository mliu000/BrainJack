package muyel.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import muyel.model.*;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Represents the service class that handles all the cases for user input as well as its validity
 */
@Service
public class GameService {

    // The player database uses a hashmap that stores the username as the key, player object as value 
    @Autowired
    private HashMap<String, Player> playerDataBase;

    // Stores the instance of the question bank
    @Autowired
    private QuestionBank questionBank;

    // List stores the players currently in match
    @Autowired
    private ArrayList<Player> playersInMatch;
    
    // Creates a dealer
    @Autowired
    private Dealer dealer;

    // Creates a deck
    @Autowired
    private Deck deck;

    // Uses default constructor

    ///// INPUT METHODS FOR PARTICIPANTS (PLAYER AND DEALER) /////
    
    /*
     * Creates a new player, but checks the username and password first
     * The username and password must both be between 4-20 chars long. 
     * The username must not exist already
     * 
     * Returns the player if successfully created, null if not
     * 
     */
    public Player createPlayer(String username, String password) {
        // Only creates a new player if requirements above are met
        boolean distinctUserName = playerDataBase.get(username) == null;
        boolean usernameLengthAppropriate = username.length() >= 4 && username.length() <= 20;
        boolean passwordLengthAppropriate = password.length() >= 4 && password.length() <= 20;

        if (distinctUserName && usernameLengthAppropriate && passwordLengthAppropriate) {
            Player newPlayer = new Player(username, password);
            playerDataBase.put(username, newPlayer);
            return newPlayer;
        }
        // Return null if requirements are not met
        return null; 
    }

    /* 
     * Changes the password of the player 
     * Returns true if password successfully changed. False if not
     */
    public boolean changePlayerPassword(Player playerToChangePwd, String newPassword) {
        if (newPassword.length() >= 4 && newPassword.length() <= 20) {
            // Case 1: Password Successfully changes
            playerToChangePwd.setPassword(newPassword);
            return true;
        } else {
            // Case 2: Password not successfully changed
            return false;
        } 
    }

    // Updates the players statistics
    public void updatePlayerStatistics(Player playerToUpdate, boolean win, boolean correct) {
        playerToUpdate.updateStatistics(win, correct);
    }

    // Sets the player's bet
    public void setPlayerBet(Player playerToUpdate, int bet) {
        playerToUpdate.setCurrBet(bet);
    }

    // Reset player curr earning
    public void resetPlayerCurrEarnings(Player playerToUpdate) {
        playerToUpdate.resetGameEarnings();
    }

    // Player hit draw card
    public void playerHit(Player playerToUpdate) {
        playerToUpdate.drawCard(deck);
    }

    ///// INPUT METHODS FOR PARTICIPANTS (BOTH DEALER AND PLAYER) /////
    
    // Start draw 2 cards
    public void participantStartDraw(Participant participantToDraw) {
        participantToDraw.drawCard(deck);
    }

    // Resets the participants after they finish
    public void participantReset(Participant participantToReset) {
        participantToReset.reset(deck);
    }

    ///// INPUT METHODS FOR DEALER ONLY /////
    
    public void dealerPlayHand() {
        dealer.playHand(deck);
    }

    ///// INPUT METHODS FOR THE QUESTION BANK /////

    ///// GETTER METHODS /////

    // Returns the requested player by username, null if no player with inputted username exists
    public Player getPlayer(String username) {
        return playerDataBase.get(username);
    }

    public HashMap<String, Player> getPlayerDatabase() { return playerDataBase; }
    public Dealer getDealer() { return dealer; }
    public ArrayList<Player> getPlayersInMatch() { return playersInMatch; } 
    public QuestionBank getQuestionBank() { return questionBank; }

}
