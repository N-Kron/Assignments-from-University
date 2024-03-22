import os


depth = 0


def print_sub(current_dir):
    global depth
    for file in current_dir:
        if file.is_dir():
            print(depth * '  ', file.name)
            os.chdir(file)
            depth += 1
            print_sub(os.scandir(os.getcwd()))
            depth -= 1

        else:
            print(depth * '  ', file.name)


print_sub(os.scandir(os.getcwd()))
