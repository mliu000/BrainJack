package muyel.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import muyel.model.*;
import muyel.utility.Pair;
import muyel.respository.PlayerRepository;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Represents the service class that handles all the cases for user input as well as its validity
 */
@Service
public class GameService {

    // Inject PlayerRepository
    @Autowired
    private PlayerRepository playerRepository;

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
    @Transactional
    public Player createPlayer(String username, String password) {
        boolean distinctUserName = playerRepository.findByUsername(username) == null;
        boolean usernameLengthAppropriate = username.length() >= 4 && username.length() <= 20;
        boolean passwordLengthAppropriate = password.length() >= 4 && password.length() <= 20;

        if (distinctUserName && usernameLengthAppropriate && passwordLengthAppropriate) {
            Player newPlayer = new Player(username, password);
            return playerRepository.save(newPlayer);
        }
        return null;
    }

    /*
     * Changes the password of the player
     * Returns true if password successfully changed. False if not
     */
    @Transactional
    public boolean changePlayerPassword(Player playerToChangePwd, String newPassword) {
        if (newPassword.length() >= 4 && newPassword.length() <= 20) {
            playerToChangePwd.setPassword(newPassword);
            playerRepository.save(playerToChangePwd);
            return true;
        } else {
            return false;
        }
    }

    // THE NEXT 4 METHODS REQUIRE THE PLAYER TO ALREADY BE IN THE ROUND

    // Updates the players statistics
    @Transactional
    public void updatePlayerStatistics(Player playerToUpdate, boolean win, boolean correct) {
        playerToUpdate.updateStatistics(win, correct);
        playerRepository.save(playerToUpdate);
    }

    // Sets the player's bet
    @Transactional
    public void setPlayerBet(Player playerToUpdate, int bet) {
        playerToUpdate.setCurrBet(bet);
        playerRepository.save(playerToUpdate);
    }

    // Reset player curr earning
    @Transactional
    public void resetPlayerCurrEarnings(Player playerToUpdate) {
        playerToUpdate.resetGameEarnings();
        playerRepository.save(playerToUpdate);
    }

    // Player hit draw card
    @Transactional
    public void playerHit(Player playerToUpdate) {
        playerToUpdate.drawCard(DECK);
        playerRepository.save(playerToUpdate);
    }

    // Player login to round
    public Pair<Integer, Player> playerLogin(String username, String password) {
        Player playerToLogin = playerRepository.findByUsername(username);
        if (playerToLogin == null || !ENCODER.matches(password, playerToLogin.getPassword())) {
            return new Pair<>(-1, null);
        } else if (PLAYERS_IN_MATCH.get(username) != null) {
            return new Pair<>(0, null);
        } else {
            PLAYERS_IN_MATCH.put(username, playerToLogin);
            return new Pair<>(1, playerToLogin);
        }
    }

    // Player logout
    public boolean playerLogout(String username) {
        if (PLAYERS_IN_MATCH.get(username) != null) {
            PLAYERS_IN_MATCH.remove(username);
            return true;
        }
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
    public Player getPlayer(String username) { return playerRepository.findByUsername(username); }
    public Dealer getDealer() { return DEALER; }
    public HashMap<String, Player> getPlayersInMatch() { return PLAYERS_IN_MATCH; }
    public QuestionBank getQuestionBank() { return QUESTION_BANK; }
    public BCryptPasswordEncoder getEncoder() { return ENCODER; }
}
