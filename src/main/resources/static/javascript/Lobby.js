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

function handleCreatePlayerButtonClick() {
    createPlayerPopup.style.display = "block";
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
});

// Hide if click is outside the popup (and not the show button)
document.addEventListener("click", (e) => {
    if (e.target.classList.contains("popup-background")) {
        e.target.style.display = "none";
    }
});

