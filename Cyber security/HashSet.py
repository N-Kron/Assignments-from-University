from dataclasses import dataclass
from typing import List


@dataclass
class HashSet:
    buckets: List[List] = None
    size: int = 0

    def init(self):
        self.size = 0
        self.buckets = [[] for i in range(256)]

    # Calculates the hash value input
    def get_hash(self, word):
        ascii_number = 0
        for letter in word:
            ascii_number += ord(letter)
        return ascii_number

    # Adds a word to set if not already added
    def add(self, word):
        hash_num = self.get_hash(word) % 256
        self.buckets[hash_num].append(word)
        self.size += 1

    # Return total number of elements inside hashmap
    def get_size(self):
        return self.size

    # Returns the size of the bucket containing the most elements
    def max_bucket_size(self):
        biggest_buck = 0
        for bucket in self.buckets:
            if len(bucket) > biggest_buck:
                biggest_buck = len(bucket)
        return biggest_buck

    def zero_bucket_ratio(self):
        zero_buckets = 0
        for bucket in self.buckets:
            if len(bucket) == 0:
                zero_buckets += 1
        return zero_buckets / 256

