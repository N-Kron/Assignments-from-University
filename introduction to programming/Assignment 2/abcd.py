lst = []
for a in range(1, 10):
    lst.append(a)

    for b in range(0, 10):
        lst.append(b)

        for c in range(0, 10):
            lst.append(c)

            for d in range(1, 10):
                lst.append(d)

                num = (a * 1000) + (b * 100) + (c * 10) + d

                r_lst = lst[::-1]
                r_num = (r_lst[0] * 1000) + (r_lst[1] * 100) + (r_lst[2] * 10) + r_lst[3]

                if num == r_num * 4:
                    print(f"The number is: {num}")
                    quit()

                print(lst)
                lst.pop(3)
            lst.pop(2)
        lst.pop(1)
    lst.pop(0)
