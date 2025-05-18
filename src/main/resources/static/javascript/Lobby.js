/*
@Mu Ye Liu - Feb 2025

Represents the code for the lobby class
*/

///// VARIABLES /////

// Constants
const successColor = "rgb(13, 207, 26)";
const failureColor = "rgb(241, 23, 12)";

// Factors
let clickOutOfPopupDisabled = false;

// The main screen variables
let loginButton;
let logoutButton;
let createPlayerButton;
let startGameButton;
let statsButton;
let loggedInPlayerListDisplay;

// For the stats popup
let statsPopup;

// For the Create Player popup
let createPlayerPopup;
let createUsernameTextField;
let createPasswordTextField;
let authenticateCreatePlayerButton;
let createPlayerMessage;

// For the Login popup
let loginPopup;
let loginUsernameTextField;
let loginPasswordTextField;
let authenticateLoginPlayerButton;
let loginMessage;

// For the logout popup
let logoutPopup;

// For the statistics popup
let statisticsPopup;

///// FUNCTIONS /////

// Make the login popup visible upon corresponding button click
function handleLoginButtonClick() {
    loginPopup.style.display = "block";
}

// Make the create player popup visible upon corresponding button click
function handleCreatePlayerButtonClick() {
    createPlayerPopup.style.display = "block";
}

// Makes the logout prompt visible upon button click
function handleLogoutButtonClick() {
    logoutPopup.style.display = "block";
}

// Makes the logout prompt visible upon button click
function handleStaticticsButtonClick() {
    statisticsPopup.style.display = "block";
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
        const response = await window.postRequest(url, JSON.stringify(bodyParameter), "application/json");
        // Set the display message, set a timeout, then close the popup and reset it
        // Also, add player to list and display it.
        setDisplayMessage(createPlayerMessage, successColor, "Success. Account Created");
        clickOutOfPopupDisabled = true;
        authenticateCreatePlayerButton.disabled = true;
        setTimeout(() => {
            createPlayerPopup.style.display = "none";
            resetPopupWithTextAfterExiting(createPlayerPopup, createPlayerMessage);
            clickOutOfPopupDisabled = false;
            authenticateCreatePlayerButton.disabled = false;
        }, 1500);
        addPlayerToList(response);
        addPlayerToListDisplay(response.username);
        addButtonToLogoutAndStatisticsPopups(response.username);
        setButtonsBasedOnSizeOfLobby();
    } catch (error) {
        // Print out the error
        console.error("API Error Code:" , error.error_code, "API Error Message:", error.message);
        // Display the error message depending on the the specific error
        switch (error.error_code) {
            case 4004:
                // Case error 1: Username already taken
                setDisplayMessage(createPlayerMessage, 
                    failureColor, 
                    "Username already taken. Please try again")
                break;
            case 4005:
                // Case error 2: Username or password is too short
                setDisplayMessage(createPlayerMessage, 
                    failureColor, 
                    "Username and password must be at least 4 characters long. Please try again")
                break;
        }
    }

}

/*
Send API POST request with given inputs from loginUsernameTextField and loginPasswordTextField
- Successfully login player if api calls returns correctly, display success message then exists popup
- Fails to login player if api calls throws error, 
*/
async function handleAuthenticateLoginPlayerButtonClick() {
    // Gets the text field input values
    
    // Puts the input values into body parameter json format
    const bodyParameter = { 
        "username": loginUsernameTextField.value,
        "password": loginPasswordTextField.value
    }

    // Get the suffix to url address
    const url = window.apiPrefix + "/players/playerLogin";

    // Reset the text fields to be empty again
    resetTextFields([loginUsernameTextField, loginPasswordTextField]);

    // Gets the response from createPlayer api call
    try {
        const response = await window.postRequest(url, JSON.stringify(bodyParameter), "application/json");
        // Set the display message, set a timeout, then close the popup and reset it
        // Also, add player to list and display it.
        setDisplayMessage(loginMessage, successColor, "Successfully Logged in");
        clickOutOfPopupDisabled = true;
        authenticateLoginPlayerButton.disabled = true;
        setTimeout(() => {
            loginPopup.style.display = "none";
            resetPopupWithTextAfterExiting(loginPopup, loginMessage);
            clickOutOfPopupDisabled = false;
            authenticateLoginPlayerButton.disabled = false;
        }, 1500);
        addPlayerToList(response);
        addPlayerToListDisplay(response.username);
        addButtonToLogoutAndStatisticsPopups(response.username);
        setButtonsBasedOnSizeOfLobby();
    } catch (error) {
        // Print out the error
        console.error("API Error Code:" , error.error_code, "API Error Message:", error.message);
        // Display the error message depending on the the specific error
        switch (error.error_code) {
            case 4001:
                // Error Case 1: Player already logged in
                setDisplayMessage(loginMessage, 
                    failureColor, 
                    "Player already logged in. Please try again");
                break;
            case 4003:
                // Error Case 2 (shouldn't happen): Lobby alredy full
                setDisplayMessage(loginMessage, failureColor, "Lobby already full");
                break;
            case 4041:
                // Error Case 3: Player not found, login failure
                setDisplayMessage(loginMessage, failureColor, 
                    "Player with username and password not found. Please try again");
                break;
        }
    }
}

