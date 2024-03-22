from dataclasses import dataclass
from typing import Any

# A head-and-tail implementation of a deque using data classes


# Each node is an instance of class Node
@dataclass
class Node:
    value: int = None
    nxt: Any = None  # Any since Node not properly defined at this point


@dataclass
class Deque:
    head: Node = None      # First node in queue
    tail: Node = None      # Last node in queue
    size: int = 0

    # Add element n as last entry in deque
    def add_last(self, n):
        new = Node(n, None)
        if self.tail is None:
            self.tail = new
            self.head = self.tail
        else:
            self.tail.nxt = new
            self.tail = self.tail.nxt
        self.size += 1

    # Returns a string representation of the current deque content
    def to_string(self):
        s = "{ "
        node = self.head
        while node is not None:
            s += str(node.value) + " "
            node = node.nxt
        s += "}"
        return s

    # Add element n as first entry in deque
    def add_first(self, n):
        new = Node(n, self.head)
        self.head = new
        self.size += 1

    # Returns (without removing) the last entry in the deque.
    # Gives error message and returns None when accessing empty deque.
    def get_last(self):
        if self.tail is None:
            print("ERROR! Empty list")
            return None
        else:
            return str(self.tail.value)

    # Returns (without removing) the first entry in the deque
    # Gives error message and returns None when accessing empty deque.
    def get_first(self):
        if self.tail is None:
            print("ERROR! Empty list")
            return None
        else:
            return str(self.head.value)

    # Removes and returns the first entry in the deque.
    # Gives error message and returns None when accessing empty deque.
    # The case size = 1 requires speciall handling
    def remove_first(self):
        if self.head is None:
            print("ERROR! Empty list")
            return None

        elif self.head is self.tail:
            self.head = None
            self.tail = self.head
            self.size -= 1
            return None

        elif self.head.nxt.nxt is None:
            self.tail = self.head
            self.size -= 1

        else:
            value = self.head.value
            self.head = self.head.nxt  # By-pass head node
            self.size -= 1
            return value

    # Removes and returns the last entry in the deque.
    # Gives error message and returns None when accessing empty deque.
    # The case size = 1 requires speciall handling
    def remove_last(self):
        if self.head is None:
            print("ERROR! Empty list")
            return None

        elif self.head is self.tail:
            self.head = None
            self.tail = self.head
            self.size -= 1
            return None

        elif self.head.nxt.nxt is None:
            self.tail = self.head
            self.size -= 1

        else:
            before = self.head
            now = before.nxt
            while now.nxt is not None:
                before = before.nxt  # Move to next node
                now = before.nxt
            value = before.nxt.value
            before.nxt = None
            self.tail = now
            self.size -= 1
            return value
