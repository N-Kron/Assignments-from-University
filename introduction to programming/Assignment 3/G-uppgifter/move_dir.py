import os


def list_directories(path):
    directories = []
    for file in os.listdir(path):
        if os.path.isdir(file):
            directories.append(file)
    return directories


def list_files(path):
    files = []
    for file in os.listdir(path):
        if os.path.isfile(file):
            files.append(file)
    return files


program_running = True
while program_running:
    cwd = os.getcwd()
    print("--------------------")
    print("1. List directories")
    print("2. Change directory")
    print("3. List files")
    print("4. Quit")
    print("--------------------")

    u_input = int(input("What do you want to do: "))
    print("--------------------")

    if u_input == 1:
        print("Dir:\n")
        for item in list_directories(cwd):
            print(item)

    elif u_input == 2:
        os.chdir(input("Change to: "))

    elif u_input == 3:
        print("Files:\n")
        for item in list_files(cwd):
            print(item)

    elif u_input == 4:
        program_running = False
