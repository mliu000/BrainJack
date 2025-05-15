/*
@Mu Ye Liu - May 2025

Represents the code for the game itself
Includes adding action listeners, case handling, and api calls
*/

///// ACTION LISTENERS /////

// Adds the basic action listeners for the buttons
function addActionListeners() {
    // Retrieve the buttons
    const startOkButton = document.getElementById("start-ok-button");

    // Add the action listeners
    startOkButton.addEventListener("click", () => {
        document.getElementById("start-popup").style.display = "none";
    });

}