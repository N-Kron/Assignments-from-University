import random
from math import pi

inside = 0
outside = 0

for power in range(1, 4):
    inside, outside = 0, 0
    n = 100 ** power
    for z in range(n):
        x = random.uniform(-1, 1)
        y = random.uniform(-1, 1)
        if x ** 2 + y ** 2 <= 1:
            inside += 1

        else:
            outside += 1

    print(f"For {n} iterations")
    print(f"inside circle: {inside}")
    print(f"outside circle: {outside}")
    relation = inside / (outside + inside)
    print(f"Pi ≈ {4 * relation}")
    print(f"Error amount: {abs(pi - 4 * relation)}")
    print("----------------")
