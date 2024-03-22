import random


def count_occurrences(lst):
    nums = {1: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}
    for number in lst:
        nums.update({number: nums[number] + 1})
    return nums


listing = [random.randint(1, 10) for num in range(100)]
listing.sort()
print(f"List: {listing}\nResult:")
dictionary_items = count_occurrences(listing).items()
for item in dictionary_items:
    print(item)
