# Untitled Casino App
#### Created by William Kerr and Jack Wagenheim

Untitled Casino is an app that allows players to choose from various luck-based games to earn credits.
A player uses their credits to play a game, and is allowed to go into the negatives by playing a game. If they do not win enough back to make it into the positives by the end of the game session, they will go into debt. Once in debt, the player will have to purchase more credits to continue playing Untitled Casino.

## Screens
- **Home Screen**: The app opens to this screen. It shows a logo for the game and allows players to move to either the Games or Credits screen. Beneath the buttons, there is a component which displays the amount of credits held by the player.
- **Game Selection Screen**: Allows players to choose between available games. Once a game is chosen, the player will then take them to the Game screen.
- **Game Screen**: Standard layout for all games, with a section in the middle in which the game will be placed. It will be dynamic so that only one screen is necessary in order to support every game. The name of the game will be displayed at the top of the screen in the top app bar. The player’s current balance will be displayed above the game. Beneath the game there will be a cost for the game, which will be half text of “Cost: “ and then an input field for the player to input their bet. Once the player has finished deciding on an amount, they can press the play button underneath to run the game. If a player ever runs out of credits (after a game is played), they will be sent to the Debt screen.
- **Debt Screen**: This screen will have text explaining to the player that they have gone into debt, and that they must get out of it by purchasing more credits. Pressing the okay button will then take them to the Credits screen.
- **Credits Screen**: Allows the player to purchase different amounts of credits at different prices. It will have instructions at the top explaining the process. Underneath will be a grid of different choices, with different amounts of credits, cost to purchase, and (ideally) images for each. The player’s current balance will be displayed underneath everything. Choosing a credit purchase will take the player to the Confirm Screen.
- **Confirm Screen**: Allows the player to confirm their purchase. After confirming or denying, a snackbar will appear at the bottom of the screen, showing the player's choice. The player will then be sent back to the home screen.

## Games
* **_Flip A Coin_**:
  - _Payout_: 2x
  - _Participation_: 0
  - A player chooses heads or tails and then flips a coin. If it lands on their chosen side, they receive _Payout_. If it does not, the player wins _Participation_.
* **_Random Number_**:
  - _Payout_: 1000x
  - _Participation_: Original Bet * Closeness To Number. Closeness To Number will be a percentage between 0 and 100. The player will receive back a range between 0 up to their original bet for playing.
  - The player will press a button to choose a random number between 0 and MAX_NUMBER. If it lands on the correct number, the player receives _Payout_. If not, the player walks away with _Participation_.

## Database
Untitled Casino's database will have three entity types.
- **playerEntity**: Will store some (undecided) information and act as a parent for gameEntity and purchaseEntity
- **gameEntity**: Will store each game session a player has run, including information such as game type, cost, payout, and date
- **purchaseEntity**: Will store each purchase a player makes, including information such as purchaseType (the cost and amount of credits) and date

## API Usage
Every day on [itchybarn.com](www.itchybarn.com) (a website hosted by Jack Wagenheim, a co-developer) a different number will be chosen, and made available to anybody using its API. At the end of every day (in an undecided timezone), the number will change.

Untitled Casino will use Ktor network requests to use the [itchybarn.com](www.itchybarn.com) API. The daily number will be used in the _Random Number_ game.