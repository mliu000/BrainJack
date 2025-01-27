package muyel.service;

import java.util.HashMap;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import muyel.model.*;

@Service
public class GameService {

    // The player database uses a hashmap that stores the username as the key, player object as value 
    @Autowired
    private HashMap<String, Player> playerDataBase;

    // Stores the instance of the question bank
    @Autowired
    private QuestionBank questionBank;

    // HashSet stores the players currently in match
    @Autowired
    private Set<Player> playersInMatch;
    
    // Creates a dealer
    @Autowired
    private Dealer dealer;

    // Uses default constructor

    ///// INPUT METHODS FOR PLAYER AND DEALER /////
    
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
     * Returns true if password changed, false if not
     */
    public boolean changePlayerPassword(String userName, String newPassword) {
        Player playerToChangePwd = playerDataBase.get(newPassword);
        if (playerToChangePwd == null || !(newPassword.length() >= 4 && newPassword.length() <= 20)) {
            // Case 1 and 2: Account with given username not found, and password does not meet reqs
            return false;
        } else {
            // Case 2: Password successfully changes
            playerToChangePwd.setPassword(newPassword);
            return true;
        } 
    }

    ///// INPUT METHODS FOR THE QUESTION BANK /////
    


    
}
