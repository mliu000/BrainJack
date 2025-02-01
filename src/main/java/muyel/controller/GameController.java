package muyel.controller;

import muyel.model.*;
import muyel.service.GameService;
import muyel.utility.Pair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/api/brainjack")
@CrossOrigin(origins = "http://localhost:8080")
public class GameController {

    @Autowired
    private GameService gameService;

    // Inner class for post request for player
    private static class PlayerRequest {
        public String username;
        public String password;
    } 

    private static class FinishRequest {
        public Boolean win;
        public Boolean correct;
    }

    ///// METHODS FOR PLAYER OPERATIONS /////

    // Request to create a new player and logges them in
    @PostMapping("/players/createPlayer")
    public ResponseEntity<?> createPlayer(@RequestBody PlayerRequest request) {
        Pair<Boolean, Player> result = gameService.createPlayer(request.username, request.password);
        if (result.getFirst() && result.getSecond() == null) {
             // Case 2: player username already exists
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error_code", 4044, "message", "username Already Exists"));
        } else if (!result.getFirst() && result.getSecond() == null ) {
            // Case 3: Lobby already full
            return getPlayerLobbyFullErrorCode();
        }
        // Case 4: successful
        return ResponseEntity.ok(result.getSecond());
    }

    // Gets the list of participants in match
    @GetMapping("/players/getActivePlayers")
    public ResponseEntity<HashSet<String>> getActivePlayers() {
        return ResponseEntity.ok(gameService.getPlayersInMatch());
    }

    // Requests to change password for a player
    @PutMapping("/players/{username}/changePlayerPassword")
    public ResponseEntity<?> changePlayerPassword(@PathVariable String username, 
            @RequestBody String newPassword) {
        // Attempts to find player
        Player playerToChangePwd = gameService.getPlayer(username);
        // Returns error message if Player with given username not found
        if (playerToChangePwd == null) {
            return getPlayerNotFoundErrorCode();
        }
        // If player if found, update the player's statistics and return it
        gameService.changePlayerPassword(playerToChangePwd, newPassword);
        return ResponseEntity.ok(playerToChangePwd);
    }

    // Updates the statistics of the player in game
    @PutMapping("/players/{username}/updatePlayerStatistics")
    public ResponseEntity<?> updatePlayerStatistics(@PathVariable String username, 
            @RequestBody FinishRequest request) {
        // Attempts to find player
        boolean playerInMatch = gameService.getPlayersInMatch().contains(username);
        // Returns error message if Player with given username not in match
        if (!playerInMatch) {
            return getPlayerNotInMatchErrorCode();
        } 
        Player playerToUpdate = gameService.getPlayer(username);
        // If Player is found, update the players statistics and return it
        gameService.updatePlayerStatistics(playerToUpdate, request.win, request.correct);
        return ResponseEntity.ok(playerToUpdate);
    }

    // Sets the bet of the player in game
    @PutMapping("/players/{username}/setPlayerBet")
    public ResponseEntity<?> setPlayerBet(@PathVariable String username, 
            @RequestBody int bet) {
        // Attempts to find player
        boolean playerInMatch = gameService.getPlayersInMatch().contains(username);
        // Returns error message if Player with given username not in match
        if (!playerInMatch) {
            return getPlayerNotInMatchErrorCode();
        } 
        Player playerToUpdate = gameService.getPlayer(username);
        // If Player is found, update the players statistics and return it
        gameService.setPlayerBet(playerToUpdate, bet);
        return ResponseEntity.ok(playerToUpdate);
    }

    // Resets the player's curr earnings for a sequence of matches (player must be in game)
    @PutMapping("/players/{username}/resetPlayerCurrEarnings")
    public ResponseEntity<?> resetPlayerCurrEarnings(@PathVariable String username) {
        // Attempts to find player
        boolean playerInMatch = gameService.getPlayersInMatch().contains(username);
        // Returns error message if Player with given username not in match
        if (!playerInMatch) {
            return getPlayerNotInMatchErrorCode();
        } 
        Player playerToUpdate = gameService.getPlayer(username);
        // If Player is found, update the players statistics and return it
        gameService.resetPlayerCurrEarnings(playerToUpdate);
        return ResponseEntity.ok(playerToUpdate);
    }
 
    // Resets the player's curr earnings for a sequence of matches (player must be in game)
    @PutMapping("/players/{username}/playerHit")
    public ResponseEntity<?> playerHit(@PathVariable String username) {
        // Attempts to find player
        boolean playerInMatch = gameService.getPlayersInMatch().contains(username);
        // Returns error message if Player with given username not in match
        if (!playerInMatch) {
            return getPlayerNotInMatchErrorCode();
        } 
        Player playerToUpdate = gameService.getPlayer(username);
        // If Player is found, update the players statistics and return it
        gameService.playerHit(playerToUpdate);
        return ResponseEntity.ok(playerToUpdate);
    }

