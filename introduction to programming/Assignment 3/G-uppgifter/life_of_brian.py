import os


def get_words(p, file_name):
    script = p + '\\' + file_name
    clean_script = ""
    special_characters = "!@#$%\"^&*()[]:\\+?_=,.<>/"
    with open(script) as script:
        for line in script:
            line = line.strip() + ' '
            for letter in line:
                if not any(c in special_characters for c in letter):
                    clean_script += letter.lower()
    clean_script = clean_script.split(' ')
    return clean_script


def save_words(p, out_file, w):
    file = p + '\\' + out_file
    with open(file, "w") as file:
        for word in w:
            file.write(word)
            file.write('\n')


# Main program
path = os.getcwd()
input_file = 'life_of_brian.txt'

word_list = get_words(path, input_file)

output_file = f'brian_{len(word_list)}_words.txt'

save_words(path, output_file, word_list)
print('Saved', len(word_list), 'words in the file', path + output_file)
