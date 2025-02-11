/*
@Mu Ye Liu - Feb 2025

Represents the javascript code for the statistics.html page
*/

///// FUNCTIONS /////

// 
/*
Displays the statistics for the logged in player.
REQUIRES: 
- player: must be a valid player with appropriate fields
*/
function displayStatistics(player) {
    console.log(player);
    // Set the text fields
    document.getElementById("statistics-page-header").textContent = "Statistics for: " + player.username;
    document.getElementById("rounds-played").innerHTML = 
        `<strong>Rounds Played: </strong> ${player.roundsPlayed}`;
    document.getElementById("rounds-won").innerHTML = 
        `<strong>Rounds Won: </strong> ${player.roundsWon}`;
    document.getElementById("win-percentage").innerHTML = 
        `<strong>Win Percentage: </strong> ${player.winPercentage}%`;
    document.getElementById("total-earnings").innerHTML = 
        `<strong>Career Earnings: </strong> \$${player.totalEarnings}`;
    document.getElementById("session-earnings").innerHTML = 
        `<strong>Earnings Since Login: </strong> \$${player.gameEarnings}`;
    
    // Finally, display the entire screen
    document.getElementById("success-screen").style.display = "flex";
}

///// INITIALIZATION PROCEDURAL CODE /////


// Trys to get the username from the link. If the variable doesn't exist, then display error message
document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    if (!params.has('username')) {
        // Username variable in url doesn't exist
        document.getElementById("error-screen").style.display = "flex";
    } else {
        // Username variable in url does exist
        window.getLoggedInPlayers().then(() => {
            // Get the username from the variable
            const username = params.get("username");
            if (window.players.has(username)) {
                // Case 1: The username is logged in, so display statistics
                displayStatistics(window.players.get(username));
            } else {
                // Case 2: The username is not logged in, so display error screen
                document.getElementById("error-screen").style.display = "flex";
            }
        });
    }

});