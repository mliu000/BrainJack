/*
@Mu Ye Liu - May 2025

Represents the code for the game itself
Includes adding action listeners, case handling, and api calls
*/

///// GAME LOGIC FUNCTIONS /////

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
                    playerStartDraws();
                    dealerStartDraw();
                    startGameFlow();
                }
            } catch (error) {
                // Error message already thrown in caller function. Simply catch it
            }
        }
        
    });

    placeBetsPopup.style.display = "block"; 
}

// Initializes the player panel, mainly includes the 2 buttons
function initializePlayerButtonsPanel() {
    for (let i = 1; i <= window.players.size; i++) {
        // Initialize player hit button
        playerHitButtonList[i - 1].addEventListener("click", async () => {
            const response = await playerHit(userNameLabelList[i - 1].textContent);
            addPlayerCard(i);
            playerScoreList[i - 1].textContent = response.score;
            if (response.busted) {
                playerHitButtonList[i - 1].disabled = "true";
                playerStopButtonList[i - 1].disabled = "true";
                await wait(1000);
                playerHitButtonList[i - 1].disabled = "false";
                playerStopButtonList[i - 1].disabled = "false";
                playerWinLoseMessage[i - 1].textContent = "LOSE";
                playerWinLoseMessage[i - 1].style.color = failureColor;
                playerWinLoseScreen[i - 1].style.display = "block";
                nextPlayer(i);
            }
        });

        // Initialize player stop button
        playerStopButtonList[i - 1].addEventListener("click", () => {
            nextPlayer(i);
        });
    }
}

// Adds a new player card
// REQUIRES: - ithPlayer: must be an integer from 1 to 4
function addPlayerCard(ithPlayer) {
    const playerHand = window.players.get(userNameLabelList[ithPlayer - 1].textContent).hand;
    const cardSuite = playerHand[playerHand.length - 1].suite.toLowerCase();
    const cardNumber = playerHand[playerHand.length - 1].number;

    const cardDiv = createCard(cardNumber, cardSuite, "slideInFromBelow");
    addPlayerCardDivProperties(cardDiv, ithPlayer);

    playerCardHolder[ithPlayer - 1].appendChild(cardDiv);
}

/* Next player, or allows dealer to play in case of last player
REQUIRES: - ithPlayer: must be a valid integer from 1 to 4
*/
function nextPlayer(ithPlayer) {
    playerStopButtonList[ithPlayer - 1].style.display = "none";
    playerHitButtonList[ithPlayer - 1].style.display = "none";
    if (ithPlayer === window.players.size) {
        // TODO: Allow dealer to play
        dealerPlay();
    } else {
        playerStopButtonList[ithPlayer].style.display = "block";
        playerHitButtonList[ithPlayer].style.display = "block";
    } 
}

// Updates all the player bets displays
function updatePlayerBetDisplays() {
    // Reinitializes the bet labels for each player
    for (let i = 1; i <= window.players.size; i++) {
        betLabelList[i - 1].textContent = `Bet: \$${window.players.get(userNameLabelList[i - 1].textContent).currBet}`;
    }
}

// Allow dealer to play
function dealerPlay() {
    
}

// Start the game flow
function startGameFlow() {
    playerHitButtonList[0].style.display = "block";
    playerStopButtonList[0].style.display = "block";
}

// Start draws for all players, and updates the score of the players
async function playerStartDraws() {
    for (let i = 1; i <= window.players.size; i++) {
        const username = userNameLabelList[i - 1].textContent;
        const response = await participantStartDraw(username);
        const playerHand = response.hand;

        for (let j = 0; j < 2; j++) {
            const cardDiv = createCard(playerHand[j].number, playerHand[j].suite.toLowerCase(), "slideInFromBelow");
            addPlayerCardDivProperties(cardDiv, i);
            playerCardHolder[i - 1].appendChild(cardDiv);
        }

        playerScoreList[i - 1].textContent = response.score;
    }
}

// Start draw for dealer
async function dealerStartDraw() {
    const response = await participantStartDraw("d");

    for (let i = 0; i < 2; i++) {
        const cardDiv = createCard(response.hand[i].number, response.hand[i].suite.toLowerCase(), "slideLeftFromRight");
        addDealerCardDivProperties(cardDiv);
        dealerCards.appendChild(cardDiv);
    }
    dealerScore.textContent = response.score;
}

///// HELPER FUNCTIONS /////

/* Sets a timeout (pause) in the code
REQUIRES: - ms: Must be an intger, which represents the number of milliseconds. 
*/
function wait(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

/* Creates a new card with some properties ()
REQUIRES: - cardNumber: must be a valid vard number
          - cardSuite: must be a valid suite
          - animation: must be valid animation from the css
*/
function createCard(cardNumber, cardSuite, animation) {
    const img = document.createElement("img");
    const div = document.createElement("div");
    img.src = `../images/52_playing_cards/${cardNumber}_of_${cardSuite}.png`
    img.alt = `${cardNumber} of ${cardSuite}`;
    Object.assign(img.style, {
        width: "100%",
        height: "100%",
        objectFit: "cover",
        display: "block"
    });
    Object.assign(div.style, {
        borderRadius: "5%",
        boxShadow: "0vh 0vh 10vh rgb(0, 0, 0)",
        animation: `${animation} 0.3s ease-out forwards`
    });
    div.appendChild(img);
    return div;
}

/* Assigns the properties for the dealer card div
REQUIRES: - cardDiv: must be a valid div
*/
function addDealerCardDivProperties(cardDiv) {
    Object.assign(cardDiv.style, {
        position: "absolute",
        height: "80%",
        top: "50%",
        transform: "translate(500%, -50%)",
        left: `${dealerCards.childElementCount * 13}%`,
    })
}

/* Assigns the properties for the player card div
REQUIRES: - cardDiv: must be a valid div
          - ithPlayer: must be a valid int from 1 to 4
*/
function addPlayerCardDivProperties(cardDiv, ithPlayer) {
    Object.assign(cardDiv.style, {
        position: "absolute",
        width: "60%", 
        left: "50%",
        transform: "translate(-50%, 200%)",
        top: `${playerCardHolder[ithPlayer - 1].childElementCount * 13}%`,
    })
}

///// MAIN FUNCTION THAT CALLS THE LOGIC FUNCTIONS/////

// Adds the basic action listeners for the buttons, doing it by div
function addActionListeners() {
    startPopupActionListeners();
    addBetInputActionListener();
    initializePlayerButtonsPanel();
}

