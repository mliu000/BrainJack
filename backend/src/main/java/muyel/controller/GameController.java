package muyel.controller;

import muyel.model.*;
import muyel.service.GameService;
import muyel.utility.Pair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blackjack")
@CrossOrigin(origins = "http://localhost:3000")
public class GameController {

    @Autowired
    private GameService gameService;

    ///// METHODS FOR PLAYER OPERATIONS /////

    // Request to create a new player
    @PostMapping("/players/create_player")
    public Player createPlayer(@RequestBody String username, @RequestBody String password) {
        return gameService.createPlayer(username, password);
    }

    // Requests to update password for a player
    @PutMapping("/players/{username}/update_player_password")
    public Pair<Boolean, Player> updatePassword() {
        return null; // stub
    }





    

    // 
}