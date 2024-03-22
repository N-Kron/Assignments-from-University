bought_boxes = 0
amount_of_candles = 0
for age in range(0, 101):
    boxes_to_buy = 0
    while amount_of_candles < age:
        amount_of_candles += 24
        bought_boxes += 1
        boxes_to_buy += 1
    amount_of_candles = amount_of_candles - age

    if boxes_to_buy > 0:
        print(f"before birthday {age} buy {boxes_to_buy} boxes")
print(f"Total number of boxes: {bought_boxes}")
print(f"Remaining candles: {amount_of_candles}")
