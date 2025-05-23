/*
@Mu Ye Liu - Feb 2025

Represents the global variables and functions for the javascript frontend
*/

///// VARIABLES /////

window.apiPrefix = "http://localhost:8080/api/brainjack";
window.tabApiPrefix = "http://localhost:8080/api/tabManagement";
window.htmlPrefix = "http://localhost:8080/html";
window.players = new Map();

///// FUNCTIONS /////

/*
General post request function
REQUIRES:
url: must be the actual url of the request
data: must be in requestBody format (json format)
contentType: must be a valid content type
*/
window.postRequest = async function(url, data, contentType) {
    try {
        // Calls the API. Tries to get the response
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": contentType
            }, 
            body: data
        });

        // Parse response into JSON format
        const jsonResponse =  await response.json();

        // If the response is not 200 OK, then throw the returned response
        if (!response.ok) {
            throw jsonResponse;
        }

        // If the response is 200 OK, then simply return it without throwing error
        return jsonResponse;

    } catch (error) {
        // Throw the error again and let the caller handle it
        throw error;
    }
}

/* General put request function 
REQUIRES: - url: Must be a valid url in string form
          - data: Must be a valid body parameter
          - contentType: Must be wither "text/plain" or "application/json"
*/
window.putRequest = async function(url, data, contentType) {
    try {
        // Calls the API. Tries to get the response
        const response = await fetch(url, {
            method: "PUT",
            headers: {
                "Content-Type": contentType
            }, 
            body: data
        });

        // Parse response into JSON format
        const jsonResponse =  await response.json();

        // If the response is not 200 OK, then throw the returned response
        if (!response.ok) {
            throw jsonResponse;
        }

        // If the response is 200 OK, then simply return it without throwing error
        return jsonResponse;

    } catch (error) {
        // Throw the error again and let the caller handle it
        throw error;
    }
}

/*
General get request function with no parameters
REQUIRES: 
url: must be a valid url to an api call.
*/
window.getRequestNoParams = async function(url) {
    try {
        // Make the GET request
        const response = await fetch(url, {
            method: "GET"
        });

        const jsonResponse = await response.json();
    
        // Check if the response was successful
        if (!response.ok) {
            throw jsonResponse;
        }
    
        // Parse and return the JSON data from the response
        return jsonResponse;
      } catch (error) {
        // Throw error again and let the caller handle it
        throw error;
      }
}

// Get all the logged in players
window.getLoggedInPlayers = async function() {
    // Gets the full api link 
    const url = window.apiPrefix + "/players/getActivePlayers";
    try {
        // Attemps to get the list of players
        const players = await window.getRequestNoParams(url);
        // Puts it into the window.players map
        window.players = new Map(Object.entries(players));
    } catch (error) {
        // Shouldn't happen
        console.error("Error that shouldn't occur occurred");
    }
}

// Gets the number of game tabs open (global function)
// RETURNS: integer
window.getNumberOfGameTabsOpen = async function() {
    const url = window.tabApiPrefix + "/numberOfGameTabsOpen";
    const response = await window.getRequestNoParams(url);

    return response;
}