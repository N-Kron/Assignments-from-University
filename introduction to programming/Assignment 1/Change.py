price = float(input("Price: "))
payment = int(input("Payment"))
change = payment - price

round(change)
change = int(change)

tusenlappar = change // 1000
change -= (tusenlappar * 1000)

femhundralappar = change // 500
change -= (femhundralappar * 500)

hundralappar = change // 100
change -= (hundralappar * 100)

femtiolappar = change // 50
change -= (femtiolappar * 50)

tjugolappar = change // 20
change -= (tjugolappar * 20)

tiokronor = change // 10
change -= (tiokronor * 10)

femkronor = change // 5
change -= (femkronor * 5)

tvakronor = (change // 2)
change -= (tvakronor * 2)

enkronor = (change // 1)

print("Tusenlapapar: " + str(tusenlappar))
print("femhundralappar: " + str(femhundralappar))
print("hundralappar: " + str(hundralappar))
print("femtiolappar: " + str(femtiolappar))
print("tjugolappar: " + str(tjugolappar))
print("tiokronor: " + str(tiokronor))
print("femkronor: " + str(femkronor))
print("tvåkronor: " + str(tvakronor))
print("enkronor: " + str(enkronor))

