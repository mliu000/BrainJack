package muyel.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import muyel.model.*;
import muyel.utility.Pair;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Represents the service class that handles all the cases for user input as well as its validity
 */
@Service
public class GameService {

    // The player database uses a hashmap that stores the username as the key, player object as value 
    private final HashMap<String, Player> PLAYER_DATABASE;

    // Stores the instance of the question bank
    private final QuestionBank QUESTION_BANK;

    // List stores the players currently in match
    private final HashMap<String, Player> PLAYERS_IN_MATCH;
    
    // Creates a DEALER
    private final Dealer DEALER;

    // Creates a DECK
    private final Deck DECK;

    // The password encoder for login checking
    private final BCryptPasswordEncoder ENCODER;

    // Constructs a new game service
    public GameService() {
        this.PLAYER_DATABASE = new HashMap<>();
        this.QUESTION_BANK = new QuestionBank();
        this.PLAYERS_IN_MATCH = new HashMap<>();
        this.DEALER = new Dealer();
        this.DECK = new Deck();
        this.ENCODER = new BCryptPasswordEncoder();
    }

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
        boolean distinctUserName = PLAYER_DATABASE.get(username) == null;
        boolean usernameLengthAppropriate = username.length() >= 4 && username.length() <= 20;
        boolean passwordLengthAppropriate = password.length() >= 4 && password.length() <= 20;

        if (distinctUserName && usernameLengthAppropriate && passwordLengthAppropriate) {
            Player newPlayer = new Player(username, password);
            PLAYER_DATABASE.put(username, newPlayer);
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

    // THE NEXT 4 METHODS REQUIRES THE PLAYER TO ALREADY BE IN THE ROUND

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
        playerToUpdate.drawCard(DECK);
    }

    // Player login to round
    public Pair<Integer, Player> playerLogin(String username, String password) {
        Player playerToLogin = PLAYER_DATABASE.get(username);
        if (playerToLogin == null || !ENCODER.matches(password, playerToLogin.getPassword())) {
            // Case 1: player not found or the password is incorrect
            return new Pair<Integer,Player>(-1, null);
        } else if (PLAYERS_IN_MATCH.get(username) != null) {
            // Case 2: player is already logged in 
            return new Pair<Integer,Player>(0, null);
        } else {
            // Case 3: player not already logged in successfully logges in 
            PLAYERS_IN_MATCH.put(username, playerToLogin);
            return new Pair<Integer,Player>(1, playerToLogin);
        }
    }


    // Player logout
    public boolean playerLogout(String username) {
        // Player can be found, do log out and return true
        if (PLAYERS_IN_MATCH.get(username) != null) {
            PLAYERS_IN_MATCH.remove(username);
            return true;
        }
        // Player cannot be found, so do nothing and return false
        return false; 
    }


    ///// INPUT METHODS FOR PARTICIPANTS (BOTH DEALER AND PLAYER) /////
    
    // Start draw 2 cards, null for dealer
    public void participantStartDraw(Player participantToDraw) {
        if (participantToDraw == null) {
            DEALER.startDraw(DECK);
        } else {
            participantToDraw.startDraw(DECK);
        }
    }

    // Resets the participants after they finish
    public void participantReset(Participant participantToReset) {
        participantToReset.reset(DECK);
    }

    ///// INPUT METHODS FOR DEALER ONLY /////
    
    public void dealerPlayHand() {
        DEALER.playHand(DECK);
    }

    ///// INPUT METHODS FOR THE QUESTION BANK /////
    
    public Map.Entry<Integer, Question> getARandomQuestion() {
        return QUESTION_BANK.getRandomQuestion();
    }

    ///// GETTER METHODS /////

    // Returns the requested player by username, null if no player with inputted username exists
    public Player getPlayer(String username) {
        return PLAYER_DATABASE.get(username);
    }

    public HashMap<String, Player> getPlayerDatabase() { return PLAYER_DATABASE; }
    public Dealer getDealer() { return DEALER; }
    public HashMap<String, Player> getPlayersInMatch() { return PLAYERS_IN_MATCH; } 
    public QuestionBank getQuestionBank() { return QUESTION_BANK; }
    public BCryptPasswordEncoder getEncoder() { return ENCODER; }

}
