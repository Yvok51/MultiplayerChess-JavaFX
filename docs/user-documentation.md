# Multiplayer Chess
## User documentation
### Introduction
Multiplayer Chess is a multiplayer game of chess in which two players join a match and play until the match ends or one of them resigns.

### Starting application
The server is started by calling `mvn exec:java` in the root directory. The client application is started with `mvn javafx:run`.

The server has a fixed port number and as such only one instance can run at a time. The IP address the client connects to is also fixed as `localhost`.

### Pre Game
After starting the application the user is met with three buttons. The bottom button `Quit` will exit the application.
The midlle button `Join Game` will open a new menu which is used to join a started game and the final top button `Start Game` will start a game.

Each game has a unique identifier that can be used to join a game. When a game is created the user is shown a new screen with a chess board but, more importantly for now, also the game's identification code in the lower left corner below the `Resign and Quit` button.

This ID is used in the join game screen, which is accessed after pressing the `Join Game` button, to join a started game. The code is simply written into the text field and after the `Join Game` button is pressed the client will attempt to join a game with the given ID. If the match with such ID does not exist or the game is already ongoing then the user is told the ID is invalid.

### Game
The game itself follows the traditional [rules of chess](https://en.wikipedia.org/wiki/Rules_of_chess) with the exception that a pawn automatically promotes to a queen and the treefold repetition is not checked for. The game does not have a stopwatch.

The player who started the game will play with white pieces, while the player who joined will play with black pieces.

When the game reached the end a popup will appear that annouces who has won and with a button that brings the user back to the main menu.
