/*
@Mu Ye Liu - May 2025

Represents the code for loading the ui page and all its elements
*/

///// unique identifiers for the tab
const tabId = window.crypto.randomUUID();

///// LOAD FUNCTIONS /////

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
        // Create the profile
        createPlayerProfile(key, value, ithPlayer, window.players.size);
        // Create win lose screen
        const winLoseScreen = createWinLoseScreen(ithPlayer, window.players.size);
        winLoseScreen.style.display = "none";
        document.getElementById("player-region").appendChild(winLoseScreen); 
        ithPlayer++;
    }

    // Adjust the bars based on number of players there are
    adjustPlayerBars();

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
        left: `${(ithPlayer - 1) * (100 / noPlayers) + (100 / (2 * noPlayers) - 100 / 8)}vw`, 
        top: "0%",
    });

    // Create the username and bet label
    const usernameLabel = createLabel(key, "h3", `username-label-${ithPlayer}`, "lightgray", "clamp(0vw, 2vw, 3vh)");
    Object.assign(usernameLabel.style, {
        position: "absolute", 
        left: "50%",
        top: "2%",
        transform: "translate(-50%)",
        textDecoration: "underline"
    });
    const betLabel = createLabel(`Bet: \$${value.currBet}`, "h3", `bet-label-${ithPlayer}`, "lightgray", "clamp(0vw, 1.5vw, 2.2vh)");
    Object.assign(betLabel.style, {
        position: "absolute", 
        left: "40%",
        top: "14%",
        transform: "translate(-50%)"
    });

    // Create the score div
    const playerScore = createPlayerScore(ithPlayer);

    // Create the buttons
    const hitButton = createButton("Hit", `hit-button-${ithPlayer}`, "40%", "5%", "green", "rgba(24, 87, 12, 0.89)", "lightgray", "clamp(0vw, 1vw, 1.5vh)");
    const stopButton = createButton("Stop", `stop-button-${ithPlayer}`, "40%", "5%", "red", "rgba(150, 0, 0, 0.89)", "lightgray", "clamp(0vw, 1vw, 1.5vh)");
    Object.assign(hitButton.style, {
        position: "absolute",
        left: "50%",
        bottom: "7%",
        transform: "translate(-50%)"
    })
    Object.assign(stopButton.style, {
        position: "absolute",
        left: "50%",
        bottom: "1%",
        transform: "translate(-50%)"
    })
    hitButton.style.display = "none";
    stopButton.style.display = "none";

    // Add card holder
    const cardHolder = document.createElement("div");
    cardHolder.id = `player-cards-${ithPlayer}`;
    Object.assign(cardHolder.style, {
        position: "absolute",
        bottom: "2%",
        left: "50%",
        width: "80%",
        height: "65%",
        transform: "translate(-50%)"
    });

    // Append all the elements to the player profile
    playerProfile.appendChild(usernameLabel);
    playerProfile.appendChild(betLabel);
    playerProfile.appendChild(playerScore);
    playerProfile.appendChild(hitButton);
    playerProfile.appendChild(stopButton);
    playerProfile.appendChild(cardHolder);
    
    // Append the player profile and win/lose screens to player main page.
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
        top: "11%",
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

/* Creates a new button
REQUIRES: pretty explanatory
*/
function createButton(buttonText, buttonID, bWidth, bHeight, backColor, hColor, textColor, ftSize) {
    const newButton = document.createElement("button");
    newButton.id = buttonID;
    newButton.textContent = buttonText;
    Object.assign(newButton.style, {
        width: bWidth,
        height: bHeight,
        backgroundColor: backColor,
        color: textColor,
        border: "3px solid var(--color)",
        textAlign: "center",
        fontSize: ftSize,
        whiteSpace: "normal",
        wordWrap: "break-word",
        overflowWrap: "break-word",
        cursor: "pointer",
        borderRadius: "4vh",
        transition: "background-color 0.3s ease",
        zIndex: "1000"
    })
    newButton.addEventListener("mouseenter", () => {
        newButton.style.backgroundColor = hColor;
    });
    newButton.addEventListener("mouseleave", () => {
        newButton.style.backgroundColor = backColor;
    });
    return newButton;
}

/*
Creates a new win lose screen
REQUIRES: - ithPlayer: must be an int from 1 to 4
          - noPlayers: must be an int from 1 to 4
*/
function createWinLoseScreen(ithPlayer, noPlayers) {
    const newWinLoseScreen = document.createElement("div");
    const message = document.createElement("h2");
    newWinLoseScreen.id = `player-win-lose-screen-${ithPlayer}`;
    message.id = `player-win-lose-message-${ithPlayer}`;
    message.textContent = "WIN"
    Object.assign(newWinLoseScreen.style, {
        position: "absolute",
        left: `${(ithPlayer - 1) * (100 / noPlayers)}%`,
        top: "0%",
        width: `${100 / noPlayers}%`,
        height: "100%",
        backgroundColor: "rgba(0, 0, 0, 0.2)",
    })
    Object.assign(message.style, {
        position: "absolute",
        left: "50%",
        top: "35%",
        marginTop: "0",
        marginBottom: "0",
        fontSize: "clamp(0vw, 8vw, 12vh)",
        color: "green",
        transform: "translate(-50%) rotate(-20deg)",
        textShadow: "0 0 5px rgba(0, 0, 0, 0.5)"
    })
    newWinLoseScreen.appendChild(message);
    return newWinLoseScreen;
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
window.addEventListener("pagehide", () => {
    // Clear the heartbeat and use sendBeacon to deregister the tab
    navigator.sendBeacon(`${window.tabApiPrefix}/${tabId}/deregister`, "");
});