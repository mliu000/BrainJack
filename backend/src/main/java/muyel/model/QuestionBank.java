package muyel.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Represents a question bank of 100 arbitrarily scraped multiple choice qnBank.
 */
public class QuestionBank {

    /*
     * Stores the questions in a hashmap for quick retrieval. The key stores the integer id 
     * of the question which can be used to randomly pick questions (but they must be in 
     * consecutive order from 0 to n-1), while the custom question class stores the questions, 
     * correct and incorrect answers. This approach allows O(1) lookup retrieve and lookup runtime
     */
    private Map<Integer, Question> qnBank;

    // Random generator
    private Random randomGenerator;

    // Constructs a new question bank by web scraping questions from this random site
    public QuestionBank() {
        // Initialize "qnBank" hashmap
        this.qnBank = new HashMap<>();
        this.randomGenerator = new Random();
        scrapeQuestions();
    }

    /*
     * Based on the answer to the question, determines whether or not question is correct
     * Returns 0 if the question is wrong, 1 if correct, -1 for error case where the qn not found
     * 
     * REQUIRES: The question Passed in from the frontend must be the same as the original question
     *           The answer passed from the frontend must also be the same as the original id
     */
    public int isCorrectAnswer(int id, String answer) {
        Question qn = qnBank.get(id);
        if (qn == null) {
            // Case 1: question could not be located (error case)
            return -1;
        } else if (qn.getCorrectAnswer().equals(answer)) {
            // Case 2: answer correct
            return 1; 
        } else {
            // Case 3: answer incorrect
            return 0;
        }
    }

    /*
     * Gets a random question from bank where you shuffle the list of Pairs around in different
     * order before returning it.
     */
    public Map.Entry<Integer, Question> getRandomQuestion() {
        // Get a random integer from 0 to size of qnBank (50)
        int randomId = randomGenerator.nextInt(qnBank.size());
        Question randomQn = qnBank.get(randomId);
        randomQn.shuffleAnswers();
        return Map.entry(randomId, randomQn);
    }

    // HELPER METHOD // Scrapes the qnBank from the site using json

    private void scrapeQuestions() {
        try {
            // Obtain the questions
            URL url = new URL("https://opentdb.com/api.php?amount=50&type=multiple");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // If the response code is not 200, then there is an error, so throw exception
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException();
            }

            // Set up the buffered reader and read the responsem storing the JSON response in 
            // String format inside a StringBuilder variable
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response string and store it into the HashMap
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray results = jsonResponse.getJSONArray("results");
            // Iterate over all entities in the JSON Array
            for (int i = 0; i < results.length(); i++) {
                JSONObject questionObj = results.getJSONObject(i);
                String question = questionObj.getString("question");
                String correctAnswer = questionObj.getString("correct_answer");
                JSONArray incorrectAnswers = questionObj.getJSONArray("incorrect_answers");

                List<String> answers = new ArrayList<>();
                // Add the correct and incorrect answers to the list
                answers.add(correctAnswer);
                for (int j = 0; j < incorrectAnswers.length(); j++) {
                    answers.add(incorrectAnswers.getString(j));
                }

                // Put the question and answers in the HashMap
                qnBank.put(i, new Question(question, correctAnswer, answers));
            }

        } catch (Exception e) {
            // I just listed exception because there is a bunch of exceptions possible
            e.printStackTrace();
        }
    }

    ///// GETTER METHODS /////
    
    public Map<Integer, Question> getQnBank() { return qnBank; }
    public Random getRandomGenerator() { return randomGenerator; }
    
}
