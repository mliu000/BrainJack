package muyel.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.mvc.method.annotation.UriComponentsBuilderMethodArgumentResolver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.HashMap;
import java.util.List;

import muyel.utility.Pair;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Represents a question bank of 100 arbitrarily scraped multiple choice qnBank.
 */
public class QuestionBank {

    /*
     * Stores the questions in a hashmap for quick retrieval. The key stores the question, while
     * the list of Pair stores the answer and whether or not the answer is correct
     */
    private HashMap<String, List<Pair<String, Boolean>>> qnBank;

    // Constructs a new question bank by web scraping questions from this random site
    public QuestionBank() {
        // Initialize "qnBank" hashmap
        qnBank = new HashMap<>();
        scrapeQuestions();
    }

    // Based on the answer to the question, determines whether or not question is correct
    // Returns 0 if the question is wrong, 1 if correct, -1 for error case where the qn not found
    public int isCorrectAnswer(String question, int answer) {
        List<Pair<String, Boolean>> answers = qnBank.get(question);
        if (answers == null) {
            // Case 1: question could not be located (error case)
            return -1;
        } else if (answers.get(answer).getSecond()) {
            // Case 2: question is answered correctly
            return 1;
        } else {
            // Case 3: question is answered incorrectly
            return 0;
        }
    }

    // HELPER METHOD // Scrapes the qnBank from the site using json

    private void scrapeQuestions() {
        try {
            // Obtain the questions
            URL url = new URL("https://opentdb.com/api.php?amount=100&type=multiple");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Set up the buffered reader than scrape the questions
            
        } catch (Exception e) {
            // I just listed exception because there is a bunch of exceptions possible
        }
    }
    
}
