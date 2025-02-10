/*
@Mu Ye Liu - Feb 2025

Represents the global variables and functions for the javascript frontend
*/

///// VARIABLES /////

window.apiPrefix = "http://localhost:8080/api/brainjack"
window.dealer = null;
window.players = [];

///// FUNCTIONS /////

// General post request function
window.postRequest = async function(url, data) {
    try {
        // Calls the API. Tries to get the response
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }, 
            body: JSON.stringify(data)
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

///// PROCEDURAL CODE /////