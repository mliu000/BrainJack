package model;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import muyel.model.Question;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Tests the question class
 */
public class QuestionTest {

    public Question question;
    public List<String> plannedAnswers;

    // Constructs a new question
    @BeforeEach
    public void constructQuestion() {
        plannedAnswers = new ArrayList<>();
        plannedAnswers.add("me");
        plannedAnswers.add("you");
        plannedAnswers.add("he");
        plannedAnswers.add("she");
        question = new Question("Who is this", "me", new ArrayList<>(plannedAnswers));
        // Set seed to ensure reproducibility
        question.getShuffleGenerator().setSeed(1);
    }

    // Tests the construction of the question
    @Test
    public void constructorTest() {
        List<String> qnAnswers = question.getAnswers();

        // Check fields
        assertEquals("Who is this", question.getQuestion());
        assertEquals("me", question.getCorrectAnswer());
        assertEquals(4, qnAnswers.size());

        // Check to make sure it is shuffled
        boolean shuffled = false;
        for (int i = 0; i < plannedAnswers.size(); i++) {
            if (!plannedAnswers.get(i).equals(qnAnswers.get(i))) {
                shuffled = true;
                break;
            }
        }
        assertTrue(shuffled);
    }
    
}
