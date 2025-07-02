# BrainJack 

BrainJack is a very fun multiplayer game that incorporates traditional blackjack with the ability to answer non-trivial questions to half your loss. This game allows up to 4 players play against a dealer at the same time, minicking a real blackjack game at a casino.

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

## Implementation Overview

BrainJack is a full-stack multiplayer blackjack game with backend-controlled logic, session persistence, and anti-exploit design.

### Tech Stack

- **Backend**: Java Spring Boot (RESTful API)
- **Frontend**: Vanilla JavaScript, HTML, CSS
- **Database**: MySQL (persistent account and stat tracking)
- **Build Tool**: Maven

### Backend Logic

- Core game logic (card draws, dealer behavior, bust detection) is handled securely on the backend
- Frontend sends REST API calls to avoid frontend-exploitable logic
- Backend fully manages player actions, betting flow, and round resolution

### Multiplayer Tab Management

To maintain consistency and prevent session conflicts:

- **Tab Registration**:
  - Each active game tab is registered in the backend
  - A server-side counter tracks the number of open tabs
- **Single-Tab Enforcement**:
  - Only one game tab is allowed to run at a time
  - New tabs are blocked to ensure game consistency
- **Lobby Lockout**:
  - While a game is active, login/logout functionality in the lobby is disabled
- **Heartbeat Mechanism**:
  - A periodic heartbeat keeps track of active tabs
  - If a tab closes unexpectedly, the backend auto-decrements after a timeout

### Persistent Player Stats

- Player accounts are stored in MySQL
- Tracks stats across sessions:
  - Net win/loss
  - Total matches played
  - Matches won

---

## Setting up

### Requirements

- Java 17+
- Maven
- MySQL 8.0+

### Steps to run the project:

- Clone the Repo
- Go to ```src/main/resources/application.properties```, and edit the following: 
    - ```spring.datasource.url=jdbc:mysql://localhost:<your_port>/brainjack```, default ```3306```.
    - ```spring.datasource.username=<your_mysql_username>```, default ```root```.
    - ```spring.datasource.password=<your_mysql_password>```
- From the root directory, type: ```mvn spring-boot:run```.


