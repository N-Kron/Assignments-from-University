numbers = int(input("Write your numbers: "))
tens_and_singles = numbers % 100

singles = tens_and_singles % 10

tens = tens_and_singles - singles

hundreds = numbers - tens_and_singles

first_number = hundreds/100
second_number = tens/10
last_number = singles

sum = first_number + second_number + last_number
print("The sum of your numbers:")
print(int(sum))
input()
