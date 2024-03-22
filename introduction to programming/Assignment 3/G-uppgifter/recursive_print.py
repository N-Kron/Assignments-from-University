import os


def print_sub(current_dir):
    for file in current_dir:
        if file.is_dir():
            print(file.name)
            os.chdir(file)
            print_sub(os.scandir(os.getcwd()))

        else:
            print('-', file.name)


print_sub(os.scandir(os.getcwd()))
