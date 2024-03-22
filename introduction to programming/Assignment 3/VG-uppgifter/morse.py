letters = {'.-': 'a', '-...': 'b', '-.-.': 'c', '-..': 'd', '.': 'e', '..-.': 'f', '--.': 'g', '....': 'h', '..': 'i',
           '.---': 'j', '-.-': 'k', '.-..': 'l', '--': 'm', '-.': 'n', '---': 'o', '.--.': 'p', '--.-': 'q',
           '.-.': 'r', '...': 's', '-': 't', '..-': 'u', '...-': 'v', '.--': 'w', '-..-': 'x', '-.--': 'y', '--..': 'z',
           '.--.-': 'å', '.-.-': 'ä', '---.': 'ö'}


def translate_from_morse(text):
    morse_list = text.split(' ')
    filter_text = list(filter(None, morse_list))
    print(filter_text)
    result = ""
    if filter_text[0].isalpha():
        for let in filter_text:
            for key, value in letters.items():
                if let == value:
                    result += key + ' '
    else:
        for let in filter_text:
            for key, value in letters.items():
                if let == key:
                    result += value + ' '
    return result


u_input = input("Write message: ").lower()
u_input_with_space = ""
for letter in u_input:
    u_input_with_space += letter + ' '
print(u_input_with_space)
print(f"In morse code: {translate_from_morse(u_input_with_space)}")


u_input = input("\nWrite in morse here: ")
print(f"In plain text: {translate_from_morse(u_input)}")
