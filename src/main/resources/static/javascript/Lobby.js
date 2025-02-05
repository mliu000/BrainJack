///// VARIABLES /////
const statsButton = document.getElementById("stats-button");
const loginPopup = document.getElementById("login-popup");
const loginButton = document.getElementById("login-button");
///// FUNCTIONS /////

/*
Toggle the stats button visibility based on whether or not list is empty
REQUIRES: the param: setVisible must be a boolean
*/
function toggleStatsButtonVisibility(setVisible) {
    // Get the stats button element
    if (setVisible) {
        statsButton.style.visibility = "visible";
    } else {
        statsButton.style.visibility = "hidden";
    }
}

// Make the login popup visible upon corresponding button click
function handleLoginButtonClick() {
    loginPopup.style.visibility = "visible";
}

///// PROCEDURAL CODE ////

// Add the button event listeners
document.addEventListener("DOMContentLoaded", () => {
    // Add the button action listeners
    loginButton.addEventListener("click", handleLoginButtonClick);

    // Add listeners to close prompts when you click anywhere outside the element

});

// Hide if click is outside the popup (and not the show button)
document.addEventListener("click", (e) => {
    if (loginPopup.style.visibility === "visible" && !loginPopup.contains(e.target) && e.target !== loginButton) {
        loginPopup.style.visibility = "hidden";
    }
});

