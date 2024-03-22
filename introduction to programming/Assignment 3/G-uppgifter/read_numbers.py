def file_reader(file):
    all_num_list = []
    with open(file) as f:
        for line in f:
            if file == file_a:
                line = line.split(',')
            else:
                line = line.split(':')
            for number in line:
                all_num_list.append(int(number))
        return all_num_list


def mean(lst):
    sum_of_all = 0
    for number in lst:
        sum_of_all += number
    return sum_of_all / 10_000


def std(lst, m):
    combined = 0
    for number in lst:
        combined += (number - m) ** 2
    return (combined / 10_000) ** 0.5


file_a = "C:\\Users\\noa\\PycharmProjects\\Assignment 3\\G-uppgifter\\file_10k_integers_A.txt"
file_b = "C:\\Users\\noa\\PycharmProjects\\Assignment 3\\G-uppgifter\\file_10k_integers_B.txt"

num_in_a = file_reader(file_a)
mean_of_a = mean(num_in_a)
print(f"Standard deviation of A: {std(num_in_a, mean_of_a)}")
print(f"Mean of A: {mean_of_a}\n")

num_in_b = file_reader(file_b)
mean_of_b = mean(num_in_b)
print(f"Standard deviation of B: {std(num_in_b, mean_of_b)}")
print(f"Mean of B: {mean_of_b}")
