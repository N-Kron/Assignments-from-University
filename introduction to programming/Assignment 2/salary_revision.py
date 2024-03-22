def round_correct(value):
    return int(value + 0.5)


total_salary = 0
salary_list = []
amount_of_salary = 0
print("-Enter one salary at a time-")
print("then write \"calc\" to calculate inputted salary's")
while True:
    salary = input("Input salary: ")
    if salary == "calc":
        break
    salary_list.append(int(salary))
    amount_of_salary += 1

salary_list = sorted(salary_list)
print(f"Salaries: {salary_list}")

if amount_of_salary % 2 == 0:
    median1 = salary_list[int(amount_of_salary / 2)]
    median2 = salary_list[int((amount_of_salary / 2) - 1)]
    median = (median1 + median2) / 2

else:
    median = salary_list[int(amount_of_salary / 2)]

for salary in salary_list:
    total_salary += salary

print(f"Median: {int(round_correct(median))}")
print(f"Average: {int(round_correct(total_salary / amount_of_salary))}")
print(f"Gap: {salary_list[-1] - salary_list[0]}")
