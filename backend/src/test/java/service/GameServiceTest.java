package service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
        Player createdPlayer1 = gameService.createPlayer("player1", "password1");
        assertNotNull(createdPlayer1);
        assertEquals(1, playerRepository.count());
        assertEquals(createdPlayer1, playerRepository.findByUsername("player1"));
        assertTrue(gameService.getPlayersInMatch().isEmpty());

        // Case 2: Player not created, username too short
        Player createdPlayer2 = gameService.createPlayer("pl1", "password1");
        assertNull(createdPlayer2);

        // Case 3: Player not created, username too long
        Player createdPlayer3 = gameService.createPlayer("playerplayerplayerplayer1", "password1");
        assertNull(createdPlayer3);

        // Case 4: Player not created, password too short
        Player createdPlayer4 = gameService.createPlayer("player2", "pw1");
        assertNull(createdPlayer4);

        // Case 5: Player not created, password too long
        Player createdPlayer5 = gameService.createPlayer("player3", "passwordpasswordpassword1");
        assertNull(createdPlayer5);

        // Case 6: User already exists
        Player createdPlayer6 = gameService.createPlayer("player1", "password1");
        assertNull(createdPlayer6);
        assertEquals(1, playerRepository.count());

        ///// public void changePlayerPasswordTest()
        // Initialize player database of size 10
        for (int i = 1; i <= 10; i++) {
            gameService.createPlayer("player" + i, "password" + i);
        }

        // Case 1: successfully changes password
        Player player1 = gameService.getPlayer("player1");
        gameService.changePlayerPassword(player1, "nullpassword1");
        assertTrue(gameService.getEncoder().matches("nullpassword1", player1.getPassword()));

        // Case 2: not successfully changed (too short)
        Player player2 = gameService.getPlayer("player2");
        gameService.changePlayerPassword(player2, "np1");
        assertFalse(gameService.getEncoder().matches("np1", player2.getPassword()));

        // Case 3: not successfully changed (too long)
        Player player3 = gameService.getPlayer("player3");
        gameService.changePlayerPassword(player3, "player3player3player3");
        assertFalse(gameService.getEncoder().matches("player3player3player3", player3.getPassword()));

        ///// public void modifyParticipantFieldsTest()
        // setPlayerBet
        Player player4 = gameService.getPlayer("player4");
        gameService.setPlayerBet(player4, 500);
        assertEquals(500, player4.getcurrBet());

        // updatePlayerStatistics
        gameService.updatePlayerStatistics(player4, true, true);
        assertEquals(100, player4.getWinPercentage());
        assertEquals(500, player4.getGameEarnings());

        // resetPlayerCurrEarnings
        gameService.resetPlayerCurrEarnings(player4);
        assertEquals(0, player4.getGameEarnings());

        // playerHit and ParticipantStartDraw (player)
        gameService.participantStartDraw(player4);
        assertEquals(2, player4.getHand().size());
        gameService.playerHit(player4);
        assertEquals(3, player4.getHand().size());

        // participantReset
        gameService.participantReset(player4);
        assertEquals(0, player4.getHand().size());
        assertEquals(0, player4.getcurrBet());

        // participantStartDraw (dealer)
        gameService.participantStartDraw(null);
        assertEquals(2, gameService.getDealer().getHand().size());
        gameService.dealerPlayHand();
        assertTrue(gameService.getDealer().getScore() >= 17);

        ///// public void playerLoginTest()
        // Case 1: Player username not found
        Pair<Integer, Player> result1 = gameService.playerLogin("randominvalid", "ajrj");
        assertEquals(-1, result1.getFirst());
        assertNull(result1.getSecond());

        // Case 2: Player password incorrect
        Pair<Integer, Player> result2 = gameService.playerLogin("player9", "jkjk");
        assertEquals(-1, result2.getFirst());
        assertNull(result2.getSecond());

        // Case 3: Player successfully logs in
        Pair<Integer, Player> result3 = gameService.playerLogin("player9", "password9");
        assertEquals(1, result3.getFirst());
        assertEquals(gameService.getPlayer("player9"), result3.getSecond());
        assertEquals(1, gameService.getPlayersInMatch().size());
        assertNotNull(gameService.getPlayersInMatch().get("player9"));

        // Case 4: Player already logged in
        Pair<Integer, Player> result4 = gameService.playerLogin("player9", "password9");
        assertEquals(0, result4.getFirst());
        assertNull(result4.getSecond());
        assertEquals(1, gameService.getPlayersInMatch().size());

        ///// public void playerLogoutTest()
        // Case 1: Username not found
        boolean logout1 = gameService.playerLogout("badusername");
        assertFalse(logout1);

        // Case 2: Username found
        boolean logout2 = gameService.playerLogout("player9");
        assertTrue(logout2);
        assertEquals(0, gameService.getPlayersInMatch().size());
        assertNull(gameService.getPlayersInMatch().get("player9"));

        ///// public void generateRandomQuestionTest()
        Map.Entry<Integer, Question> question = gameService.getARandomQuestion();
        assertNotNull(question);
        assertTrue(gameService.getQuestionBank().getQnBank().containsKey(question.getKey()));
        assertTrue(gameService.getQuestionBank().getQnBank().containsValue(question.getValue()));
    }
}
