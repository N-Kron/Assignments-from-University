from mysql.connector import connect, Error
import hashlib
import datetime
import re

# Program created by Noa Kron


def search_books(search_type):
    browsing = True
    while browsing:
        u_search = input(f"Enter the {search_type}: ")
        count_query = f"SELECT COUNT(*) FROM books WHERE {search_type} = %s"
        selected_books_query = f"SELECT * FROM books WHERE {search_type} = %s"
        with connection.cursor() as cursor:
            # Execute query to count the number of books with the entered author or title
            cursor.execute(count_query, (u_search,))
            count = cursor.fetchone()[0]

            # Check if there are any books with the entered author or title
            if count == 0:
                print(f"No books found with {search_type} '{u_search}'.")
                print("Press n to try again")
                u_choice = input("Press ENTER to return to main menu")
                if u_choice == '':
                    browsing = False

            else:
                # Execute query to retrieve books with the entered author or title
                cursor.execute(selected_books_query, (u_search,))
                books = cursor.fetchall()

                for row in books:
                    print("\nAuthor: " + row[1])
                    print("Title: " + row[2])
                    print("ISBN: " + row[0])
                    print("Price: " + str(row[3]))
                    print("Subject: " + row[4])

                print("\n-Write the ISBN to add to cart")
                print("-Write b to continue browsing")
                print("-Press ENTER to return to menu")
                u_choice = input("-:")

                if u_choice == 'b':
                    print("continuing browsing...")

                elif u_choice == '':
                    browsing = False

                else:
                    qty = int(input("Enter the quantity of the book to add to the cart: "))
                    add_book_to_cart(user_id, u_choice, qty)
                    input("Press ENTER to return to main menu.")
                    browsing = False


def add_book_to_cart(user_id, book_isbn, book_qty):
    # Check if the book exists in the "books" table.
    check_book = "SELECT * FROM books WHERE isbn = %s"

    # If the book exists, check if it's already in the cart for the given user_id.
    # If it is, update the quantity by adding book_qty to the existing quantity in the cart.
    # Otherwise, insert a new entry with the given user_id, book_isbn, and book_qty.
    check_cart = "SELECT * FROM cart WHERE userid = %s AND isbn = %s"
    with connection.cursor() as cursor2:
        cursor2.execute(check_cart, (user_id, book_isbn))
        cart_entry = cursor2.fetchone()

    if cart_entry:
        update_cart = "UPDATE cart SET qty = %s WHERE userid = %s AND isbn = %s"
        with connection.cursor() as cursor2:
            cursor2.execute(update_cart, (book_qty, user_id, book_isbn))
            connection.commit()
            print("Book quantity updated in cart.")
            return
    else:
        add_to_cart = "INSERT INTO cart (userid, isbn, qty) VALUES (%s, %s, %s)"
        with connection.cursor() as cursor2:
            cursor2.execute(add_to_cart, (user_id, book_isbn, book_qty))
            connection.commit()
        print("Book added to cart.")
        return


def print_invoice(cart_i):
    total_price = sum(i[1] * i[2] for i in cart_i)
    print("-" * 80)
    print(f"{'BOOK':<60} {'QTY':>4} {'PRICE':>10}")
    print("-" * 80)
    for i in cart_i:
        print(f"{i[0]} (qty: {i[2]}) - ${i[1] * i[2]:.2f}")
    print("-" * 80)
    print(f"{'TOTAL':>74} ${total_price:.2f}")
    print("-" * 80)


password = input("Enter password: ")