    // Logs in player
    @PostMapping("/players/playerLogin")
    public ResponseEntity<?> playerLogin(@RequestBody PlayerRequest request) {
        Pair<Integer, Player> logInStatus = gameService.playerLogin(request.username, request.password);
        if (logInStatus.getFirst() == -1 && logInStatus.getSecond() == null) {
            // Account with usename not found or password incorrect
            return getPlayerNotFoundErrorCode();
        } else if (logInStatus.getFirst() == 0 && logInStatus.getSecond() == null) {
            // Account already logged in 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error_code", 4001, "message", "Player already logged in"));
        } else if (logInStatus.getFirst() == -2 && logInStatus.getSecond() == null) {
            return getPlayerLobbyFullErrorCode();
        }

        return ResponseEntity.ok(logInStatus.getSecond());
    }

    // Logs player out 
    @PostMapping("/players/playerLogout")
    public ResponseEntity<?> playerLogout(@RequestBody String username) {
        boolean response = gameService.playerLogout(username);
        if (!response) {
            // Account cannot be found in match
            return getPlayerNotFoundErrorCode();
        }

        return ResponseEntity.ok(true);
    }

    // Logs out player
    //@PostMapping("/players/playerLogout")
    //public ResponseEntity<

    ///// INPUT METHODS FOR BOTH PLAYER AND DEALER OPERATIONS (PARTICIPANT SUPERCLASS) /////
    
    /*
     * Draw 2 cards to start match for both dealer and player. 
     * username = "" for dealer, actual username for player
     */ 
    @PutMapping("/participant/{username}/participantStartDraw")
    public ResponseEntity<?> participantStartDraw(@PathVariable String username) {
        if (username.isEmpty()) {
            // Case 1: string is empty so we will proceed with dealer
            Dealer dealerToUpdate = gameService.getDealer();
            gameService.participantStartDraw(null);
            return ResponseEntity.ok(dealerToUpdate);
        } else {
            // Case 2: string is not empty so we will attempt to find the 
            // Attempts to find player
            boolean playerInMatch = gameService.getPlayersInMatch().contains(username);
            // Returns error message if Player with given username not in match
            if (!playerInMatch) {
                return getPlayerNotInMatchErrorCode();
            } 
            Player playerToUpdate = gameService.getPlayer(username);
            // If Player is found, update the players statistics and return it
            gameService.participantStartDraw(playerToUpdate);
            return ResponseEntity.ok(playerToUpdate);
        } 
    }

    /*
     * Resets the participant after round
     * username = "" for dealer, actual username for player
     */ 
    @PutMapping("/participant/{username}/participantReset")
    public ResponseEntity<?> participantReset(@PathVariable String username) {
        if (username.isEmpty()) {
            // Case 1: string is empty so we will proceed with dealer
            Dealer dealerToUpdate = gameService.getDealer();
            gameService.participantReset(dealerToUpdate);
            return ResponseEntity.ok(dealerToUpdate);
        } else {
            // Case 2: string is not empty so we will attempt to find the 
            // Attempts to find player
            boolean playerInMatch = gameService.getPlayersInMatch().contains(username);
            // Returns error message if Player with given username not in match
            if (!playerInMatch) {
                return getPlayerNotInMatchErrorCode();
            } 
            Player playerToUpdate = gameService.getPlayer(username);
            // If Player is found, update the players statistics and return it
            gameService.participantReset(playerToUpdate);;
            return ResponseEntity.ok(playerToUpdate);
        } 
    }

    ///// INPUT METHODS FOR DEALER ONLY /////
    
    @PutMapping("/dealerPlayHand") 
    public ResponseEntity<Dealer> dealerPlanHand() {
        gameService.dealerPlayHand();
        return ResponseEntity.ok(gameService.getDealer());
    }

    //// INPUT METHOD FOR QUESTION BANK /////
    
    @GetMapping("/getRandomQuestion") 
    public ResponseEntity<Question> getRandomQuestion() {
        return ResponseEntity.ok(gameService.getARandomQuestion().getValue());
    }
    
    ///// HELPER METHODS /////
    
    // Method to create "Player not found" error responseEntity (since it is very repetitive)
    private ResponseEntity<?> getPlayerNotFoundErrorCode() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error_code", 4041, "message", "Player not found"));
    }

    // Method to create "Player not in match" error responseEntity
    private ResponseEntity<?> getPlayerNotInMatchErrorCode() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error_code", 4042, "message", "Player not in match"));
    }

    // Method to create "Player not in match" error responseEntity
    private ResponseEntity<?> getPlayerLobbyFullErrorCode() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error_code", 4043, "message", "Match is full"));
    }
}