/*
Handle Logout of player given username
REQUIRES: 
- username: must be a valid username in string format
*/
async function handleLogoutOfPlayer(username) {
    // Gets the url
    const url = window.apiPrefix + "/players/playerLogout";
    try {
        // Trys to logout
        const response = await window.postRequest(url, username, "text/plain");
        // Modify the UI if logout is successful
        console.log("Logout status" + response);
        window.players.delete(username);
        removePlayerFromList(username);
        removeButtonFromLogoutAndStatisticsPopups(username);
        setButtonsBasedOnSizeOfLobby();
        // Make all buttons disappear for 1.5 sec and set the logout feedback message
        const logoutButtonList = document.getElementById("logout-button-list");
        logoutButtonList.style.display = "none";
        const logoutFeedback = document.getElementById("logout-feedback");
        clickOutOfPopupDisabled = true;
        setDisplayMessage(logoutFeedback, 
            successColor, 
            `Successfully logged out: ${username}`);
        logoutFeedback.style.display = "block";
        setTimeout(() => {
            logoutPopup.style.display = "none";
            logoutFeedback.style.display = "none";
            logoutButtonList.style.display = "flex";
            clickOutOfPopupDisabled = false;
        }, 1500);
    

    } catch (error) {
        // Logout failed (shouldn't happen)
        
        console.error("Logout failed. Usernane is not found in logged in player list. Code: " + error.error_code)
    }
}

///// HELPER FUNCTIONS ///// 

// MODIFY LOGOUT AND STATISTICS BUTTON HELPERS

/*
Add logout and statistcs button to their corresponding popups
REQUIRES: 
- username: must be a valid username in string format
*/ 
function addButtonToLogoutAndStatisticsPopups(username) {
    // Get the corresponding lists
    const logoutButtonList = document.getElementById("logout-button-list");
    const statisticsButtonList = document.getElementById("statistics-button-list");
    // Create a new button for each popup, and give it an id and class
    const newLogoutPlayerButton = document.createElement("button");
    const newViewPlayerStatisticsButton = document.createElement("button");
    newLogoutPlayerButton.id = "logout-" + username;
    newLogoutPlayerButton.classList.add("container-button");
    newLogoutPlayerButton.textContent = username;
    newViewPlayerStatisticsButton.id = "statistics-" + username;
    newViewPlayerStatisticsButton.classList.add("container-button");
    newViewPlayerStatisticsButton.textContent = username;
    // Add them to the corresponding button lists
    logoutButtonList.appendChild(newLogoutPlayerButton);
    statisticsButtonList.appendChild(newViewPlayerStatisticsButton);
    // Add action listeners to them
    addActionListenerToLogoutButtons(newLogoutPlayerButton, username);
    addActionListenerToStatisticsButton(newViewPlayerStatisticsButton);
}


/*
Adds the action listener to the logout popup buttons
REQUIRES: 
- playerLogoutButton: Must be a valid logout button in the popup
- username: Must be a valid username
*/
function addActionListenerToLogoutButtons(playerLogoutButton, username) {
    playerLogoutButton.addEventListener("click", () => {
        handleLogoutOfPlayer(username);
    });
}

/*
Adds the action listener to the statistics button in the popup, where you navigate to statistics page
REQUIRES: 
- statisticsButton: Must be a valid statistics button in the popup
*/
function addActionListenerToStatisticsButton(playerStatisticsButton) {
    playerStatisticsButton.addEventListener("click", () => {
        // Get the url, and encode the username into it
        const url = window.htmlPrefix + "/Statistics.html?username=" 
            + encodeURIComponent(playerStatisticsButton.textContent);
        // Then, go to url
        window.location.href = url;
        
    });
}

/*
Removes logout and statistcs button from their corresponding popups
REQUIRES: 
- username: must be a valid username in string format
*/ 
function removeButtonFromLogoutAndStatisticsPopups(username) {
    const logoutButtonToRemove = document.getElementById("logout-" + username);
    const statisticsButtonToRemove = document.getElementById("statistics-" + username);
    logoutButtonToRemove.remove();
    statisticsButtonToRemove.remove();
}

// CREATE PLAYER AND LOGIN HELPERS

