/*
@Mu Ye Liu - May 2025

Represents the synchronization and tabManagement for my game.
*/

///// MUTEX FUNCTIONS FUNCTIONS /////

// Attmepts to acquire the mutex. If it is locked, then the lock is not acquired. If 
// the mutex lock is stuck due to misbehaviour, then acquire it after 10 seconds have passed.
function acquireLock() {
    const lockKey = "gameTabMutex";
    const lockTime = Date.now();
    const existingLock = localStorage.getItem(lockKey);

    // Check if the lock is expired (e.g., after 5 seconds)
    if (existingLock === null || (lockTime - parseInt(existingLock) > 10000)) {
        localStorage.setItem(lockKey, lockTime.toString());
        return true;
    }
    return false;
}

// Release mutex if it it hasn't been released yet
function releaseMutex() {
    localStorage.removeItem("gameTabMutex");
}

// Wait for mutex lock to be available. This async function
async function waitForMutex() {
    return new Promise((resolve) => {
        const checkLock = () => {
            if (acquireLock()) {
                resolve();
            } else {
                setTimeout(checkLock, 100);
            }
        };
        checkLock();
    });
}

///// API'S /////

/* 
Registers the tab using api
REQUIRES: - tabId: must be a valid tabId
*/
async function registerTab(tabId) {
    return generalTabPostRequest(tabId, "register");
}

/*
deregisters teh tab using api
REQUIRES: - tabId: must be a valid tabId
*/
async function deregisterTab(tabId) {
    return generalTabPostRequest(tabId, "deregister");
}

/*
updates the state of the game tab via heartbeat every 5 seconds
REQUIRES: - tabId: must be a valid tabId
*/
function heartbeat(tabId) {
    async function sendHeartBeat() {
        generalTabPostRequest(tabId, "heartbeat")
    }
    
    const heartBeatId = setInterval(sendHeartBeat, 5000);
    window.addEventListener("beforeunload", () => clearInterval(heartBeatId));
}

///// HELPER FUNCTIONS /////

/*
operates based on tab id
REQUIRES: - tabId: must be a valid tabId
          - operation: must be one of: "register", "deregister", "heartbeat"
RETURNS: Results from api
*/
async function generalTabPostRequest(tabId, operation) {
    try {
        const url = window.tabApiPrefix + `/${tabId}/${operation}`;
        return await window.postRequest(url, "", "application/json");
    } catch (error) {
        console.error("Unable to process request:" + error.message);
    }
}