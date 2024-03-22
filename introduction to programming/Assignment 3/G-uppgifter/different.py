import random


def different(lst):
    dif_list = list(set(lst))
    dif_list.sort()
    return dif_list


listing = [random.randint(1, 200) for num in range(100)]
print("different int:")
print(different(listing))
