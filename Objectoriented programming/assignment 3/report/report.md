# Reflection on My Design Plan
In my initial design plan, I overlooked the need for a Board class due to my attempt to condense the number of classes. However, I now realize that having a separate class for the board makes it easier to modify in the future. The Start class was present but had limited use. My first design aimed to use modulus to reset the player position when the dice roll plus the current position exceeded the board size and to give the player money if they passed the StartTile. I did not include a HumanPlayer class, thinking that an AI class would not require a separate class, but reflecting back, dividing it into two classes would have been a wiser decision.

I kept my design plan simple and did not want to include more than what was needed. I renamed the Start class to StartTile to make it clear what the class was for. Classes should be named so that their purpose is apparent.

The relations between classes differ greatly between the two designs. The absence of certain relationships in my design is due to the fundamental difference between the two designs. In my first design, I aimed to keep things simple, which meant that there were fewer relationships between classes. For example, the new design uses linked lists to cycle between tiles when moving a player, which is not necessary in the first design. A board class would be useful in my first design to keep track of what tile has what positional value so that the game class does not have to contain that information.

In my first design, I used dependency/association instead of ID-connections. My first design had too much code running inside the player and game classes. Introducing a Board class would have alleviated some of that work from the game class, creating a more balanced program. The Die class could have been improved to check if the dice roll resulted in twins and prompt for a re-roll.

The object diagram differs from my class diagram because of the lack of linked lists. Consequently, the Game class has more central control, as it stores the dice value instead of the dice themselves, as shown in my new diagram. The sequence diagram highlights the simplicity of my initial design, with fewer function calls. Most of the calls come from the Game class instead of the HumanPlayer class. The significant difference lies in the different ways of moving the player.

Overall, I believe that designing first gives you a better understanding of the overarching structure of the program, resulting in a faster process in the end. Although I prefer my initial idea of using modulus instead of linked lists and still prefer that design, the new one is a lot more robust and opens up for future modifications by using more classes and distributing the work more evenly.


## Initial design:

### Class:
![Class](../img/Class%20diagram.png)

### Object:
![Object](../img/Object%20diagram.png)

### Sequence:
![Sequence](../img/Sequence%20diagram.png)