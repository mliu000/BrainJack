/*
@Mu Ye Liu - May 2025

Stores all the functions that represents the api calls that the game needs to make
*/

/* Sets the player bet
REQUIRES: - username: Must be a valid player username that is already logged in
          - bet: Must be a valid positive integer from 1 to 1 million
RETURNS: the same player along with their bet set.
*/
async function setPlayerBet(username, bet) {
    try {
        const url = `${window.apiPrefix}/players/${username}/setPlayerBet`;
        const response = window.putRequest(url, bet, "application/json");

        return response;
    } catch (error) {
        console.error("API Error Code:" , error.error_code, "API Error Message:", error.message);
        throw error;
    }
}

/* Player draws a card
REQUIRES: - username: Must be a valid player username that is already logged in
RETURNS: the same player along with their additional hit card
*/
async function playerHit(username) {
    try {
        const url = `${window.apiPrefix}/players/${username}/playerHit`;
        const response = window.putRequest(url, null, "text/plain");

        return response;
    } catch (error) {
        console.error("API Error Code:" , error.error_code, "API Error Message:", error.message);
        throw error;
    }
}

/* Updates the player statistics (based on win or lose / correct or incorrect answer) after match
REQUIRES: - username: Must be a valid player username that is already logged in
          - bet: Must be a valid positive integer from 1 to 1 million
RETURNS: the same player with their stats updated 
*/
async function updatePlayerStatistics(username, win, correct) {
    try {
        const body = {
            "win": win, 
            "correct": correct
        }
        const url = `${window.apiPrefix}/players/${username}/updatePlayerStatistics`;
        const response = window.putRequest(url, JSON.stringify(body), "application/json");

        return response;
    } catch (error) {
        console.error("API Error Code:" , error.error_code, "API Error Message:", error.message);
        throw error;
    }
}

/* Draws 2 cards, which is used for both player or dealer
REQUIRES: - username: Must be a valid player username that is already logged in, or "d" for dealer
RETURNS: the same player/dealer along with their decks reset
*/
async function participantStartDraw(username) {
    try {
        const url = `${window.apiPrefix}/participant/${username}/participantStartDraw`;
        const response = window.putRequest(url, null, "text/plain");

        return response;
    } catch (error) {
        console.error("API Error Code:" , error.error_code, "API Error Message:", error.message);
        throw error;
    }
}

/* Resets the particpant, which is used for both player or dealer
REQUIRES: - username: Must be a valid player username that is already logged in, or "d" for dealer
RETURNS: the same player/dealer along with their decks reset
*/
async function participantReset(username) {
    try {
        const url = `${window.apiPrefix}/participant/${username}/participantReset`;
        const response = window.putRequest(url, null, "text/plain");

        return response;
    } catch (error) {
        console.error("API Error Code:" , error.error_code, "API Error Message:", error.message);
        throw error;
    }
}

/* Dealer plays hand all at once.
RETURNS: the dealer with the cards drawn
*/
async function dealerPlayHand() {
    try {
        const url = `${window.apiPrefix}/dealerPlayHand`;
        const response = window.putRequest(url, null, "text/plain");

        return response;
    } catch (error) {
        console.error("API Error Code:" , error.error_code, "API Error Message:", error.message);
        throw error;
    }
}

/* Gets a random question
RETURNS: A multiple choice question with 3-4 choices
*/
async function getRandomQuestion() {
    try {
        const url = `${window.apiPrefix}/getRandomQuestion`;
        const response = window.getRequestNoParams(url);

        return response;
    } catch (error) {
        console.error("API Error Code:" , error.error_code, "API Error Message:", error.message);
        throw error;
    }
}