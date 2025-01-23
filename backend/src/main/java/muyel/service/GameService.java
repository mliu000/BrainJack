package muyel.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import muyel.model.*;

@Service
public class GameService {

    // The player database uses a hashmap that stores the username as the key, player object as value 
    @Autowired
    private HashMap<String, Player> playerDataBase;

    // HashSet stores the players currently in match
    @Autowired
    private HashSet<Player> playersInMatch;
    
    // Creates a dealer
    @Autowired
    private Dealer dealer;

    

    // Uses default constructor

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
    


    
}
