# BrainJack 

Brainjack is a very fun multiplayer game that incorporates traditional blackjack with the ability to answer non-trivial questions to half your loss. This game allows up to 4 players play against a dealer at the same time, minicking a real blackjack game at a casino.

## Game Features

### *Multiplayer*

Log In up to 4 players at once, each of you will have a personal account with a personalized username and stat tracking. 

<img src="src/main/resources/static/images/Readme_pictures/Screenshot 2025-05-20 125711.png" alt="Gameplay Screenshot" width="400"/>

<img src="src/main/resources/static/images/Readme_pictures/Screenshot 2025-05-20 125907.png" alt="Gameplay Screenshot" width="400"/>

<img src="src/main/resources/static/images/Readme_pictures/Screenshot 2025-05-20 125928.png" alt="Gameplay Screenshot" width="400"/>

### *Place Your Bets*

Each player gets to place the amount they want to bet, which if they win, they will win the amount they betted. If they lose, they they will answer a random question to potentially half their loss compared to their bet.

<img src="src/main/resources/static/images/Readme_pictures/Screenshot 2025-05-20 125823.png" alt="Gameplay Screenshot" width="400"/>

### *Answer Questions to Half your loss*

If the player loses, either by busting or the dealer beats them, they will answer a randomly generated question in which  they can half their loss if answered correctly.

<img src="src/main/resources/static/images/Readme_pictures/Screenshot 2025-05-20 125938.png" alt="Gameplay Screenshot" width="400"/>

### *View Player Statistics*

In Between matches, you can view statistics (such as net win/loss, number of matches played, number of matches won) all on one screen.

<img src="src/main/resources/static/images/Readme_pictures/Screenshot 2025-05-20 125759.png" alt="Gameplay Screenshot" width="400"/>

### Implementation Features

This game was implemented with a Java Springboot using RESTFul API for the server, with Vanilla JS frontend + html + css. The core logic of the game is on the Java backend, while the animations, dom manipulation and styling is on the frontend. The JS frontend sends API calls to the Java Backend to perform sensitive game logic that is exploitable on the frontend. 

The game uses mutexes to stagger tab loading to prevent the game from loading abnormally. Whenever a new game tab is opened, the tab is registered in the Java backend, and the total amount of game tabs opened is returned. This allows the feature of only allowing one game tab to allow the game to run at a time (to ensure it runs smoothly). Also, when at least one game tab is running, the lobby is blocked to disallow players from logging out while the game is running. When the tab is closed, it gets deregistered, decrementing the number of tabs open. Also, a heartbeat feature is set up to allow the server to automatically decrement the tab counter if a tab were to fail to deregister after closing, ensuring the number of tabs is accurately tracked. 

Finally, this program also uses mySQL to store player accounts and stats between sessions, so they can be retrieved and used again.

## Instructions to run on local computer

After cloning this project, it should run automatically because it uses Java Maven, which automatically managed the dependencies. To run the project, type the following in the terminal (root directory is fine): 

**mvn spring-boot:run**

However, you will have to set up the mySQL, which the instructions are below:

### Setting up mySQL

Download mySQL, preferrably with version 8 or newer. Once downloaded, create a database and give it name. Then in the ***application.properties*** file (in the resource folder), and change the following lines to: 

spring.datasource.url=jdbc:mysql://localhost:<**Your Port**>/<**Your database name**> <br>
spring.datasource.password=<**Your sql account password**>


