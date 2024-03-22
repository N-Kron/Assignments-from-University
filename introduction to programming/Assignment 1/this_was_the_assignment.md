1DV501/1DT901, Introduction to Programming, Autumn 2022

# Assignment 1 -- Getting started

_Please note_
* Please contact our Python Help Desk if you run into problems when trying to install Python or VS Code
* Do not hesitate to ask your supervisor at the tutoring sessions (or Jonas at the lectures) if you have any problems with the Lecture 2 and 3 exercises. You can also post a question in the Slack channel
* Tasks are graded **Pass** or **Pass with distinction** (or Swedish _G_ or _VG_)
* **Pass** (or _G_) tasks are mandatory
* **Pass with distinction** (or _VG_) are only needed if you aspire for A or B grades. These tasks need to be handed in via Moodle
* First year campus students belonging to a study program on campus present their solutions to **Pass** (_G_) exercises during the tutoring sessions

## Install Software (Lecture 1)

The first task below is only to get you started and you do not need to show it as it only guides you through how to install and test out Python. You show that this works by showing the other tasks in Assignment 1.

1. Install Python
Install Python via Anaconda Distribution [https://www.anaconda.com/products/distribution]. Verify that it works by opening a Console (Windows) or Terminal (mac) window and type python. Example from Jonas' Mac:
```
jlnmsi % python
Python 3.8.3 (default, Jul  2 2020, 11:26:31) 
[Clang 10.0.0 ] :: Anaconda, Inc. on darwin
Type "help", "copyright", "credits" or "license" for more information.
>>> exit()  # terminate Python
```

Here is an Anaconda installation video from Youtube [https://www.youtube.com/watch?v=T8wK5loXkXg] for Windows user. Make sure to use the option "Add Anaconda to my PATH environmental variable" during the installation process.

2. Install Visual Studio Code and extensions
    1. Download and install Visual Studio Code (VSC) [https://code.visualstudio.com/]
    2. Download and install Python Extension for VSC [https://marketplace.visualstudio.com/items?itemName=ms-python.python]

3. Set up Visual Studio Code
Before you start programming, do the following.
    * Create a folder with the name python_courses in some location in your home directory.
    * Create a folder named 1DV501 (or 1DT901) inside python_courses.
    * Use Visual Studio Code to open the folder 1DV501
    * Use Visual Studio Code to create a new folder named YourLnuUserName_assign1 inside folder 1DV501. For example, it might look something like wo222ab_assign1.
    * Save all program files from the exercises in this assignment inside the folder YourLnuUserName_assign1.
    * In the future: create a new folder (YourLnuUserName_assignX) for each assignment and a new folder (with the course code as name) for each new course using Python.

4. Create, edit and execute
Use Visual Studio Code to create a file called hello.py inside your Assignment 1 folder and type
```python
# The classical "Hello World!" program. 
print("Hello World!")
```

Execute program by right-clicking on the program and chose: "Run Python File in Terminal". The result, if everything works, is that **Hello World!** is printed on your terminal output.

**Notice:** It is essential that you have a working Python and Visual Studio Code installation. Make sure that it works and post a question in Slack (or visit the Python Help Desk) if it doesn't.

## Tasks
The following tasks are marked as _G_ for **Pass** or _VG_ for **Pass with distinction**. As per the above instructions, the _G_ tasks must be done and shown at the deadline to the teachers in the lab session. There are eleven tasks marked as _G_ and you need to do all of them. Tasks marked with _VG_ need to be sent in via Moodle and will be checked later. There are four tasks marked as _VG_ and to get an A you need to do all of them. Notice that you will not automatically get an A or B if you do the _VG_ tasks, if they are poorly done you may recieve a grade below B.

### Input/Output, Operations on Primitive Types (Lecture 2)

1. Printing (G)

Write a program **print.py** that prints the phrase Knowledge is power!
on one line, on three lines, one word on each line,
inside a rectangle made up by the characters = and |.
Executing the program should give the following output:
```
Knowledge is power!

Knowledge
   is
  Power!

=========================
|                       |
|  Knowledge is power!  |
|                       |
=========================
```

2. Quote (G)

Write a program **quote.py** which reads a line of text from the keyboard and then prints the same line as a quote (that is inside " "). An example of an execution:
```
Write a line of text:  May the Force be with you!
Quote: "May the Force be with you!"
```

3. Fahrenheit (G)

Write a program **fahrenheit.py** that reads a The Fahrenheit temperature F (a float) from the keyboard and then print the corresponding Celsius temperature C. The relationship between C and F is:
```
F = (9/5)*C + 32
```
An example of an execution:
```
Provide a temperature in Fahrenheit: 100
Corresponding temperature in Celsius is 37.77778
```

4. Interest (G)

Write a program **interest.py** which computes the value of your savings S after Y years given a certain interest rate P (percentage). You can assume that S, Y and P are integers. The value should be an integer correctly rounded off. Input will given by the user via the keyboard. An example of an execution:
```
Initial savings: 1000
Interest rate (in percentages): 9
Number of years: 5

The value of your savings after 5 years is: 1539
```

5. Volume (G)

Create a program **volume.py** that asks for the radius of a sphere and prints the volume. The result should be presented with a single decimal correctly rounded off. An example of an execution:
```
Provide a radius: 6
The volume is 904.8
```

6. Time (G)

Write a program **time.py**, which reads a number of seconds (an integer) and then prints the same amount of time given in hours, minutes and seconds. An example of an execution:
```
    Give a number of seconds: 9999
    This corresponds to: 2 hours, 46 minutes and 39 seconds.
```
Hint: Use integer division and the modulus (remainder) operator.



7. Sum of Three (VG)

Write a program **sumofthree.py** which asks the user to provide a three digit number. The program should then compute the sum of the three digits. **Note:** you may _not_ convert the input from an integer to a string or a list (or anything else), you need to _calculate_ the answer. For example:
```
Provide a three digit number: 456
Sum of digits: 15
```

8. Change (VG)

Write a program **change.py** that computes the change a customer should receive when she/he has paid a certain sum. The program should exactly describe the minimum number of Swedish bills and coins that should be returned rounded off to nearest krona (kr). Example:
```
Price: 372.38
Payment: 1000

Change: 628 kr
1000kr bills: 0
 500kr bills: 1
 200kr bills: 0
 100kr bills: 1
  50kr bills: 0
  20kr bills: 1
  10kr coins: 0
   5kr coins: 1
   2kr coins: 1
   1kr coins: 1
```

### If statements and extra material (Lecture 3)

9. Positive, Zero, or Negative (G)

Write a program **positive.py** which reads an integer and then classifies (and prints) it as positive, zero, or negative. For example
```
Please provide an integer: -27
-27 is negative
```

10. Largest (G)

Write a program **largest.py** which reads three integers A, B, C and then prints the largest number. You should solve this problem using if statements. You are not allowed to use any of the max and sort functions that comes with Python. For example
```
Please provide three integers A, B, C.
Enter A: 23
Enter B: 46
Enter C: -11

The largest number is: 46
```

11. Classify Numbers (G)

Write a program **oddpositive.py** which generates a random number in the interval [-10,10] and classifies it as odd/even and as positive/negative. Use the function random.randint in the random module. No reading from the keyboard in this exercise. Two examples
```
The generated number is -7
-7 is odd and negative

The generated number is 0
0 is even and neither positive nor negative
```

12. Short Name (G)

Write a program **shortname.py**, reading a first name, a middle name and a last name (given name and family name) as strings. The output should consist of the first letter of the first name followed by a dot and a space, first letter of the middle name followed by a dot and a space and then followed by the first four letters of the last name. An example of an execution:
```
First name: Jabba  
Middle name: Desilijic
Last name: Tiure
Short name: J. D. Tiur
```
Also known as Jabba the Hutt... 😁

13. Random Number (G)

Write a program called **randomsum.py** generating and printing the sum of five random numbers in the interval [1,100]. For example

```
    Five random numbers: 78 13 91 2 36
    
    The sum is 220
```

14. Taxes (VG)

In a (very) simplified version of the Swedish income tax system we have three tax levels depending on your monthly salary:

* You pay a 30% tax on all income below 38.000 SEK/month
* You pay an additional 5% tax on all income in the interval 38.000 SEK/month to 50.000 SEK/month
* You pay an additional 5% tax on all income above 50.000 SEK/month

Write a program **tax.py** which reads a (positive) monthly income from the keyboard and then prints the corresponding income tax. For example
```
Please provide monthly income: 32000
Corresponding income tax:  9600

Please provide monthly income: 46000
Corresponding income tax:  14200

Please provide monthly income: 79000
Corresponding income tax:  27200
```




15. Chess Square Color (VG)

<img src="chessboard.gif" width="300"/>

Each square on a chess board in identified by a letter (a-h) and an integer (1-8). They are typically refered to as c3 or f5. Write a program **squarecolor.py** that reads a square identifier (e.g. e5) from the keyboard and prints the color (Black or White). Example execution:
```
    Enter a chess square identifier (e.g. e5): c6
    
    c6 is White
```
