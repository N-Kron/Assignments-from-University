import os


def count_directories(dir_path):
    return dir_path.count('\\')


def count_files(dir_path):
    amount_of_files = 0
    for file in os.listdir(dir_path):
        amount_of_files += 1
    return amount_of_files


cwd = os.getcwd()
print(f"I am now at: {cwd}")
print(f"Bellow me I have {count_directories(cwd)} directories/folders")
print(f"This directory contains {count_files(cwd)} files")
