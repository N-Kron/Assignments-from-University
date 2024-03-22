import os
from dataclasses import dataclass


@dataclass
class Character:
    name: str = ''
    kind: str = ''
    planet: str = ''

    def set_name(self, n):
        self.name = n

    def set_kind(self, k):
        self.kind = k

    def set_planet(self, p):
        self.planet = p

    def to_string(self):
        lenth1 = 20 - len(self.name)
        lenth2 = 20 - len(self.kind)
        result = self.name + lenth1*' ' + self.kind + lenth2*' ' + self.planet
        return result


def get_names(path):
    with open(path) as file:
        for line in file:
            stripped_line = line.strip()
            character_list = stripped_line.split(',')

            name = Character()
            name.set_name(character_list[0])
            name.set_kind(character_list[1])
            name.set_planet(character_list[2])

            print(name.to_string())


get_names(os.getcwd() + '\\' + 'starwars.txt')
