package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import muyel.model.Question;
import muyel.model.QuestionBank;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Tests the question bank class, also making sure questions are web scraped properly
 */
public class QuestionBankTest {
    
    public QuestionBank qb;

    // Tests the Question bank class in general. Can't create too many instances at once with scraping
    @Test
    public void test() {
        qb = new QuestionBank();
        qb.getRandomGenerator().setSeed(1);
        Map<Integer, Question> qnBank = qb.getQnBank();
        // Checks the size of the hashmap
        assertEquals(50, qnBank.size());
        // Checks to make sure the index id's are consecutive from 0 to 49 inclusive
        for (int i = 0; i < 50; i++) {
            if (qnBank.get(i) == null) {
                fail();
            }
        }
        // Random sample one question. Make sure that the questions is set up properly
        // by making sure that fields are properly instantiated, and the list of answers is 4
        Question sampleQn = qb.getRandomQuestion().getValue();
        assertFalse(sampleQn.getQuestion() == null);
        assertFalse(sampleQn.getCorrectAnswer() == null);
        assertEquals(4, sampleQn.getAnswers().size());
        assertTrue(sampleQn.getAnswers().contains(sampleQn.getCorrectAnswer()));

        // Tests the getRandomQuestion method
        Map.Entry<Integer, Question> randomEntry = qb.getRandomQuestion();
        assertEquals(qb.getQnBank().get(randomEntry.getKey()), randomEntry.getValue());
    }
    
}
