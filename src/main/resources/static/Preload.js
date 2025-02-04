///// VARIABLES /////

window.gameState = {
    dealer: null,
    players: []
};

///// FUNCTIONS /////

// Initialize elements based on the game states
function initializeElements() {
    // Get the stats button element
    let statsButton = document.getElementById("stats-button");
    if (statsButton && statsButton.style.visibility !== "hidden") {
        statsButton.style.visibility = "hidden";
        localStorage.setItem("stats-button-visibility", "hidden");
    }
}

function getDealer() {

}

///// PROCEDURAL CODE ////

window.dealer = getDealer();

initializeElements();

