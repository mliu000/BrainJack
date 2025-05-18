/*
@Mu Ye Liu - May 2025

Represents the code for the game itself
Includes adding action listeners, case handling, and api calls
*/

///// ACTION LISTENERS FUNCTIONS /////

// Adds the action listeners for the start up screen
function startPopupActionListeners() {
    startOkButton.addEventListener("click", () => {
        startPopup.style.display = "none";
        initializePlaceBetsScreen();
    });
}

// Add action listener to place bets screen to limit input to numbers from 1 to 1000000
// Uses regex
function addBetInputActionListener() {
    const input = document.getElementById("place-bets-input");

    input.addEventListener("input", () => {
        input.value = input.value.replace(/[^0-9]/g, "");
        if (input.value.startsWith("0")) {
            input.value = input.value.replace(/^0+/, '');
        }
    });
}

///// HELPER FUNCTIONS

// Handles the display of the place bets screen by initializing it and then displaying it
function initializePlaceBetsScreen() {
    const playerEntries = Array.from(window.players.keys());
    let index = 0;

    placeBetsUsername.textContent = `For user: ${playerEntries[index]}`;

    placeBetsButton.addEventListener("click", async () => {
        if (placeBetsInput.value === "") {
            placeBetsMessage.textContent = "Bet Cannot Be Empty";
            Object.assign(placeBetsMessage.style, {
                    display: "block", 
                    color: failureColor
                });
        } else {
            try {
                await setPlayerBet(playerEntries[index], placeBetsInput.value);
                placeBetsMessage.textContent = "Bet Successfully Placed";
                Object.assign(placeBetsMessage.style, {
                    display: "block", 
                    color: successColor
                });
                placeBetsInput.value = "";
                placeBetsButton.disabled = true;
                await wait(1000);
                placeBetsButton.disabled = false;
                index++;
                if (index < playerEntries.length) {
                    placeBetsMessage.style.display = "none";
                    placeBetsUsername.textContent = `For user: ${playerEntries[index]}`;
                } else {
                    placeBetsPopup.style.display = "none";
                    index = 0;
                    updatePlayerBetDisplays();
                }
            } catch (error) {
                // Error message already thrown in caller function. Simply catch it
            }
        }
        
    });

    placeBetsPopup.style.display = "block"; 
}

// Updates all the player bets displays
function updatePlayerBetDisplays() {
    // Reinitializes the bet labels for each player
    for (let i = 1; i <= window.players.size; i++) {
        betLabelList[i].textContent = `Bet: \$${window.players.get(userNameLabelList[i].textContent).currBet}`;
    }
}

// Initializes the player panel
function initializePlayerPanel() {
    
}

/* Sets a timeout (pause) in the code
REQUIRES: - ms: Must be an intger, which represents the number of milliseconds. 
*/
function wait(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

///// MAIN FUNCTION, ADDS THE ACTION LISTENERS /////

// Adds the basic action listeners for the buttons, doing it by div
function addActionListeners() {
    startPopupActionListeners();
    addBetInputActionListener();
    initializePlayerPanel();
}

