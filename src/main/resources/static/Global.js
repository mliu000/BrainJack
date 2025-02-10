///// VARIABLES /////

window.apiPrefix = "http://localhost:8080/api/brainjack"
window.dealer = null;
window.players = [];

///// FUNCTIONS /////

function getDealer() {
    
}

// General post request function
window.postRequest = async function(url, data) {
    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }, 
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error(`HTTP error ! Status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("Error in Post Request", error);
        return null;
    }
}

///// PROCEDURAL CODE /////

window.gameState.dealer = getDealer();