# Black Jack Game
This is a Java implementation of the popular card game Black Jack. The game is designed with a focus on a robust and well-documented design that can handle change, rather than on usability or a nice user interface. The game supports different combinations of rules, making it a flexible design.

## Features
Playable Game: The game is playable with the operation Game::Stand implemented.
Rule Variants: The game supports different rule variants for when the dealer should take one more card. This includes the Soft 17 rule, where the dealer can get another card valued at 10 but still have 17 as the value of the ace is reduced to 1.
Winner Determination: The game has a variable rule for who wins the game. This could be who wins on an equal score (in one implementation the Dealer wins, in the other the Player).
Code Refactoring: The code for getting a card from the deck, showing the card and giving it to a player has been refactored to remove duplication and support low coupling/high cohesion.
Observer Pattern: The Observer-pattern is used to send an event to the user interface that a player (human or dealer) has got a new card in his hand. When the event is handled, the user interface is “redrawn” to show the new hand (with the new card) and the game is briefly paused for excitement.

## Usage
To build and run the game using gradle, follow these steps:

1. Clone this repository to your local machine.
2. Navigate to the project directory in your terminal.
3. Run gradle build to build the project.
4. Run gradle run to start the game.
5. Enjoy playing Black Jack!

## Contributing
Contributions are welcome! Please read our contributing guidelines before getting started.

## License
This project is licensed under [GNU General public license](https://www.gnu.org/licenses/gpl-3.0.html). See LICENSE for more information.

Please note that this README file should be updated as you continue developing your application to ensure it accurately represents your project. Good luck with your project! 👍