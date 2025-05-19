package service;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import model.DeckTest;
import muyel.Main;
import muyel.model.Player;
import muyel.model.Question;
import muyel.respository.PlayerRepository;
import muyel.service.GameService;
import muyel.utility.Pair;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Tests the service class to make sure it is inputting the correct results
 */
@SpringBootTest(classes = Main.class)
public class GameServiceTest {

    // Instance of gameService Autowired
    @Autowired
    public GameService gameService;

    // Instance of PlayerRepository Autowired
    @Autowired
    private PlayerRepository playerRepository;

    // Tests everything because of API issues
    @Test
    @Transactional
    public void test() {
        gameService.getDeck().getRandomGenerator().setSeed(100);
        // Make sure the autowired fields are not null
        assertNotNull(gameService.getDealer());
        assertNotNull(gameService.getQuestionBank());
        assertNotNull(gameService.getPlayersInMatch());
        assertNotNull(gameService.getEncoder());

        // Ensure the player repository is empty at the beginning
        playerRepository.deleteAll();
        assertTrue(playerRepository.findAll().isEmpty());

        ///// public void createPlayerTest()
        // Case 1: successfully creates player
        Player createdPlayer1 = gameService.createPlayer("player1", "password1").getSecond();
        assertNotNull(createdPlayer1);
        assertEquals(1, playerRepository.count());
        assertEquals(createdPlayer1, playerRepository.findByUsername("player1"));
        assertEquals(1, gameService.getPlayersInMatch().size());

        // Case 2: Player not created, username too short
        Player createdPlayer2 = gameService.createPlayer("pl1", "password1").getSecond();
        assertNull(createdPlayer2);

        // Case 3: Player not created, username too long
        Player createdPlayer3 = gameService.createPlayer("playerplayerplayerplayer1", "password1").getSecond();
        assertNull(createdPlayer3);

        // Case 4: Player not created, password too short
        Player createdPlayer4 = gameService.createPlayer("player2", "pw1").getSecond();
        assertNull(createdPlayer4);

        // Case 5: Player not created, password too long
        Player createdPlayer5 = gameService.createPlayer("player3", "passwordpasswordpassword1").getSecond();
        assertNull(createdPlayer5);

        // Case 6: User already exists
        Player createdPlayer6 = gameService.createPlayer("player1", "password1").getSecond();
        assertNull(createdPlayer6);
        assertEquals(1, playerRepository.count());

        // Case 7: Size > 4
        for (int i = 2; i <= 4; i++) {
            gameService.createPlayer("player" + i, "password" + i);
        }
        assertEquals(4, playerRepository.count());;
        Pair<Boolean, Player> createdPlayerNull5 = gameService.createPlayer("player3", "passwordpasswordpassword1");
        assertNull(createdPlayerNull5.getSecond());
        assertFalse(createdPlayerNull5.getFirst());

        Player player1 = gameService.getPlayersInMatch().get("player1");
        Player player3 = gameService.getPlayersInMatch().get("player3");

        ///// public void modifyParticipantFieldsTest()
        // setPlayerBet
        // Case 1: less than 1 million
        Player player4 = gameService.getPlayer("player4");
        gameService.setPlayerBet(player4, 500);
        assertEquals(500, player4.getcurrBet());
        // Case 2: more than 1 million (caps to 1 million)
        gameService.setPlayerBet(player1, 10000000);
        assertEquals(1000000, player1.getcurrBet());

        // updatePlayerStatistics
        gameService.updatePlayerStatistics(player4, true, true);
        assertEquals(100, player4.getWinPercentage());
        assertEquals(500, player4.getGameEarnings());

        // playerHit and ParticipantStartDraw (player)
        gameService.participantStartDraw(player4);
        assertEquals(2, player4.getHand().size());
        gameService.participantStartDraw(player4);
        assertEquals(2, player4.getHand().size());
        gameService.playerHit(player4);
        assertEquals(3, player4.getHand().size());
        gameService.playerHit(player4);
        assertEquals(4, player4.getHand().size());

        // participantReset (both dealer and player)
        gameService.participantReset(player4);
        assertEquals(0, player4.getHand().size());
        assertEquals(0, player4.getcurrBet());
        gameService.participantStartDraw(gameService.getDealer());
        gameService.dealerPlayHand();
        gameService.participantReset(gameService.getDealer());
        assertTrue(gameService.getDealer().getHand().isEmpty());

        // participantStartDraw (dealer)
        gameService.participantStartDraw(gameService.getDealer());
        assertEquals(2, gameService.getDealer().getHand().size());
        gameService.participantStartDraw(gameService.getDealer());
        assertEquals(2, gameService.getDealer().getHand().size());
        gameService.dealerPlayHand();
        assertTrue(gameService.getDealer().getScore() >= 17);

        ///// public void playerLogoutTest()
        // Case 1: Username not found
        boolean logout1 = gameService.playerLogout("badusername");
        assertFalse(logout1);

        // Case 2: Username found (deck is already empty)
        boolean logout2 = gameService.playerLogout("player4");
        assertTrue(logout2);
        assertEquals(3, gameService.getPlayersInMatch().size());
        assertNull(gameService.getPlayersInMatch().get("player4"));

        // Case 3: Username found (deck is not empty (error case))
        assertNotNull(gameService.getPlayersInMatch().get("player3"));
        gameService.participantStartDraw(player3);
        gameService.playerLogout("player3");
        assertNull(gameService.getPlayersInMatch().get("player3"));
        assertTrue(player3.getHand().isEmpty());

        ///// public void playerLoginTest()
        // Create extra dummy logged out player for case 5
        gameService.createPlayer("player5", "password5").getSecond();
        gameService.playerLogout("player5");

        // Case 1: Player username not found
        Pair<Integer, Player> result1 = gameService.playerLogin("randominvalid", "ajrj");
        assertEquals(-1, result1.getFirst());
        assertNull(result1.getSecond());

        // Case 2: Player password incorrect
        Pair<Integer, Player> result2 = gameService.playerLogin("player4", "jkjk");
        assertEquals(-1, result2.getFirst());
        assertNull(result2.getSecond());

        // Case 3: Player successfully logs in
        Pair<Integer, Player> result3 = gameService.playerLogin("player4", "password4");
        assertEquals(1, result3.getFirst());
        assertEquals(gameService.getPlayer("player4"), result3.getSecond());
        assertEquals(3, gameService.getPlayersInMatch().size());
        assertNotNull(gameService.getPlayersInMatch().get("player4"));

        // Case 4: Player already logged in
        Pair<Integer, Player> result4 = gameService.playerLogin("player4", "password4");
        assertEquals(0, result4.getFirst());
        assertNull(result4.getSecond());
        assertEquals(3, gameService.getPlayersInMatch().size());

        // Case 5: Match is already full
        gameService.playerLogin("player3", "password3");
        Pair<Integer, Player> result5 = gameService.playerLogin("player5", "password5");
        assertEquals(-2, result5.getFirst());
        assertNull(result5.getSecond());
        assertEquals(4, gameService.getPlayersInMatch().size());

        ///// public void generateRandomQuestionTest()
        Map.Entry<Integer, Question> question = gameService.getARandomQuestion();
        assertNotNull(question);
        assertTrue(gameService.getQuestionBank().getQnBank().containsKey(question.getKey()));
        assertTrue(gameService.getQuestionBank().getQnBank().containsValue(question.getValue()));

        // Check the state of the deck after all those tests!
        gameService.getDealer().reset(gameService.getDeck());
        DeckTest.checkDeckState(gameService.getDeck());
    }
}
