package muyel.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Represents the deck of 52 poker cards. Singleton because we only need one card deck to play. 
// Card aesthetics customization could be done in the front end
public class Deck {

    // Random number generator
    private Random randomGenerator;
    
    // Hashset stores the deck of cards. We chose a hashset because we don't care about the order.
    // Also, it has a faster runtime for lookup and retrieval
    private List<PokerCard> deckOfCards;

    // Private constructor constructs a new deck of 52 poker cards. 
    public Deck() {
        this.randomGenerator = new Random();
        this.deckOfCards = new ArrayList<>();

        // Create and iterate over numbers to create the cards, then store them in the hashset
        String[] suites = {"Diamonds", "Clover", "Hearts", "Spades"};
        String[] numbers = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for (String suite: suites) {
            for (String number: numbers) {
                deckOfCards.add(new PokerCard(suite, number));
            } 
        }
    }

    // Draws a random card
    public PokerCard drawCard() {
        // Return null if the deck of cards is empty, which shouldn't be
        if (deckOfCards.isEmpty()) {
            return null; // or throw an exception if preferred
        }

        // Generate random number, and retrieve the card associated with the index
        int indexOfCard = randomGenerator.nextInt(deckOfCards.size());
        PokerCard card = deckOfCards.get(indexOfCard);
        // Remove the card from the random deck of cards
        deckOfCards.remove(indexOfCard);

        return card;
    }

    ///// GETTER METHODS /////  
    
    public List<PokerCard> getDeckOfCards() { return deckOfCards; }

}   
