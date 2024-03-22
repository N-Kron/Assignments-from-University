number = input("What is your number")
num_list = [int(x) for x in number]

nr_of_zero = 0
nr_of_odd = 0
nr_of_even = 0
for digit in num_list:
    if digit == 0:
        nr_of_zero += 1

    elif digit % 2 == 0:
        nr_of_even += 1

    else:
        nr_of_odd += 1

print(f"Number of zero: {nr_of_zero}")
print(f"Number of odd: {nr_of_odd}")
print(f"Number fo even: {nr_of_even}")
