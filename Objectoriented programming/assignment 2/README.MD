# Object Oriented Design

## Class diagram:
![Alt text](OOD/Class%20diagram.png)

These are my notes when making this diagram:


**Tile class:**
Pos, name and getName() all that is same for them all of them. A get name is not needed as the position will only be used to verify the name of the tile.


**Start tile:**
Nothing needed. All is stored within game as it does not make sense to store player laps here nor the giveMoneyCuzYouPastMe() method


**Free tile:**
Nothing but it is static and so is start tile as there will only be one of each.


**Player:**
Player stores name, position, money, isInJail, owned property as a list of strings and the laps the player has gone as i felt this was more relevant to store on each player as there would be a unknown amount of players so storing in game would look ugly. My thinking now is that I might be able to add to the method that changes the position of player to also check if a lap has gone and in that case see if laps has gone to 10. 

In player there is now set() and get() methods for position and setPos() returns a bool as it will be responsible for checking if the player has gone the entire board as that method will have to reset the pos to 0 anyway when it passes go. Thus move() will also return a bool to tell the game object that someone has gone 10 laps. So laps will be handled internally which won't be a problem as both money : int and move() is within player.

enterOrLeaveJail() will be one method as it is boolean, it is unnecessary to have two methods.

Pay(int) also returns a bool and this is to check if the player has gone bankrupt.


**Dice:**
Because the player has to move and may have to pay or buy estate between twin rolls the twin roll calculation can not be done within the dice object this but has to be done within the game object. The dice therefore just return one die at a time and it will just be called twice a tuple or simular could be returned but the value of doing this is very limited. I renamed it to Die to fit better.


**Game:**
StartingGame(String[]) will take in all the names of the players and create new players with addPlayer(String).

endGame() will be activated through either movePlayer() that will only return a bool as all else relevant is calculated within player and not game. Thus we can return a bool every time a player moves that will be true if a player has reached lap 10 and with that starting the method endGame(). Then endGame() can start by a player paying and as the calculation of how much money player has is done within the player object we can return a isBroke bool that will trigger endGame if true.
When the endGame() is triggered the method will simply go trough its array of Players and do getCurrentMoney().

MovePlayer() will call the die twice as previously stated and then send that information to the player will move and game will check new pos of player and ask the UI if Player wants to buy the property it landed on after game has checked the Player pos and seen that it is a Property tile.

buyProperty() will then activate if UI wants to, where it will then use the Player.getPos() to look at the Tile class to see the cost with the methods get name (because it would be a very unfunny Monopoly without street names). Then use that name to see the cost (exactly the same way as when it looked if Player should pay rent earlier).


**Computer player:**
To be able to implement computer players in the future I decided to let the computers have the same class as normal players by adding a difficulty attribute to the Player class. My first thoughts was that a subclass was to be made to player. But because of the simple nature of Monopoly a computer only has to actually make one decision and that is to buy or not. And because buying is better by a long shot a higher difficulty would be used as a chance the computer will buy the property. The difficulty could also be used to check if the Player object is a computer or not. So before consulting the UI the Game object should see if the player has a difficulty or not and then act accordingly by rolling the dice or by using chance and the difficulty int of the bot to see if it will buy or not.

## Object diagram:
![Alt text](OOD/Object%20diagram.png)

## Sequence diagram:
![Alt text](OOD/Sequence%20diagram.png)