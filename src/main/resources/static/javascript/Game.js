/*
@Mu Ye Liu - May 2025

Represents the game page for my brain jack game.
*/

///// MAIN FUNCTIONS /////

///// HELPER FUNCTIONS /////

// Changes the count based on the number of game tabs open
function changeGameTabCount(operation) {
    let currCount = parseInt(localStorage.getItem("numGameTabsInAction")) || 0;
    if (operation === 1) {
        currCount++;
    } else if (operation === -1) {
        currCount--;
    }

    localStorage.setItem("numGameTabsInAction", currCount);
}

// Overrides the content page to display error message when game is in play
function gameInPlayOverride() {
    const mainPage = document.getElementById("main-page");
    const overridePage = document.getElementById("override-page");
    mainPage.style.display = "none";
    overridePage.style.display = "block";
}


///// ACTION LISTENERS /////

// Set on load
window.addEventListener("DOMContentLoaded", () => {
    // Check to see whether or not game was already loaded in another window. 
    // If so, do not load it again.
    if (localStorage.getItem("gameInAction") === true) {
        // placeholder code
        document.body.style.display = "none";
    }
    changeGameTabCount(1);

    console.log(localStorage.getItem("numGameTabsInAction"));

    // If count != 1, then do not allow another game to be played
    if (localStorage.getItem("numGameTabsInAction") !== "1") {
        gameInPlayOverride();
    }
});

// Set on unload
window.addEventListener("beforeunload", () => {
    changeGameTabCount(-1);
});
