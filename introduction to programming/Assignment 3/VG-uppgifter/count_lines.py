import os


line_amount = 0


def print_sub(current_dir):
    for file in current_dir:
        if file.is_dir():
            os.chdir(file)
            print_sub(os.scandir(os.getcwd()))
            os.chdir('..')

        elif file.name.endswith(".py"):
            count_py_lines(file.name)


def count_py_lines(py_name):
    file = os.getcwd() + '\\' + py_name
    global line_amount
    with open(file, "r", encoding="utf8") as program:
        for line in program:
            if line != '\n':
                line_amount += 1


print_sub(os.scandir(os.getcwd()))
print(line_amount)
