import math


def sub_encrypt():
    encrypted = ''
    for letter in u_text:
        if letter.isalpha():
            ind = alphabet.index(letter)
            ind = (ind + int(u_key)) % 25
            encrypted += alphabet[ind]
        else:
            encrypted += letter
    return encrypted


def sub_decrypt():
    encrypted = ''
    for letter in u_text:
        if letter.isalpha():
            ind = alphabet.index(letter)
            ind = (ind - int(u_key))
            if ind < 0:
                ind += 25
            else:
                ind = ind % 25
            encrypted += alphabet[ind]
        else:
            encrypted += letter
    return encrypted


def trans_encrypt():
    processed_text = ""
    word_list = []
    for letter in u_text:
        word_list.append(letter)

    count = 1
    turn = 1
    for i in range(len(word_list)):
        processed_text += word_list[count - 1]
        if count + int(u_key) > len(word_list):
            count = 1 + turn
            turn += 1
        else:
            count += int(u_key)
    return processed_text


def trans_decrypt():
    processed_text = ""
    word_list = []
    for letter in u_text:
        word_list.append(letter)
    distance = math.ceil(len(word_list) / int(u_key))
    dif = (distance * int(u_key)) - len(word_list)
    if dif > 0:
        for i in range(dif):
            word_list.insert((len(word_list)) - (i * distance), '')

    count = 0
    turn = 1
    for i in range(len(word_list)):
        processed_text += word_list[count]
        if count + distance >= len(word_list):
            count = turn
            turn += 1
        else:
            count += distance
    return processed_text


alphabet = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z']

# Main

print("Welcome to my cipher program!")
print("Answer the questions with 1 or 2\n")
print("1.Substitution\n2.Transposition")
u_input_method = input("What method do you want to use: ")
print("1.Encrypt\n2.Decrypt")
u_input_crypt = input("\nDo you want to encrypt or decrypt ")

u_text_file_name = input("Name of file to be processed in this folder (with file extension): ")
u_text = ''
with open(u_text_file_name, "r") as file:
    for row in file:
        u_text += row.lower()
u_key = input("Enter key to be used\n(Key is a number)\n-: ")

if u_input_method == '1':
    if u_input_crypt == '1':
        pros_message = sub_encrypt()
    else:
        pros_message = sub_decrypt()


else:
    if u_input_crypt == '1':
        pros_message = trans_encrypt()

    else:
        pros_message = trans_decrypt()

print(pros_message)
with open("processed_file.txt", 'w') as f:
    f.write(pros_message)

