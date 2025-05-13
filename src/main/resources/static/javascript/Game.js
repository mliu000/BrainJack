/*
@Mu Ye Liu - May 2025

Represents the UI for the game tab itself.
*/

///// unique identifiers for the tab
const tabId = window.crypto.randomUUID();

///// LOAD FUNCTIONS /////

// Adds the basic action listeners for the buttons
function addActionListeners() {
    // Retrieve the buttons
    const startOkButton = document.getElementById("start-ok-button");

    // Add the action listeners
    startOkButton.addEventListener("click", () => {
        document.getElementById("start-popup").style.display = "none";
    });

}

// Overrides the content page to display error message when game is in play
function gameInPlayOverride() {
    const mainPage = document.getElementById("main-page");
    const overridePage = document.getElementById("override-page");
    mainPage.style.display = "none";
    overridePage.style.display = "block";
}

// Initializes the startup screen
function initializeGameScreen() {
    // TODO
}

/* Sets the status of whether or not the game is currently in actions, which determines whether or
not the popup to confirm exit will show up. Uses session storage.
REQUIRES: - status: must be a boolean
*/
function setInGamePlayStatus(status) {
    sessionStorage.setItem("gameInPlay", status);
}


///// ACTION LISTENERS /////

// Set on load
window.addEventListener("DOMContentLoaded", async() => {
    // Wait for the mutex first. 
    await waitForMutex();

    try {
        // Register the tab to increment its counter. 
        // Also, set the game in action status to false
        const numberOfGameTabsOpen = await registerTab(tabId);
        console.log(numberOfGameTabsOpen);
        setInGamePlayStatus(false);

        // If count != 1, then do not allow another game to be played
        if (numberOfGameTabsOpen === 1) {
            // Initialize game and set the game in action status to true
            setInGamePlayStatus(true);
            initializeGameScreen();
            addActionListeners();
        } else {
            gameInPlayOverride();
        }
    } finally {
        // Release the mutex after initialization and start heartbeat of tab
        heartbeat(tabId);
        releaseMutex();
    }
});

// Set when attempting to unload
window.addEventListener("beforeunload", (event) => {
    if (sessionStorage.getItem("gameInPlay") === "true") {
        // show custom popup here. 
        event.preventDefault();
        event.returnValue = "";
    }
});
