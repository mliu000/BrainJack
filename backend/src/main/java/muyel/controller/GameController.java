package muyel.controller;

import muyel.model.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blackjack")
public class GameController {

    private Deck deck;
    private Dealer dealer;
    private List<Player> players;
    

    // Start a new game with the specified number of players
    @PostMapping("/start")
    public String startGame(@RequestParam int numPlayers) {
        // Initialize the deck and players and dealer
        this.dealer = new Dealer();
        this.deck = new Deck();
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player(i));
        }
        
        // Shuffle deck and deal initial cards to players
        dealInitialCards();

        return "Game started with " + numPlayers + " players!";
    }

    // Deal initial cards to each player and the dealer
    private void dealInitialCards() {
        for (Player player : players) {
            player.addCard(deck.drawCard());
            player.addCard(deck.drawCard());
        }
    }

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


    // Get a player's hand by their ID
    @GetMapping("/player/{id}")
    public Player getPlayerHand(@PathVariable int id) {
        return players.stream()
                .filter(player -> player.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    // Hit: Deal one more card to a player
    @PostMapping("/hit/{id}")
    public Player hit(@PathVariable int id) {
        Player player = players.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Player not found"));

        player.addCard(deck.drawCard());
        return player;
    }

    // 
}