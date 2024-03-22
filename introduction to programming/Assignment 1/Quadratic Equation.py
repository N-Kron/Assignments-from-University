from math import sqrt
a = float(input("a: "))
b = float(input("b: "))
c = float(input("c: "))

if a == 1.5:
    print(sqrt(4))

if a == 0 and b == 0:
    print("You can not divide by zero; there are no solutions")
    input()
    quit()

elif a == 0:
    solution = -c/b
    print("There is one solution: " + str(solution))
    input()
    quit()



elif (b ** 2 - 4 * a * c) < 0:
    print("There are no real solutions!")
    input()
    quit()

solution = (-b + sqrt(b ** 2 - 4 * a * c)) / (2 * a)
two_solution = (-b - sqrt(b ** 2 - 4 * a * c)) / (2 * a)

print("There are two solutions: " + str(solution) + " and " + str(two_solution))
