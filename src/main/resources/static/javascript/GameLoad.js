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
    // Create the player profiles
    let ithPlayer = 1;
    for (const [key, value] of window.players) {
        createPlayerProfile(key, value, ithPlayer, window.players.size);
        ithPlayer++;
    }

    // Adjust the bars based on number of players there are
    adjustPlayerBars();

    // 
}

///// HELPER FUNCTIONS /////

/* Creates a new player html profile (involves a lot of html/css)
REQUIRES: - key and value: must be a corresponding key value pair
          - ithPlayer: must be an integer from 1 to 4
          - noPlayers: must be an integer from 1 to 4
*/
function createPlayerProfile(key, value, ithPlayer, noPlayers) {
    // Get the player div first
    const playerRegion = document.getElementById("player-region");

    // Create the profile background
    const playerProfile = document.createElement("div");
    playerProfile.id = `player-profile-${ithPlayer}`;
    Object.assign(playerProfile.style, {
        position: "absolute",
        width: "25%",
        height: "100%",
        left: `${(100*ithPlayer - 50) / noPlayers}vw`, 
        top: "0%",
        transform: "translate(-50%)",
    });

    // Create the username label
    const usernameLabel = createLabel(key, "h3", `username-label-${ithPlayer}`, "lightgray", "clamp(0vw, 2vw, 3vh)");
    Object.assign(usernameLabel.style, {
        position: "absolute", 
        left: "50%",
        top: "2%",
        transform: "translate(-50%)",
        textDecoration: "underline"
    });

    // Create the score div
    const playerScore = createPlayerScore(ithPlayer);

    // Append all the elements to the player profile
    playerProfile.appendChild(usernameLabel);
    playerProfile.appendChild(playerScore);
    
    // Append the player profile to the main page
    playerRegion.appendChild(playerProfile);
}

/* Adjust the bars based on the number of players there are
*/
function adjustPlayerBars() {
    switch (window.players.size) {
        case 2: 
            Object.assign(document.getElementById("player-line-1").style, {
                display: "block",
                left: "50vw"
            });
            break;
        case 3: 
            Object.assign(document.getElementById("player-line-1").style, {
                display: "block",
                left: "33vw"
            });
            Object.assign(document.getElementById("player-line-2").style, {
                display: "block",
                left: "67vw"
            });
            break;
        case 4:
            Object.assign(document.getElementById("player-line-1").style, {
                display: "block",
                left: "25vw"
            });
            Object.assign(document.getElementById("player-line-2").style, {
                display: "block",
                left: "50vw"
            });
            Object.assign(document.getElementById("player-line-3").style, {
                display: "block",
                left: "75vw"
            });
            break;
    }
}

/* Create Label in general, then returns it
REQUIRES: - text: must be a valid string
          - style: must be a valid style such as "h1, h2, p, etc" in string form
          - textColor: must be a valid color
          - textFontsize: must be a valid integer
*/
function createLabel(text, labelID, style, textColor, textFontSize) {
    const newLabel = document.createElement(style);
    newLabel.id = labelID, 
    newLabel.innerHTML = text;
    Object.assign(newLabel.style, {
        color: textColor,
        fontSize: textFontSize
    });
    return newLabel;
}

/* Create the div for the player score
REQUIRES: - ithPlayer: must be an int from 1 to 4
*/
function createPlayerScore(ithPlayer) {
    const newPlayerScoreContainer = document.createElement("div");
    const newPlayerScoreLabel = createLabel("Score", `player-score-label-${ithPlayer}`);
    const newPlayerScoreCircle = document.createElement("div");
    const newPlayerScore = createLabel("0", `player-score-${ithPlayer}`);

    newPlayerScoreContainer.id = `player-score-container-${ithPlayer}`;
    newPlayerScoreCircle.id = `player-score-circle-${ithPlayer}`;
    newPlayerScoreCircle.className = "circle";
    
    Object.assign(newPlayerScoreContainer.style, {
        position: "absolute",
        display: "flex",
        flexDirection: "column", 
        alignItems: "center",
        left: "80%",
        top: "10%",
        transform: "translate(-50%)"
    });
    Object.assign(newPlayerScoreLabel.style, {
        fontSize: "clamp(0vw, 1.2vw, 1.8vh)",
        marginTop: "1vh",
        marginBottom: "0",
        color: "lightgray"
    })
    Object.assign(newPlayerScoreCircle.style, {
        width: "clamp(0vw, 4vw, 6vh)",
        height: "clamp(0vw, 4vw, 6vh)",
        position: "relative"
    })
    Object.assign(newPlayerScore.style, {
        position: "absolute",
        left: "50%",
        top: "50%",
        transform: "translate(-50%, -50%)",
        fontSize: "clamp(0vw, 2vw, 3vh)",
        color: "lightgray"
    })

    newPlayerScoreCircle.appendChild(newPlayerScore);
    newPlayerScoreContainer.appendChild(newPlayerScoreCircle);
    newPlayerScoreContainer.appendChild(newPlayerScoreLabel);
    return newPlayerScoreContainer;
}


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