import os

# changed pretty much all of it...
# it now goes through the lines instead of the letters
# and now uses the replace function to remove symbols
# as I still want numbers to be included but only inside words
# a for-loop is used to look if a digit stands alone


def get_words(p, file_name):
    script = p + '\\' + file_name
    clean_word_list = []
    with open(script, encoding="utf8") as file:
        for line in file:
            line = line.replace('”', '')
            line = line.replace('\n', '')
            line = line.replace('\t', '')
            line = line.replace('!', '')
            line = line.replace('.', '')
            line = line.replace(',', '')
            line = line.replace(':', '')
            line = line.replace(';', '')
            line = line.replace('?', '')
            line = line.replace('(', '')
            line = line.replace(')', '')
            line = line.replace('%', '')
            line = line.replace('/', '')
            line = line.replace('–', '')
            line = line.replace('•', '')
            for word in line.split(' '):
                if not word.isdigit():
                    clean_word_list.append(word)
    return clean_word_list


def save_words(out_file, w):
    file = path + '\\' + out_file
    with open(file, 'w', encoding="utf8") as file:
        for word in w:
            file.write(word)
            file.write('\n')


# Main program
path = os.getcwd()
input_file = 'swe_news.txt'

word_list = get_words(path, input_file)

output_file = f'.swe_news_{len(word_list)}_words.txt'

save_words(output_file, word_list)
print('Saved', len(word_list), 'words in the file', path + output_file)
