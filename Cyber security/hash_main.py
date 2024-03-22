import HashSet
import random

# Program starts

# Initialize word set
words = HashSet.HashSet()
words.init()

alphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z", "[", '\\', "]", "^", "_", "`", "a", "b", "c", "d",
            "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
            "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"]

word = ['@', '@', '@', '@', '@', '@', '@', '@', '@', '@']
u_input = input("Enter \"1\" if you want to test uniformity. Enter \"2\" to test small changes\n-:")

if u_input == '1':
    for i in range(30000):
        word = ''
        for z in range(random.randint(1, 16)):
            word += alphabet[random.randint(0, 25)]
        words.add(word)


else:
    for q in range(0, 58):
        for i in range(0, 10):
            word[i] = alphabet[q]
            words.add(''.join(str(t) for t in word))

    word = ['@', '@', '@', '@', '@', '@', '@', '@', '@', '@']
    for q in range(0, 58):
        for i in range(10):
            word[9 - i] = alphabet[q]
            words.add(''.join(str(t) for t in word))

# Hash data test
mx = words.max_bucket_size()
print("\nmax bucket:", mx)
print("Optimal max bucket size = ", round(words.get_size() / 256, 1))

print("zero bucket ratio:", round(words.zero_bucket_ratio(), 2))

print("Number of elements: ", words.get_size())
