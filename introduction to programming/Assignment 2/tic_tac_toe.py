def print_bord():
    for x in numbers:
        print(x, end=' ')
    print()
    for x in row1:
        print(x, end=' ')
    print()
    for x in row2:
        print(x, end=' ')
    print()
    for x in row3:
        print(x, end=' ')
    print("\n")


def winning_condition_x():
    if row1.count('X') == 3 or row2.count('X') == 3 or row3.count('X') == 3:
        print("CONGRATS X")
        quit()

    elif row1[1] + row2[2] + row3[3] == "XXX":
        print("CONGRATS X")
        quit()

    elif row1[3] + row2[2] + row3[1] == "XXX":
        print("CONGRATS X")
        quit()

    elif row1[1] + row2[1] + row3[1] == "XXX":
        print("CONGRATS X")
        quit()

    elif row1[2] + row2[2] + row3[2] == "XXX":
        print("CONGRATS X")
        quit()

    elif row1[3] + row2[3] + row3[3] == "XXX":
        print("CONGRATS X")
        quit()


def winning_condition_o():
    if row1.count('O') == 3 or row2.count('O') == 3 or row3.count('O') == 3:
        print("CONGRATS O")
        quit()

    elif row1[1] + row2[2] + row3[3] == "OOO":
        print("CONGRATS O")
        quit()

    elif row1[3] + row2[2] + row3[1] == "OOO":
        print("CONGRATS O")
        quit()

    elif row1[1] + row2[1] + row3[1] == "OOO":
        print("CONGRATS O")
        quit()

    elif row1[2] + row2[2] + row3[2] == "OOO":
        print("CONGRATS O")
        quit()

    elif row1[3] + row2[3] + row3[3] == "OOO":
        print("CONGRATS O")
        quit()


def player_x():
    player_row = int(input("X what row do you want to play: "))
    player_column = int(input("X what column do you want to play: "))
    if 3 >= player_column >= 1 and 3 >= player_row >= 1:

        if big_list[player_row][player_column] == '-':
            big_list[player_row][player_column] = 'X'

        else:
            print("You stupid animal, there is no such space")
            player_x()

    else:
        print("The only winning move is actually to play")
        player_x()


def player_o():
    player_row = int(input("O what row do you want to play: "))
    player_column = int(input("O what column do you want to play: "))
    if 3 >= player_column >= 1 and 3 >= player_row >= 1:

        if big_list[player_row][player_column] == '-':
            big_list[player_row][player_column] = 'O'

        else:
            print("You stupid animal, that space is taken")
            player_o()

    else:
        print("The only winning move is actually to play")
        player_o()


numbers = [' ', 1, 2, 3]
row1 = ['1', '-', '-', '-']
row2 = ['2', '-', '-', '-']
row3 = ['3', '-', '-', '-']
big_list = [numbers, row1, row2, row3]

for turn in range(1, 6):
    print_bord()
    winning_condition_o()
    player_x()
    print_bord()
    winning_condition_x()
    if turn == 5:
        break
    player_o()

print("It's a draw")
