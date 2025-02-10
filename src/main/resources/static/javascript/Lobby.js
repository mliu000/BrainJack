///// VARIABLES /////

/// The main screen variables
const loginButton = document.getElementById("login-button");
const createPlayerButton = document.getElementById("create-player-button");

// For the stats popup
const statsPopup = document.getElementById("statistics-popup");
const statsButton = document.getElementById("stats-button");

// For the Create Player popup
const createPlayerPopup = document.getElementById("create-player-popup");
const authenticateCreatePlayerButton = document.getElementById("authenticate-create-player-button");

// For the Login popup
const loginPopup = document.getElementById("login-popup");

///// FUNCTIONS /////

/*
Toggle the stats button visibility based on whether or not list is empty
REQUIRES: the param: setVisible must be a boolean
*/
function toggleStatsButtonVisibility(setVisible) {
    // Get the stats button element
    if (setVisible) {
        statsButton.style.display = "inline-block";
    } else {
        statsButton.style.display = "none";
    }
}

// Make the login popup visible upon corresponding button click
function handleLoginButtonClick() {
    loginPopup.style.display = "block";
}

// Make the create player popup visible upon corresponding button click
function handleCreatePlayerButtonClick() {
    createPlayerPopup.style.display = "block";
}

/// Create Player Popup ///

/*
Send API POST request with given inputs from createUsernameTextField and createPasswordTextField.
- Successfully create new player if username and password successful, while also displaying a success
message for 1 sec, then closes the popup tab,
- Fails to create new player, and displays corresponding error message. Keeps the tab open.
*/
async function handleAuthenticateCreatePlayerButtonClick() {
    // Gets the text field input values
    const inputUsername = document.getElementById("create-username-text-field").value;
    const inputPassword = document.getElementById("create-password-text-field").value;
    
    // Puts the input values into body parameter json format
    const bodyParameter = { 
        "username": inputUsername,
        "password": inputPassword
    }

    // Get the suffix to url address
    const url = window.apiPrefix + "/players/createPlayer";

    // Gets the response from createPlayer api call
    try {
        const response = await window.postRequest(url, bodyParameter);
        // If returned response is the error response, throw an error
        console.log("API Response: ", response);
    } catch (error) {
        console.error("API Error Code:" , error.error_code, "API Error Message:", error.message);
    }
}

// Post Request to create new player
function createPlayerRequest() {

}


///// API REQUEST FUNCTIONS ///// 

// Post request
async function postRequest() {

}


///// INITIALIZATION PROCEDURAL CODE /////

// Add action listeners to the text inputs to restrict to alphanumeric inputs
document.querySelectorAll(".input-field").forEach(field => {
    field.addEventListener("input", () => {
        field.value = field.value.replace(/[^a-zA-Z0-9]/g, "");
    });
});

// Add the button event listeners
document.addEventListener("DOMContentLoaded", () => {
    // Add the button action listeners
    loginButton.addEventListener("click", handleLoginButtonClick);
    createPlayerButton.addEventListener("click", handleCreatePlayerButtonClick);
    authenticateCreatePlayerButton.addEventListener("click", handleAuthenticateCreatePlayerButtonClick);
});

// Hide if click is outside the popup (and not the show button)
document.addEventListener("click", (e) => {
    if (e.target.classList.contains("popup-background")) {
        e.target.style.display = "none";
    }
});

