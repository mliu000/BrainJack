package muyel.controller;

import muyel.model.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blackjack")
public class GameController {

    //private Deck deck;
    private Dealer dealer;
    private List<Player> players;
    

    // Get the current state of all players' hands
    @GetMapping("/players")
    public List<Player> getPlayers() {
        return players;
    }

    // Get the current state of all players' hands
    @GetMapping("/players")
    public Dealer getDealer() {
        return dealer;
    }


    

    // 
}