// The function that initializes which buttons are available upon startup
function initialize() {
    let statsButton = document.getElementById("stats-button");
    let middle3Buttons = document.getElementById("middle-3-buttons");
    statsButton.style.visibility = "hidden";
}

// Allow initialize() to run once the entire program has started up
window.onload = function() {
    initialize();
};