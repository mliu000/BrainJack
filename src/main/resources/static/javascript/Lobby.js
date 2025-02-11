/*
@Mu Ye Liu - Feb 2025

Represents the code for the lobby class
*/

///// VARIABLES /////

/// The main screen variables
const loginButton = document.getElementById("login-button");
const logoutButton = document.getElementById("logout-button");
const createPlayerButton = document.getElementById("create-player-button");
const startGameButton = document.getElementById("start-game-button");
const statsButton = document.getElementById("stats-button");
const loggedInPlayerListDisplay = document.getElementById("logged-in-player-list");

// For the stats popup
const statsPopup = document.getElementById("statistics-popup");

// For the Create Player popup
const createPlayerPopup = document.getElementById("create-player-popup");
const createUsernameTextField = document.getElementById("create-username-text-field");
const createPasswordTextField = document.getElementById("create-password-text-field");
const authenticateCreatePlayerButton = document.getElementById("authenticate-create-player-button");
const createPlayerMessage = document.getElementById("create-player-message");

// For the Login popup
const loginPopup = document.getElementById("login-popup");

///// FUNCTIONS /////

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
    
    // Puts the input values into body parameter json format
    const bodyParameter = { 
        "username": createUsernameTextField.value,
        "password": createPasswordTextField.value
    }

    // Get the suffix to url address
    const url = window.apiPrefix + "/players/createPlayer";

    // Reset the text fields to be empty again
    resetTextFields([createUsernameTextField, createPasswordTextField]);

    // Gets the response from createPlayer api call
    try {
        const response = await window.postRequest(url, bodyParameter);
        // If returned response is the error response, throw an error
        console.log("API Response: ", response);
        // Set the display message, set a timeout, then close the popup and reset it
        // Also, add player to list and display it.
        setDisplayMessage(createPlayerMessage, "rgb(13, 207, 26)", "Success. Account Created");
        setTimeout(() => {
            createPlayerPopup.style.display = "none";
            resetPopupWithTextAfterExiting(createPlayerPopup, createPlayerMessage);
        }, 1500);
        addPlayerToList(response);
        addPlayerToListDisplay(response.username);
        setButtonsBasedOnSizeOfLobby();
    } catch (error) {
        // Print out the error
        console.error("API Error Code:" , error.error_code, "API Error Message:", error.message);
        // Display the error message depending on the the specific error
        switch (error.error_code) {
            case 4004:
                setDisplayMessage(createPlayerMessage, 
                    "rgb(241, 23, 12)", 
                    "Username already taken. Please try again")
                break;
            case 4005:
                setDisplayMessage(createPlayerMessage, 
                    "rgb(241, 23, 12)", 
                    "Username or password must be at least 4 characters long. Please try again")
                break;
        }
    }

}

///// HELPER FUNCTIONS ///// 

/*
Adds the player to list. Also, adjusts the visible based on the size of the lobby.
REQUIRES: 
- player: must be a json response with player with necessary fields
*/
function addPlayerToList(player) {
    window.players.set(player.username, player);
    console.log(window.players);
}

/*
Adds player to logged in list
REQUIRES: 
- playerUsername: must be an actual player in json format
*/
function addPlayerToListDisplay(playerUsername) {
    const listItem = document.createElement("li");
    listItem.textContent = playerUsername;
    loggedInPlayerListDisplay.appendChild(listItem);
}

/*
Sets the content of a display message
REQUIRES: 
- label: must be a label, like an h1, h2 text in html, etc
- color: must be a string that represents a color, or rgb, hsla format, etc
- message: must be a string
*/
function setDisplayMessage(label, color, message) {
    label.style.color = color;
    label.textContent = message;
}

/*
Reset text fields to be blank
REQUIRES: 
- listOfTextFields: must be a list of text fields
*/
function resetTextFields(listOfTextFields) {
    listOfTextFields.forEach((textField) => {
        textField.value = "";
    });
}

/*
Reset popup menu after exiting by reset the input field and making the error message disappear again
REQUIRES:
- popupWithText: must be a popup with text 
*/
function resetPopupWithTextAfterExiting(popupWithTextField, message) {
    const textFields = Array.from(popupWithTextField.getElementsByClassName("input-field"));
    resetTextFields(textFields);
    setDisplayMessage(message, "transparent", "");
}

/*
Sets the button visibility based on parameters
REQUIRES: 
- a, b, c, d: must be "none" or "inline-block"
*/
function setButtonVisibility(a, b, c, d, e) {
    loginButton.style.display = a;
    logoutButton.style.display = b;
    createPlayerButton.style.display = c;
    startGameButton.style.display = d;
    statsButton.style.display = e;
}

// Sets which buttons are available depending on the size of the lobby
function setButtonsBasedOnSizeOfLobby() {
    switch (window.players.size) {
        case 0:
            // Empty lobby
            setButtonVisibility("inline-block", "none", "inline-block", "none", "none");
            break;
        case 4: 
            // Full lobby
            setButtonVisibility("none", "inline-block", "none", "inline-block", "inline-block");
            break;
        default:
            // Not full or empty
            setButtonVisibility("inline-block", "inline-block", "inline-block", "inline-block", "inline-block");
            break;
    }
}

///// INITIALIZATION PROCEDURAL CODE /////

// Initializes the lobby based on the number of players logged in.
document.addEventListener("DOMContentLoaded", () => {
    // gets the logged in players from the backend, then initializes the buttons and logged in list
    // Based on log in size
    window.getLoggedInPlayers().then(() => {
        setButtonsBasedOnSizeOfLobby();
        for (const username of window.players.keys()) {
            addPlayerToListDisplay(username);
        }

    });
});

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
    // Make popup invisible
    if (e.target.classList.contains("popup-background")) {
        e.target.style.display = "none";
        // If the popup contains text input fields and feedback message, then reset those as well.
        if (e.target.querySelector(".feedback-message") && e.target.querySelector(".input-field")) {
            const feedbackMessage = e.target.querySelector(".feedback-message");
            resetPopupWithTextAfterExiting(e.target, feedbackMessage);
        }   
    } 
});

