# Untitled Casino App
#### Created by William Kerr and Jack Wagenheim

Untitled Casino is an app that allows players to choose from various luck-based games to earn credits.
A player uses their credits to play a game. The player can choose to purchase more credits to continue playing Untitled Casino.

## Screens
- **Home Screen**: The app opens to this screen. It shows a logo for the game and allows players to move to either the Games, Credits, or Get Help screen. Above the buttons, there is a component which displays the amount of credits held by the player.
- **Game Selection Screen**: Allows players to choose between available games. Once a game is chosen, the player is then take them to the Game screen.
- **Game Screen**: Standard layout for all games, with a section in the middle in which the game is placed. It is dynamic so that only one screen is necessary in order to support every game. The name of the game is displayed at the top of the screen. The player’s current balance is displayed above the game. Beneath the game, there is an input field in which the player can input their bet. Once the player has finished deciding on an amount, they can press the play button to run the game.
- **Credits Screen**: Allows the player to purchase different amounts of credits at different prices. Underneath is a grid of different choices, with different amounts of credits, cost to purchase, and images for each. The player’s current balance is be displayed above everything. Choosing a credit option and pressing purchase will take the player to the Confirm Screen.
- **Confirm Screen**: Allows the player to confirm their purchase. After confirming or denying, a snackbar will appear at the bottom of the screen, showing the player's choice. The player will then be sent back to the home screen.
- **History Screen**: Allows the player to view their game or purchase history, depending on from which screen it was accessed. The screen is just a list of cards, showing each instance.
- **Get Help Screen**: Brings the player to a site for gambling addiction.

## Games
* **_Flip A Coin_**:
  - _Payout_: 2x
  - _Participation_: 0
  - A player chooses heads or tails and then flips a coin. If it lands on their chosen side, they receive _Payout_. If it does not, the player wins _Participation_.
* **_Daily Number_**:
  - _Payout_: 1000x
  - _Participation_: Original Bet * Closeness To Number. Closeness To Number is be a percentage between 0 and 100. The player will receive back a range between 0 up to their original bet for playing.
  - The player will press a button to choose a random number between 0 and MAX_NUMBER. If it lands on the correct number, the player receives _Payout_. If not, the player walks away with _Participation_.
* **_Hi Lo_**:
    - _Payout_: Original Bet * 1.2^(Rounds Played)
    - _Participation_: 0
    - The player will be presented a card from a deck, and have to guess whether the next will be higher or lower. If they get it right, they move forward a round, and can continue doing so until they hit the round limit. The player can also choose to cash out at any point, and take their earnings, leaving with *Payout* (this will also happen if they make it to max round). If the player is wrong, they win *Participation*.
  
## Database
Untitled Casino's database has three entity types.
- **playerEntity**: Stores the player's credits and act as a parent for gameEntity and purchaseEntity
- **gameEntity**: Stores each game session a player has run, including information such as game type, cost, payout, and date
- **purchaseEntity**: Stores each purchase a player makes, including information such as purchaseType the cost, amount of credits, and date of transaction.

## API Usage
Every day on [itchybarn.com](www.itchybarn.com) (a website hosted by Jack Wagenheim, a co-developer) a different number is chosen, and made available to anybody using its API. At the end of every day (in an undecided timezone), the number will change.

Untitled Casino uses Ktor network requests to use the [itchybarn.com](www.itchybarn.com) API. The daily number is used in the _Daily Number_ game.