/*
Adds the player to list. Also, adjusts the visible based on the size of the lobby.
REQUIRES: 
- player: must be a json response with player with necessary fields
*/
function addPlayerToList(player) {
    window.players.set(player.username, player);
}

/*
Adds player to logged in list
REQUIRES: 
- playerUsername: must be an actual player in json format
*/
function addPlayerToListDisplay(playerUsername) {
    const listItem = document.createElement("li");
    listItem.id = "list-display-" + playerUsername;
    listItem.textContent = playerUsername;
    loggedInPlayerListDisplay.appendChild(listItem);
}

// LOGOUT HELPERS

/*
Removes the player from the list of active players display (only if it exists)
REQUIRES: 
- username: must be a valid username in string format
*/
function removePlayerFromList(username) {
    // Removes player username from display ilst
    const listElementToRemove = document.getElementById("list-display-" + username);
    if (listElementToRemove) {
        listElementToRemove.remove();
    }
}


// RESET POPUP HELPERS

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

// MISCELLANEOUS HELPERS

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

// Overrides the content page to display error message when game is in play
function gameInPlayOverride() {
    const mainPage = document.getElementById("main-page");
    const overridePage = document.getElementById("override-page");
    mainPage.style.display = "none";
    overridePage.style.display = "block";
}

///// INITIALIZATION PROCEDURAL CODE /////

// Initializes the lobby based on the number of players logged in.
document.addEventListener("DOMContentLoaded", async () => {
    // Assign DOM elements after DOM is loaded
    loginButton = document.getElementById("login-button");
    logoutButton = document.getElementById("logout-button");
    createPlayerButton = document.getElementById("create-player-button");
    startGameButton = document.getElementById("start-game-button");
    statsButton = document.getElementById("stats-button");
    loggedInPlayerListDisplay = document.getElementById("logged-in-player-list");

    statsPopup = document.getElementById("statistics-popup");

    createPlayerPopup = document.getElementById("create-player-popup");
    createUsernameTextField = document.getElementById("create-username-text-field");
    createPasswordTextField = document.getElementById("create-password-text-field");
    authenticateCreatePlayerButton = document.getElementById("authenticate-create-player-button");
    createPlayerMessage = document.getElementById("create-player-message");

    loginPopup = document.getElementById("login-popup");
    loginUsernameTextField = document.getElementById("login-username-text-field");
    loginPasswordTextField = document.getElementById("login-password-text-field");
    authenticateLoginPlayerButton = document.getElementById("authenticate-login-button");
    loginMessage = document.getElementById("login-message");

    logoutPopup = document.getElementById("logout-popup");
    statisticsPopup = document.getElementById("statistics-popup");

    logoutButtonList = document.getElementById("logout-button-list");
    logoutFeedback = document.getElementById("logout-feedback");
    mainPage = document.getElementById("main-page");
    overridePage = document.getElementById("override-page");

    // Check or initialize the number of game tabs open
    const numberOfGameTabs = await window.getNumberOfGameTabsOpen();

    if (numberOfGameTabs > 0) {
        gameInPlayOverride();
        return;
    }

    // Make the main page visible
    mainPage.style.display = "block";

    // Get logged in players, initialize list and buttons
    window.getLoggedInPlayers().then(() => {
        setButtonsBasedOnSizeOfLobby();
        for (const username of window.players.keys()) {
            addButtonToLogoutAndStatisticsPopups(username);
            addPlayerToListDisplay(username);
        }
    });

    // Add the button action listeners
    loginButton.addEventListener("click", handleLoginButtonClick);
    createPlayerButton.addEventListener("click", handleCreatePlayerButtonClick);
    logoutButton.addEventListener("click", handleLogoutButtonClick);
    statsButton.addEventListener("click", handleStaticticsButtonClick);

    authenticateCreatePlayerButton.addEventListener("click", handleAuthenticateCreatePlayerButtonClick);
    authenticateLoginPlayerButton.addEventListener("click", handleAuthenticateLoginPlayerButtonClick);
});

// Add action listeners to the text inputs to restrict to alphanumeric inputs
document.querySelectorAll(".input-field").forEach(field => {
    field.addEventListener("input", () => {
        field.value = field.value.replace(/[^a-zA-Z0-9]/g, "");
    });
});

// Hide if click is outside the popup (and not the show button)
document.addEventListener("click", (e) => {
    // Make popup invisible
    if (e.target.classList.contains("popup-background") && !clickOutOfPopupDisabled) {
        e.target.style.display = "none";
        // If the popup contains text input fields and feedback message, then reset those as well.
        if (e.target.querySelector(".feedback-message") && e.target.querySelector(".input-field")) {
            const feedbackMessage = e.target.querySelector(".feedback-message");
            resetPopupWithTextAfterExiting(e.target, feedbackMessage);
        }   
    } 
});

