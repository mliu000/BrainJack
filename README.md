## BrainJack 

Brainjack is a very fun multiplayer game that incorporates traditional blackjack with the ability to answer non-trivial questions to half your loss. This game is **multiplayer**, allowing up to 4 players play against a dealer at the same time, minicking a real blackjack game in a casino.

### Game Features

#### *Multiplayer*

Log In up to 4 players at once, each of you will have a personal account with a personalized username and stat tracking. 

<img src="../brainjack_nwhacks_2025_rebuild/src/main/resources/static/images/Readme_pictures/Screenshot 2025-05-20 125711.png" alt="Gameplay Screenshot" width="400"/>

<img src="../brainjack_nwhacks_2025_rebuild/src/main/resources/static/images/Readme_pictures/Screenshot 2025-05-20 125907.png" alt="Gameplay Screenshot" width="400"/>

<img src="../brainjack_nwhacks_2025_rebuild/src/main/resources/static/images/Readme_pictures/Screenshot 2025-05-20 125928.png" alt="Gameplay Screenshot" width="400"/>

#### *Set Your Bets*

Each player gets to set a bet value, which if they win the match, they will win the amount they betted. If they lose, they they will answer a random question to potentially half their loss compared to their bet.

<img src="../brainjack_nwhacks_2025_rebuild/src/main/resources/static/images/Readme_pictures/Screenshot 2025-05-20 125823.png" alt="Gameplay Screenshot" width="400"/>

##### *Answer Questions to Half your loss*

If the player loses, either by busting or the dealer beats them, they will answer a randomly generated question in which they can half their bet to 

<img src="../brainjack_nwhacks_2025_rebuild/src/main/resources/static/images/Readme_pictures/Screenshot 2025-05-20 125938.png" alt="Gameplay Screenshot" width="400"/>

##### *View Player Statistics*

In Between matches, you can view statistics (such as net win/loss, number of matches played, number of matches won) all one one screen.

<img src="../brainjack_nwhacks_2025_rebuild/src/main/resources/static/images/Readme_pictures/Screenshot 2025-05-20 125759.png" alt="Gameplay Screenshot" width="400"/>

### Implementation Features

This game was implemented with a Java Springboot using RESTFul API for the server, with Vanilla JS frontend + html + css. The core logic of the game is on the Java backend, while the animations, dom manipulation and styling is on the frontend. The JS frontend sends API calls to the Java Backend to perform sensitive game logic that is exploitable on the frontend. 

The game uses mutexes to stagger tab loading to prevent the game from loading abnormally. Whenever a new game tab is opened, the tab is registered in the Java backend, and the total amount of game tabs opened is returned. This allows the feature of only allowing one game tab to allow the game to run at a time (to ensure it runs smoothly). Also, when at least one game tab is running, the lobby is blocked to disallow players from logging out while the game is running. When the tab is closed, it gets deregistered, decrementing the number of tabs open. Also, a heartbeat feature is set up to allow the server to automatically decrement the tab counter if a tab were to fail to deregister after closing, ensuring the number of tabs is accurately tracked. 

Finally, this program also uses mySQL to store player accounts and stats between sessions, so they can be retrieved and used again.
