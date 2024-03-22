# Assigment 4


## Size limits of heavenly bodies
The limits are imported from the last assignment with added relative size checks. The limits are as follows:

### Star
Star radius has to be between 16,700 and 7,000,000km.

### Planet
Planet radius has to be between 2,000 and 200,000km with a orbit radius bigger than 18,000km and twice the radius of the star.

### Moon
Moon radius has to be between 6 to 10,000km with a orbit bigger than 60 and twice the size of the planet.


## Limetations of program
The program has several limitations that need to be addressed.
Firstly, the program does not account for the radius of planets in its calculations, which can be relevant when it comes to close orbits. This means that there can be planets orbiting closer than twice the radius of the sun, although this is unlikely.
Secondly, the program does not store big stars that could have formed in the earlier universe. As a result, the program is limited to only living stars.
Thirdly, while the current approach to the program may be more effective, there is a lack of another class that could improve its functionality. For example, a Universe class might have been a useful addition to replace the use of the star array. However, the decision to not include this was a conscious one made during development.
Fourthly, the program is unable to store bodies with "-" in their name due to the nature of the storage system. As a result, the program automatically removes the "-" from the name, which may be seen as a drawback.
Finally, there is potential for improvement in the program's menu. The code could be streamlined using methods to avoid repetition and enhance efficiency.

## ER diagram:
![Alt text](Pictures/ER%20diagram.jpg)