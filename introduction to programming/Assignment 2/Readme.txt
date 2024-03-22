abcd.py
This code will first loop through all possible combinations of abcd same way you would
crack a codelock (0001, 0002, 0003...). then it checks if that combination will equal 
itself backwards times 4 using a list.


birthday_candles.py
This program checks if candles are needed to be bought. If candles are needed to be bought
it will add the amount of candles inside the box to the stored candles and remove the needed amout for birthdays.


countdigits.py
This program will get the numbers from the user and pyut it inside a list. It will then
use the list to find the class of numbers inside and store the amount found to then be printed.


pi_approx.py
This program will generate random numbers for x and y to plot random points within the domain of the square.
The coordinaets are then used to calculate the relation of the area between the square and the cirkle.
Because we know the area of the square the relation can be used to calculate the area of the cirkle wich is pi.


salary_revision.py
a while loop is used to get the unknown amount of salaries. When the keyword calc is written in the program will calculate
the desired stats. To round of the awnsers a function is used so that rounding ties (0.5) are broken in favor of the higher number.


tic_tac_toe.py
functions are created to break up the code into readeble chunks and a for loop is uesd for speed instead of a while loop.
But because the amount of rounds is uneaven for a draw to happen a if statment is needed to end the program after player x
did their last turn. winning condition is called after a play and brute forces the entire board for a win.
could probabbly only check the possible rows to get a win depending on where the player played but because the playing feald is so small.
A print board function is also used to print the playing field.