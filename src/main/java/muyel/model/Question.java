package muyel.model;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * A class that represents a QUESTION, which stores the QUESTION, as well as ANSWERS and whether
 * or not the answer is correct. Is only able to store one correct answer
 */
public class Question {

    // Used for setting seed for testing purposes
    private Random shuffleGenerator;
    
    // The QUESTION prompt. Field cannot change
    private final String QUESTION;

    // Stores the correct and incorrect ANSWERS
    private final String CORRECT_ANSWER;
    private final List<String> ANSWERS;

    /*
     * Constructs a new QUESTION with given QUESTION and ANSWERS
     * 
     * REQUIRES: The correct answer must be in the list of ANSWERS
     */
    public Question(String QUESTION, String CORRECT_ANSWER, List<String> ANSWERS) {
        // Instantiate the fields
        this.shuffleGenerator = new Random();
        this.QUESTION = QUESTION;
        this.CORRECT_ANSWER = CORRECT_ANSWER;
        this.ANSWERS = ANSWERS;
        // Shuffle the ANSWERS
        shuffleAnswers();
    }

    // Shuffle the ANSWERS so we know that the first one is not always the correct one
    public void shuffleAnswers() {
        Collections.shuffle(ANSWERS, shuffleGenerator);
    }

    ///// GETTER METHODS /////
    
    public String getQuestion() { return QUESTION; }
    public String getCorrectAnswer() { return CORRECT_ANSWER; }
    public List<String> getAnswers() { return ANSWERS; }
    public Random getShuffleGenerator() { return shuffleGenerator; }
    
}
