cash = int(input("How much money do you earn: "))

if 38000 < cash < 50000:
    tax = 11400 + ((cash - 38000) * 0.35)

elif cash > 50000:
    tax = 11400 + 4200 + ((cash - 50000) * 0.4)

else:
    tax = cash * 0.3

print(int(tax))
