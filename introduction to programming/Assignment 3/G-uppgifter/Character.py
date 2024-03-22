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
        result = self.name + ' is a(n) ' + self.kind + ' from ' + self.planet
        return result
