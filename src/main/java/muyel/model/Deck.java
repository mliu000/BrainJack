package muyel.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Represents a deck of 52 cards
 */
public class Deck {

    // Random number generator that generates an array index that allows retrieval and removal of 
    // Randomly generated card
    private Random randomGenerator;
    
    // List that stores the cards. 
    private List<PokerCard> deckOfCards;

    // Constructs a new deck of 52 cards
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

    /*
     * Adds card back to deck. If card is already in deck, do nothing
     * Although the runtime per operation is O(n), it is negligible because n is bounded by 52
     *
     * REQUIRES: Card is originally part of deck
     */
    public void addCardBackToDeck(PokerCard card) {
        if (!deckOfCards.contains(card)) {
            deckOfCards.add(card);
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
    public Random getRandomGenerator() { return randomGenerator; }

}   