while True:
    try:
        with connect(
                host="localhost",
                user="root",
                password=password,
                database="book_store",
        ) as connection:
            print(connection)

            print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
                  "*******************************************\n"
                  "***                                     ***\n"
                  "***  Welcome to the Online Book Store   ***\n"
                  "***                                     ***\n"
                  "*******************************************")

            u_choice = input("1. Member Login\n"
                             "2. New Member Registration\n"
                             "q. Quit\n"
                             "-:")

            if u_choice == '1':
                email = input("Enter email: ")
                u_password = input("Enter password: ")

                # Hash the user's password
                hashed_password = hashlib.sha256(u_password.encode()).hexdigest()

                # Check if the user's credentials are valid
                sql = "SELECT * FROM members WHERE email = %s AND password = %s"
                val = (email, hashed_password)
                with connection.cursor() as cursor:
                    cursor.execute(sql, val)
                    result = cursor.fetchone()

                if result:
                    logged_in = True
                    with connection.cursor() as cursor:
                        query = "SELECT userid FROM members WHERE email = %s"
                        cursor.execute(query, (email,))
                        user_id = cursor.fetchone()
                        user_id = str(user_id[0])
                    while logged_in:
                        print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
                              "*******************************************\n"
                              "***                                     ***\n"
                              "***  Welcome to the Online Book Store   ***\n"
                              "***            Member Menu              ***\n"
                              "***                                     ***\n"
                              "*******************************************")
                        u_choice = input("1. Browse by Subject\n"
                                         "2. Search by Author/Title\n"
                                         "3. Check Out\n"
                                         "4. Logout\n"
                                         "-:")
                        print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n")

                        if u_choice == '1':
                            browsing = True
                            while browsing:
                                book_subjects = "SELECT DISTINCT subject FROM books"
                                with connection.cursor() as cursor:
                                    cursor.execute(book_subjects)
                                    result = cursor.fetchall()

                                subjects = []
                                for row in result:
                                    subjects.append(str(row[0]))
                                subjects = sorted(subjects)
                                num = 0
                                for subject in subjects:
                                    print(str(num) + ' ' + subject)
                                    num += 1

                                u_choice_subject = input("Enter corresponding number to view books with that subject: ")
                                selected_books = "SELECT * FROM books WHERE subject = %s LIMIT 2"
                                try:
                                    with connection.cursor() as cursor:
                                        cursor.execute(selected_books, (subjects[int(u_choice_subject)],))
                                        books = cursor.fetchall()
                                    for row in books:
                                        print("\nAuthor: " + row[1])
                                        print("Title: " + row[2])
                                        print("ISBN: " + row[0])
                                        print("Price: " + str(row[3]))
                                        print("Subject: " + row[4])

                                    print("\n-Write the ISBN to add to cart")
                                    print("-Write b to continue browsing")
                                    print("-Press ENTER to return to menu")
                                    u_choice = input("-:")

                                    if u_choice == 'b':
                                        print("continuing browsing...")

                                    elif u_choice == '':
                                        browsing = False

                                    else:
                                        add_book_to_cart(user_id, u_choice, int(input(
                                            "Enter the quantity of the book to add to your cart: ")))
                                        input("press ENTER to return to main menu.")
                                        browsing = False
                                except (IndexError, ValueError):
                                    print("Enter valid number")
                                    input("Press ENTER to try again..")

                        elif u_choice == '2':
                            print("1 Author Search")
                            print("2 Title Search")
                            print("3 Go Back to Main Menu")
                            u_choice_search = input("-:")

                            if u_choice_search == '1':
                                search_books("author")
                            elif u_choice_search == '2':
                                search_books("title")
                            else:
                                print("Going back to main menu")

                        elif u_choice == '3':
                            # Get shipping info
                            with connection.cursor() as cursor:
                                cursor.execute(
                                    "SELECT adress, city, state, zip, lname, fname FROM members WHERE userid = %s",
                                    (user_id,))
                                shipping_info = cursor.fetchone()

                                # Save shipping information to individual variables
                                address, city, state, zip_code, lname, fname = map(str, shipping_info)

                            # Get user's cart
                            with connection.cursor() as cursor:
                                cursor.execute(
                                    "SELECT books.title, books.price, cart.qty, books.isbn FROM cart INNER JOIN books ON "
                                    "cart.isbn=books.isbn WHERE cart.userid=%s",
                                    (user_id,))
                                cart_items = cursor.fetchall()

                            # Display invoice and shipping information
                            estimated_delivery_date = datetime.date.today() + datetime.timedelta(days=7)
                            print("Cart:")
                            print_invoice(cart_items)

                            # Prompt user to confirm purchase
                            confirmation = input("Proceed to checkout (Y/N)? ").lower()
                            if confirmation == "y" and len(cart_items) > 0:
                                # Save order to orders table
                                with connection.cursor() as cursor:
                                    zip_code = str(zip_code)  # convert zip_code to string
                                    cursor.execute(
                                        "INSERT INTO orders (userid, recived, shipAddress, shipCity, shipState, shipZip) VALUES (%s, %s, %s, %s, %s, %s)",
                                        (user_id, datetime.date.today().strftime('%Y-%m-%d'), address, city, state,
                                         zip_code))
                                    order_id = cursor.lastrowid

                                # Save order details to odetails table
                                print("\n\n\n\n\n\n\n\n\n\n\n\n\n")
                                print("Invoice:")
                                with connection.cursor() as cursor:
                                    for item in cart_items:
                                        cursor.execute(
                                            "INSERT INTO odetails (ono, isbn, qty, price) VALUES (%s, %s, %s, %s)",
                                            (order_id, item[3], item[2], item[1]))

                                # display order confirmation
                                print(
                                    f"Thank you for shopping with us!\nYour order has been placed and will be delivered by {estimated_delivery_date}.\n")
                                print(f"Shipping Adress")
                                print(f"name: {fname} {lname}")
                                print(f"Adress: {address}\n{' ' * 8}{city}\n{' ' * 8}{state} {zip_code}")
                                print_invoice(cart_items)
                                print(
                                    f"Thank you for shopping with us!\nYour order has been placed and will be delivered by {estimated_delivery_date}")

                                # Clear user's cart
                                with connection.cursor() as cursor:
                                    cursor.execute("DELETE FROM cart WHERE userid=%s", (user_id,))
                                    connection.commit()
                                input("Press ENTER to return to menu..")

                            else:
                                print("Purchase canceled.")
                                input("Press ENTER to return to menu..")

                        elif u_choice == '4':
                            logged_in = False

                        else:
                            input("Enter valid input\nPress ENTER to try again")

                else:
                    input("Invalid credentials\nPress ENTER to continue")

            elif u_choice == '2':
                print("Welcome to the member registration!")
                fname = input("Enter first name: ")
                lname = input("Enter last name: ")
                adress = input("Enter address: ")
                city = input("Enter city: ")
                state = input("Enter state: ")
                zip = input("Enter zip: ")
                phone = input("Enter phone: ")

                # Ask for email until it is a valid email
                while True:
                    email = input("Enter email: ")
                    if re.search(r'^[a-z0-9]+[._]?[ a-z0-9]+@\w+\.[a-z]{2,3}$', email):
                        break
                    else:
                        print("Email is invalid, please enter a valid email")

                new_password = input("Enter password: ")
                u_confirm = (input("Confirm registration? y/n")).lower()

                if u_confirm == 'y':
                    # Hash the new password
                    hashed_password = hashlib.sha256(new_password.encode()).hexdigest()

                    try:
                        # Insert member registration data into the database
                        sql = "INSERT INTO members (fname, lname, adress, city, state, zip, phone, email, password) " \
                              "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s) "
                        val = (fname, lname, adress, city, state, zip, phone, email, hashed_password)
                        with connection.cursor() as cursor:
                            cursor.execute(sql, val)
                            connection.commit()
                    except Exception as e:
                        print("Error: ", e)
                        input("Press ENTER to return to menu..")

            elif u_choice == 'q':
                quit()

    except Error as e:
        print(e)
        break
