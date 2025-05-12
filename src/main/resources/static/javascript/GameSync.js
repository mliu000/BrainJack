/*
@Mu Ye Liu - May 2025

Represents the synchronization functions for my game tab.
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

/* Changes the count based on the number of game tabs open
REQUIRES: - operation: must be 1 (increment) or -1 (decrement) 
*/
function changeGameTabCount(operation) {
    let currCount = parseInt(localStorage.getItem("numGameTabsInAction")) || 0;
    if (operation === 1) {
        currCount++;
    } else if (operation === -1) {
        currCount--;
    }

    localStorage.setItem("numGameTabsInAction", currCount);
}
