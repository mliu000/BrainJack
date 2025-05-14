/*
@Mu Ye Liu - May 2025

Represents the code for loading the ui page
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

// Overrides the content page to display error message when more than one tab open
// REQUIRES: - tabs: must be an integer
function gameInPlayOverride(tabs) {
    const mainPage = document.getElementById("main-page");
    const overridePage = document.getElementById("override-page");
    const overrideMessage = document.getElementById("override-message");
    overrideMessage.innerHTML = `GAME IS IN PROGRESS IN ANOTHER TAB. ALSO, MAKE SURE THERE IS ONLY ONE GAME TAB OPEN, AND WAIT A FEW SECONDS BEFORE RELOADING. THERE ARE CURRENTLY <u>${tabs}</u> TABS OPEN`;
    mainPage.style.display = "none";
    overridePage.style.display = "block";
}

// Overrides the content page to display error message when there are no players
function notEnoughPlayersOverride() {
    const mainPage = document.getElementById("main-page");
    const noPlayersPage = document.getElementById("no-players-page");
    mainPage.style.display = "none";
    noPlayersPage.style.display = "block";
}

// Initializes the game layout screen based on the number of players
// REQUIRES: the function can only be called when there is at least one player logged in.
function initializeGameScreen() {
    switch (window.players.size) {
        case 1: 
            break;
        case 2: 
            break;
        case 3: 
            break;
        case 4:
            break;
    }
}

/* Sets the status of whether or not the game is currently in actions, which determines whether or
not the popup to confirm exit will show up. Uses session storage.
REQUIRES: - status: must be a boolean
*/
function setInGamePlayStatus(status) {
    sessionStorage.setItem("gameInPlay", status);
}

///// HELPER FUNCTIONS /////



///// ACTION LISTENERS /////

// Set on load
window.addEventListener("DOMContentLoaded", async() => {
    // Wait for the mutex first. 
    await waitForMutex();

    // Get all the players first
    await window.getLoggedInPlayers();


    // Use try/finally because error with tab opening could occur
    try {
        // Register the tab to increment its counter. 
        // Also, set the game in action status to false
        const numberOfGameTabsOpen = await registerTab(tabId);
        console.log(numberOfGameTabsOpen);
        setInGamePlayStatus(false);

        // Make the main page visible
        document.getElementById("main-page").style.display = "block";

        // If count != 1, then do not allow another game to be played
        if (numberOfGameTabsOpen > 1) {
            gameInPlayOverride(numberOfGameTabsOpen); 
        } else if (window.players.size === 0) {
            // No players logged in 
            notEnoughPlayersOverride();
        } else {
            // Initialize game and set the game in action status to true
            setInGamePlayStatus(true);
            initializeGameScreen();
            addActionListeners();
        }
    } finally {
        // Release the mutex after initialization and start heartbeat of tab
        heartbeat(tabId);
        releaseMutex();
    }

});

// Set when attempting to unload
window.addEventListener("beforeunload", () => {
    // PUT STUFF HERE.
});

// We need this due to the confirmation tab
window.addEventListener("unload", () => {
    // Clear the heartbeat and use sendBeacon to deregister the tab
    navigator.sendBeacon(`${window.tabApiPrefix}/${tabId}/deregister`, "");
});