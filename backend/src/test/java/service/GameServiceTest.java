package service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import muyel.Main;
import muyel.model.Dealer;
import muyel.model.Deck;
import muyel.model.Player;
import muyel.model.QuestionBank;
import muyel.service.GameService;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Tests the service class to make sure it is inputting the correct results
 */
@SpringBootTest(classes = Main.class)
public class GameServiceTest {
    
    // 2 instances of gameService to resolve the issue with api limits
    @Autowired
    public GameService gameService;

    @Test
    public void constructorTest() {
        // Make sure the autowired fields are not null
        assertNotNull(gameService.getDealer());
        assertNotNull(gameService.getPlayerDatabase());
        assertNotNull(gameService.getQuestionBank());
        assertNotNull(gameService.getPlayersInMatch());
        assertTrue(gameService.getPlayerDatabase().size() == 0);
        assertTrue(gameService.getPlayersInMatch().size() == 0);
    }
}
