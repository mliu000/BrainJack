package muyel.model;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * A class that represents a question, which stores the question, as well as answers and whether
 * or not the answer is correct. Is only able to store one correct answer
 */
public class Question {

    // Used for setting seed for testing purposes
    private Random shuffleGenerator;
    
    // The question prompt. Field cannot change
    private final String question;

    // Stores the correct and incorrect answers
    private final String correctAnswer;
    private final List<String> answers;

    /*
     * Constructs a new question with given question and answers
     * 
     * REQUIRES: The correct answer must be in the list of answers
     */
    public Question(String question, String correctAnswer, List<String> answers) {
        // Instantiate the fields
        this.shuffleGenerator = new Random();
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
        // Shuffle the answers
        shuffleAnswers();
    }

    // Shuffle the answers so we know that the first one is not always the correct one
    public void shuffleAnswers() {
        Collections.shuffle(answers, shuffleGenerator);
    }

    ///// GETTER METHODS /////
    
    public String getQuestion() { return question; }
    public String getCorrectAnswer() { return correctAnswer; }
    public List<String> getAnswers() { return answers; }
    public Random getShuffleGenerator() { return shuffleGenerator; }
    
}
