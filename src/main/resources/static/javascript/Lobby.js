///// VARIABLES /////

/// The main screen variables
const loginButton = document.getElementById("login-button");
const createPlayerButton = document.getElementById("create-player-button");

// For the stats popup
const statsPopup = document.getElementById("statistics-popup");
const statsButton = document.getElementById("stats-button");

// For the Create Player popup
const createPlayerPopup = document.getElementById("create-player-popup");
const createUsernameTextField = document.getElementById("create-username-text-field");
const createPasswordTextField = document.getElementById("create-password-text-field");
const authenticateCreatePlayerButton = document.getElementById("authenticate-create-player-button");
const createPlayerMessage = document.getElementById("create-player-message");

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
    
    // Puts the input values into body parameter json format
    const bodyParameter = { 
        "username": createUsernameTextField.value,
        "password": createPasswordTextField.value
    }

    // Get the suffix to url address
    const url = window.apiPrefix + "/players/createPlayer";

    // Gets the response from createPlayer api call
    try {
        const response = await window.postRequest(url, bodyParameter);
        // If returned response is the error response, throw an error
        console.log("API Response: ", response);
    } catch (error) {
        // Print out the error
        console.error("API Error Code:" , error.error_code, "API Error Message:", error.message);
        // Reset the text fields to be empty again
        resetTextFields([createUsernameTextField, createPasswordTextField]);
        // Display the error message depending on the the specific error
        switch (error.error_code) {
            case 4004:
                setDisplayMessage(createPlayerMessage, "rgb(241, 23, 12)", "Username already taken. Please try again")
                break;
            case 4005:
                setDisplayMessage(createPlayerMessage, "rgb(241, 23, 12)", "Username or password must be at least 4 characters long. Please try again")
                break;
        }
    }

}

///// HELPER FUNCTIONS ///// 

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

