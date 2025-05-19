package muyel.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import muyel.model.Dealer;
import muyel.model.Deck;
import muyel.model.Participant;
import muyel.model.Player;
import muyel.model.Question;
import muyel.model.QuestionBank;
import muyel.respository.PlayerRepository;
import muyel.utility.Pair;

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
     * Only creates player if the number of players logged in is less than 4
     * Returns the player if successfully created, null if not, 
     *
     */
    @Transactional
    public Pair<Boolean, Player> createPlayer(String username, String password) {
        if (PLAYERS_IN_MATCH.size() < 4) {
            boolean distinctUserName = playerRepository.findByUsername(username) == null;
            boolean usernameLengthAppropriate = username.length() >= 4 && username.length() <= 20;
            boolean passwordLengthAppropriate = password.length() >= 4 && password.length() <= 20;

            if (distinctUserName && usernameLengthAppropriate && passwordLengthAppropriate) {
                Player newPlayer = new Player(username, password);
                PLAYERS_IN_MATCH.put(username, newPlayer);
                return new Pair<>(true, playerRepository.save(newPlayer));
            }

            return new Pair<>(true, null);

        }
        return new Pair<>(false, null);
    }

    // THE NEXT 4 METHODS REQUIRE THE PLAYER TO ALREADY BE IN THE ROUND

    // Updates the players statistics
    @Transactional
    public void updatePlayerStatistics(Player playerToUpdate, boolean win, boolean correct) {
        playerToUpdate.updateStatistics(win, correct);
        playerRepository.save(playerToUpdate);
    }

    // Sets the player's bet. Max bet 1 million
    @Transactional
    public void setPlayerBet(Player playerToUpdate, int bet) {
        if (bet > 1000000) {
            playerToUpdate.setCurrBet(1000000);
        } else {
            playerToUpdate.setCurrBet(bet);
        }
    }

    // Player hit draw card
    @Transactional
    public void playerHit(Player playerToUpdate) {
        playerToUpdate.drawCard(DECK);
    }

    // Player login to round
    @Transactional
    public Pair<Integer, Player> playerLogin(String username, String password) {
        Player playerToLogin = playerRepository.findByUsername(username);
        if (playerToLogin == null || !ENCODER.matches(password, playerToLogin.getPassword())) {
            return new Pair<>(-1, null);
        } else if (PLAYERS_IN_MATCH.get(username) != null) {
            return new Pair<>(0, null);
        } else if (PLAYERS_IN_MATCH.size() == 4) {
            return new Pair<>(-2, null);
        } else {
            PLAYERS_IN_MATCH.put(username, playerToLogin);
            return new Pair<>(1, playerToLogin);
        }
    }

    // Player logout
    public boolean playerLogout(String username) {
        Player logoutPlayer = PLAYERS_IN_MATCH.get(username);
        if (logoutPlayer != null) {
            if (!logoutPlayer.getHand().isEmpty()) {
                logoutPlayer.reset(DECK);
            }
            PLAYERS_IN_MATCH.remove(username);
            return true;
        }
        return false;
    }

    ///// INPUT METHODS FOR PARTICIPANTS (BOTH DEALER AND PLAYER) /////

    // Start draw 2 cards, null for dealer. Does nothing if the player hand is not null
    public void participantStartDraw(Participant participantToDraw) {
        if (participantToDraw.getHand().isEmpty()) {
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
    public Deck getDeck() { return DECK; }
}
