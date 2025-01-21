package muyel.model;

import java.util.ArrayList;
import java.util.List;


public class Player {
    private List<PokerCard> hand;  // List to store player's hand (cards)
    private int score;        // The score of the player's hand
    private int id;
    private int balance;

    // Constructor to initialize the player's hand and score
    public Player(int id) {
        this.id = id;
        this.hand = new ArrayList<>();
        this.score = 0;
        this.balance = 0;
        
    }

    // Method to add a card to the player's hand
    public void addCard(PokerCard card) {
        hand.add(card);
        calculateScore();  // Recalculate score when a new card is added
    }

    // Method to calculate the total score of the player's hand
    public void calculateScore() {
        score = 0;
        int aceCount = 0;

        // Loop through each card and calculate the score
        for (PokerCard card : hand) {
            int cardValue = card.getValue();
            score += cardValue;

            // Count aces to adjust for their value later if necessary
            if (cardValue == 11) {
                aceCount++;
            }
        }

        // Adjust the score for aces (if score exceeds 21, convert aces from 11 to 1)
        while (score > 21 && aceCount > 0) {
            score -= 10;  // Reduce score by 10 for each Ace counted as 11
            aceCount--;
        }
    }

    // Method to check if the player is busted (score exceeds 21)
    public boolean isBusted() {
        return score > 21;
    }

    // Changes the balance
    public void changeBalance(int change) {
        balance += change;
    }

    // Method to get a string representation of the player's hand
    // @Override
    // public String toString() {
    //     StringBuilder handString = new StringBuilder("Player's hand: ");
    //     for (PokerCard card : hand) {
    //         handString.append(card.toString()).append(" ");
    //     }
    //     return handString.toString() + "Score: " + score;
    // }

    ///// GETTER METHODS /////
    
    public int getScore() { return score; }
    public List<PokerCard> getHand() { return hand; }
    public int getId() { return id; }
    public int getBalance() { return balance; }
}
