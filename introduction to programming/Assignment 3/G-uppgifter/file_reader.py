import os


def reading(path, name):
    place = path + '\\' + name
    with open(place, "r") as file:
        for row in file:
            yield row


line_count = 0
program = reading(os.getcwd(), input("What is the name of the file: "))
for line in program:
    print(line, end='')
    line_count += 1
print(f"Line count: {line_count}")
