import os


def writing(path, name, content):
    print(os.getcwd())
    place = path + '\\' + name
    file = open(place, "w")
    file.write(content)
    file.close()


name_of_file = input("What name should the file have: ")

string = ""
print("Write \'stop\' to stop writing")
while True:
    u_input = input("Write:")
    if u_input == 'stop':
        break
    string += u_input + '\n'
writing(os.getcwd(), name_of_file, string)
print("Done!")

