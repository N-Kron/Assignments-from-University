1DV501/1DT901, Introduction to Programming, Autumn 2022

# Old version - Only valid autumn 2022!!!

# Assignment 2 -- Iterations

_Please note_
* Do not hesitate to ask your supervisor at the tutoring sessions (or Jonas at the lectures). You can also post a question in the Slack channel
* Tasks are graded **Pass** or **Pass with distinction** (or Swedish _G_ or _VG_)
* **Pass** (or _G_) tasks are mandatory
* **Pass with distinction** (or _VG_) are only needed if you aspire for A or B grades. These tasks need to be handed in via Moodle
* First year campus students belonging to a study program on campus present their solutions to **Pass** (_G_) exercises during the tutoring sessions
* It is important that output of the assignment looks just like shown here (except, of course for random values) -- if there is text asking a user for input, that text must be shown

## Tasks
The following tasks are marked as G for Pass or VG for Pass with distinction. As per the above instructions, the G tasks must be done and shown at the deadline to the teachers in the lab session. There are eleven tasks marked as G and you need to do all of them. Tasks marked with VG need to be sent in via Moodle and will be checked later.

> We expect all students to turn on the Flake8 linter that comes with VS Code. A linter is a tool that automatically checks your Python code for programmatic and stylistic errors. Instructions for how to turn on and use the Flake8 linter are given at the end of Lecture 4, and a [video](https://mymoodle.lnu.se/mod/kalvidres/view.php?id=3347491) for how to get started is a part of the Lecture 4 material (in Moodle). **We expect each student to clean up their code such that the Flake8 linter gives no warnings or errors.**

### Iterations

1. Sum even 100 (G)

This program should sum all even numbers up to 100, excluding 0 but including 100. No input is required. Call the program **all_even.py**.

```
Sum of the 100 first numbers is: 2550
```

2. Change to for (G)

Write a program that you call **change_to_for.py** where you convert the following program that is using a *while* iteration into a program that uses *for* instead. The output must be the same for both programs.
```python
count = 0
sum = 0

while count < 100:
    sum = sum + count
    count = count + 1

print(sum)
```

3. Largest K (G)

Write a program **largest_k.py** which for any given positive integer *n* (read from the keyboard) computes the largest integer k such that 0 + 2 + 4 + 6 + 8 + ... + k < n. Notice: The program should be terminated with a suitable error message if a non-positive n is provided. An example of an execution:

```
Enter a positive integer: 25
8 is the largest k such that 0+2+4+6+...+k < 25
```

4. Print primes (G)

Create a program called **print_primes.py** that asks for a number of prime numbers to print. The program should then print them ten at a line and then continue on the next line. You can read more about prime numbers on [Wikipedia](https://en.wikipedia.org/wiki/Prime_number)

```
How many primes? 50
2 3 5 7 11 13 17 19 23 29
31 37 41 43 47 53 59 61 67 71
73 79 83 89 97 101 103 107 109 113
127 131 137 139 149 151 157 163 167 173
179 181 191 193 197 199 211 223 227 229
```

5. Rolling the die (G)

They say that if you roll a die enough times, you should get about the same number of ones, twos, threes and so on. But is that true? Create a program called **roll_the_die.py** which rolls a die and stores how many times it comes up at one, two, three and so on. It will do this many times, more on this a bit further down. You will notice that for few rolls, there is quite a difference between how many times the different faces will show up. Calculate the difference between the face that turns up the most times and the face that shows up the least times. This should then be divided with the number of the face that was shown the most to give you a sort of ratio (in percent) of the difference. This should then be done 20 times with increasing number of times to roll the die, from 10 to 20 to 40 and so on by doubling the number of rolls each time. Present it like shown below:

```
For 10 rolls, the difference is 100.0%
For 20 rolls, the difference is 90.0%
For 40 rolls, the difference is 53.33%
For 80 rolls, the difference is 32.14%
For 160 rolls, the difference is 11.32%
For 320 rolls, the difference is 22.81%
For 640 rolls, the difference is 24.46%
For 1280 rolls, the difference is 8.54%
For 2560 rolls, the difference is 10.14%
For 5120 rolls, the difference is 10.72%
For 10240 rolls, the difference is 5.38%
For 20480 rolls, the difference is 4.69%
For 40960 rolls, the difference is 2.86%
For 81920 rolls, the difference is 1.69%
For 163840 rolls, the difference is 0.76%
For 327680 rolls, the difference is 0.42%
For 655360 rolls, the difference is 0.22%
For 1310720 rolls, the difference is 0.39%
For 2621440 rolls, the difference is 0.41%
For 5242880 rolls, the difference is 0.28%
```

6. Random numbers (G)

Write a program **random_numbers.py** that reads a positive integer **n** from the keyboard and then:
- Generates and prints (in a single line) *n* random numbers in the interval [1,100]
- Prints the average value (with two decimals), the smallest number (min), and the largest number (max).
A suitable error message should be presented if the input number *n* is non-positive. You should _**not**_ use a list (or any other data structure) to first store the generated numbers before you compute average, min, and max.

An example of an execution:
```
Enter number of integers to be generated: 10

Generated values: 77 15 13 54 96 73 100 12 98 28 
Average, min, and max are 56.6, 12, and 100
```

7. Counting Digits (VG)
Write a program **countdigits.py**, which for any given positive number _n_ (read from the keyboard) prints the number of zeros, odd digits, and even digits of the integer. In this case we consider zeros to be neither odd nor even. An example of an execution:
```
Enter a large positive integer: 6789500

Zeros: 2
Odd: 3
Even: 2
```

8. Birthday (VG)

Write a program **birthday_candles.py** that computes how many boxes of candles a person needs to buy each year for her\his birthday cake. You can assume that the person reaches an age of 100, the number of candles used each year is the same as the age, that you save non-used candles from one year to another, and that each box contains 24 candles. Also, at the end, we want you to print the total number of boxes one has to buy, and how many candles that are available after having celebrated the 100th birthday. An example of an execution:

```
Before birthday 1, buy 1 box(es)
Before birthday 7, buy 1 box(es)
Before birthday 10, buy 1 box(es)
Before birthday 12, buy 1 box(es)
Before birthday 14, buy 1 box(es)

...

Before birthday 95, buy 3 box(es)
Before birthday 96, buy 4 box(es)
Before birthday 97, buy 5 box(es)
Before birthday 98, buy 4 box(es)
Before birthday 99, buy 4 box(es)
Before birthday 100, buy 4 box(es)

Total number of boxes: 211, Remaining candles: 14
```
**Notice:** In our example we only have a print-out of those birthdays where you must buy boxes. In the non-printed years (e.g. 2-6 and 8-9) you can handle the birthdays without having to buy any more candles.

### Functions

9. Sentence Builder (G)

A sentence can consist of a pronoun followed by a verb and a noun. If the sentence is in future tense, a sentence could be "I will paint a house", where "I" is the pronoun, "will paint" is the verb and "a house" is the noun. Create a program called **sentence_builder.py** that has three functions called **pronoun()**, **verb()** and **noun()** respectively. Each function should randomly pick one of five of each kind (meaning that the function **pronoun()** should select one of "I", "You", "It", "We", "They"). _After_ the function definitions you should have an iteration which creates ten random sentences by calling the three functions. An example of an execution:

```
It will see a house
They will eat a house
You will pull a car
I will see a house
I will touch a computer
You will eat a tree
You will touch a house
I will touch a car
I will see a tree
```

10. Math Functions (G)

Create a program called **simple_math.py** in which you have eight functions:
```python
def inc(n)           #  Increments n with one
def inc_with(n, t)   #  Increments n with the value of t
def dec(n)           #  Decrements n with one
def dec_with(n, t)   #  Decrements n with the value of t
def greatest(n1, n2) #  Returns the largest of the values n1 and n2
def is_even(n)       #  Returns True if n is even, otherwise false
def power(x, n)      #  Returns x to the power of n
def factorial(n)     #  Returns the factorial of n
```

You may _not_ use the Math library in Python to implement the functions above, but you must give them an implementation. Below your implemented functions, you should have a number of calls to the functions. The calls can, but must not, look like below:

```python
print('41 plus 1:', inc(41))
print('30 plus 12:', inc_with(30, 12))

print('43 minus 1:', dec(43))
print('52 minus 10:', dec_with(52, 10))

print('Which is greater, 24 or 42?', greatest(24, 42))

print('Is 42 even?: ', is_even(42))

print('2 to the power of 16:', power(2, 16))

print('Factorial of 5:', factorial(5))
```

If you use the calls above, the output will be:

```
41 plus 1: 42
30 plus 12: 42
43 minus 1: 42
52 minus 10: 42
Which is greater, 24 or 42? 42
Is 42 even?:  True
2 to the power of 16: 65536
```

11. Distance (G)

Inside file **distance.py**, create a function **distance(x1,y1,x2,y2)** which computes the distance between two points _x1,y1_ and _x2,y2_ using the formula
```
distance = Sqrt( (x1-x2)^2 + (y1-y2)^2 )
```

Sqrt() means "the square root of" and ^ means "raised to".

Also, add program code so that a user can provide the point coordinates and get the distance. The answer should be presented with three decimal digits as in the example below:
```
Enter x1: 1
Enter y1: 1
Enter x2: 5
Enter y2: 6

The distance between (1.0,1.0) and (5.0,6.0) is 6.403
```

12. Types of Characters (G)

Create a program that you call **simple_strings.py** in which you have three functions as described below:

```python
def first_last(s)       #  Returns the first and last character in the string s
def char_types(s)       #  Returns the number of vowels and consonants in string s
def char_symbol_number  #  Returns the number of characters, symbols (including spaces) and numbers in string s
```

Also supply a number of calls to the methods below their definition. Below you can see an example exection and what strings that were used:

```
First and last in "May the Force be with you!": M !
In that sentence, the number of vowels is 10 and the number of consonants is 10
In the sentence "I am 1 with the Force, not 2..." the number of letters is 18, 
symbols is 11 and numbers is 2
```

13. ABCD (VG)

There are four different digits A, B, C, and D such that the number DCBA is equal to 4 times the number ABCD. What are the four digits? Note: to make ABCD and DCBA a proper four digit integer, neither A nor D can be zero. The name of the program computing A, B, C, and D should be named **abcd.py**.

**Hint:** Use a quadruple nested loop and a function get_number(a, b, c, d) that converts digits a, b, c, d into a four digit integer abcd.

14. Calculating Pi (VG)

![Unit square](unit-square.gif)

Assume a unit circle centred around origin inside a square with sides 2 like in the figure above. Assume also that we randomly generate N points (x,y) where both x and y are within the range [- 1,1]. The proportion of points inside the circle should then approximately be the same as the ratio between the circle area pi*R*R (which equals pi since R=1) and the square area 4. This relation can be used to compute an approximation of pi. Write a program pi_approx.py that computes (and prints) a pi approximation for N=100, N=10000, and N=1000000. Print also the error (i.e. the absolut value of pi_actual - pi_approx).

### Lists

15. Magic 8-ball (G)

A magic 8-ball will answer question you ask it in a random way (yes, they do not work for real...). Create a program called **eightball.py** that has a list of strings of possible answers. The program should ask the user to ask a question and it will randomly pick one of the strings in the list as an answer. The program will stop when the user enters the string **stop**, and it will stop at once not giving any more answers. See below for an example execution:

```
Ask the magic 8-ball your question: Will I win the lottery?
The magic 8-ball says: Ask again later
Ask the magic 8-ball your question: Okay, will I win the lottery?
The magic 8-ball says: As I see it, yes
Ask the magic 8-ball your question: Will I win a lot of money?
The magic 8-ball says: Concentrate and ask again
Ask the magic 8-ball your question: Okay... Will I get rich?
The magic 8-ball says: As I see it, yes
Ask the magic 8-ball your question: Great! Will I be a millionaire?
The magic 8-ball says: Better not tell you now
Ask the magic 8-ball your question: Why? Please tell me if I will...
The magic 8-ball says: Very doubtful
Ask the magic 8-ball your question: stop
```

16. List information (G)

Write a program called **list_info.py** that creates a list with 100 elements that are random values between 1 and 10000. The program should then print out the largest, smallest, average (rounded to two decimals) and second largest value in the list. In this task you may use built in functions like **max()** and so on. See below for an example execution:

```
Largest value in list: 9973
Smallest value in list: 22
Average value in list: 4677.61
Second largest value in list: 9941
```

17. Deck of Cards (G)

Normal playing cards consist of 52 cards with suits (Hearts, Spades, Clubs and Diamonds) and a value/rank (2 to 10 followed by Jack, Queen, King and Ace). Create a program called **deck.py** that creates such a deck (with all 52 cards) and then shuffles it _without_ using the built in **shuffle()**. The program should then print out the five top cards as "My hand". See below for an execution:
```
My hand:
3 of Hearts
10 of Clubs
6 of Spaces
5 of Spaces
Ace of Diamonds
```

18. Palidrome (G)

Inside file **palindrome.py**, create a function **is_palindrome(s)** that returns **True** only if the string _s_ is a palindrome. A string is a palindrome if it contains the same sequence of letters when read backwards. We make no difference between upper and lower case letters. Examples of palindromes are:
```
"Was it a rat I saw?"     "A nut for a jar of tuna."    "Madam"    "Ni talar bra latin!"   
```

Also, add program code demonstarting how the function can be used. **Hint:** Start by creating a new string with only lower case letters and where all non-letters have been removed. For example, start by converting "Was it a rat I saw?" to wasitaratisaw.

19. List Functions (G)

Create a program called **functions_for_lists.py** that contains the following for functions:

```python
def random_num_list(n)        #  Generates a list of n integers
def only_odd(lst)             #  Takes a list as input and returns a list with only the odd values
def square(lst)               #  Takes a list as input and creates a new list with all the values squared
def sublist(lst, start, stop) #  Returns a new list with only the values between start and stop in the list
```

Try to implement these functions as _one-liners_. The three first are all possible to solve using _list comprehension so try that. Failing that, you can implement them any way you like. Also supply example calls to the functions below their definitions. An example execution is shown below:

```
Here is the list: [48, 70, 8, 81, 42]
Odds in it are: [81]
Let's square each number: [2304, 4900, 64, 6561, 1764]
Only the three middle values: [70, 8, 81]
```

20. Shall we play a game? (VG)

In the 1983 movie "War Game" the computer is finally overturn by asking it to play a game of tic-tac-toe. In this task, create a program called **tic_tac_toe.py** that implements this game for two players taking turn in selecting where in a 3x3 grid to put their markers. In turn, ask each player which row and column they want to play. Make sure that the program checks if that row/column combination is empty. When a player has won, end the game. When the whole board is full and there is no winner, announce a draw.

As the program will be fairly large, you should divide it into suitable functions that do subset of the problem, but it up to you to decide what functions to use. See below for an example execution of the game:

```
  1 2 3
1 - - - 
2 - - - 
3 - - - 
Player X, which row do you play? 2
Player X, which column do you play? 2
  1 2 3
1 - - - 
2 - X - 
3 - - - 
Player O, which row do you play? 1
Player O, which column do you play? 1
  1 2 3
1 O - - 
2 - X - 
3 - - - 
Player X, which row do you play? 2
Player X, which column do you play? 3
  1 2 3
1 O - - 
2 - X X 
3 - - - 
Player O, which row do you play? 3
Player O, which column do you play? 3
  1 2 3
1 O - - 
2 - X X 
3 - - O 
Player X, which row do you play? 2
Player X, which column do you play? 1
  1 2 3
1 O - - 
2 X X X 
3 - - O 
Player X won!
```

21. Salary (VG)

When the union is reporting about the latest salary negotiations they are presenting the _average salary_, the _median salary_, and the _salary gap_ for the workers that they represent. Write a program **salary_revision.py** that reads an arbitrary number of salaries (integers) and then reports the median and average salaries, and the salary gap. All of them should be integers (correctly rounded off).
By salary gap we mean the difference between the highest and lowest saleries. The median salary is the middle salary (or average of the two middle salaries) when all saleries have been sorted. The easiest way to sort a list is to use the sort method in the list class. Notice that you are _not_ allowed to use external libraries that handle statistics for this task, the purpose of it is that you show that you can do the calculations yourself.

Two different executions might look like this:
```
Provide salaries: 21700 28200 26300 25100 22600 22800 19900 
Median: 22800
Average: 23800
Gap: 8300

Provide salaries: 22100 29800 27300 25400 23100 22300
Median: 24250
Average: 25000
Gap: 7700
```
**Notice**, how to read an arbitrary number of integers from the keyboard was shown in Lecture 6.
