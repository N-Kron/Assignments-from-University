package view;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * This class provides methods for user interaction.
 */
public class UserView {
  private Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

  public String enterName() {
    System.out.println("Enter name:");
    return scanner.nextLine();
  }

  public String enterEmail() {
    System.out.println("Enter email:");
    return scanner.nextLine();
  }

  public String enterPhoneNumber() {
    System.out.println("Enter phone number:");
    return scanner.nextLine();
  }

  public String enterMemberId() {
    System.out.println("Enter member id:");
    return scanner.nextLine();
  }

  public String enterMemberName() {
    System.out.println("Enter member name:");
    return scanner.nextLine();
  }

  public String enterItemName() {
    System.out.println("Enter item name");
    return scanner.nextLine();
  }

  public void couldNotBorrow() {
    System.out.println("Could not borrow item");
  }

  /**
 * This method prompts the user to enter the cost per day for an item.
 * It continues to prompt until a valid integer is entered.
 *
 * @return The cost per day as an integer.
 */
  public int enterCostPerDay() {
    while (true) {
      try {
        System.out.println("Enter cost per day:");
        return Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number.");
      }
    }
  }

  /**
 * This method prompts the user to enter the number of days they want to borrow an item.
 * It continues to prompt until a valid integer is entered.
 *
 * @return The number of days as an integer.
 */
  public int enterEndDate() {
    while (true) {
      try {
        System.out.println("How many days do you want to borrow the item:");
        return Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number.");
      }
    }
  }

  /**
 * This method prompts the user to enter a new cost per day for an item.
 * It continues to prompt until a valid integer is entered.
 *
 * @return The new cost per day as an integer.
 */
  public int enterNewCostPerDay() {
    while (true) {
      try {
        System.out.println("Enter new cost per day for the item:");
        return Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number.");
      }
    }
  }

  public String enterItemDescription() {
    System.out.println("Enter item description:");
    return scanner.nextLine();
  }

  public String enterItemCategory() {
    System.out.println("Enter item category:");
    return scanner.nextLine();
  }

  public String enterNewItemName() {
    System.out.println("Enter new name for the item:");
    return scanner.nextLine();
  }

  public String enterNewItemDescription() {
    System.out.println("Enter new description for the item:");
    return scanner.nextLine();
  }

  public String enterNewItemCategory() {
    System.out.println("Enter new category for the item:");
    return scanner.nextLine();
  }

  public void printItemEditedSuccessfully() {
    System.out.println("Item edited successfully!");
  }

  public void printItemNotFound() {
    System.out.println("Item not found");
  }

  public void printItemDeleted() {
    System.out.println("Item deleted");
  }

  public void printInvalidChoice() {
    System.out.println("Invalid choice");
  }

  public void printOwnerOfItem() {
    System.out.println("You are the owner of this item.");
  }

  public void printItemBorrowedSuccessfully() {
    System.out.println("Item borrowed successfully.");
  }

  public void printItemAlreadyBorrowedOrBroke() {
    System.out.println("Item is already borrowed or you are too broke");
  }

  public void phoneNumberAlreadyExists() {
    System.out.println("Phone number already exists");
  }

  public void emailAlreadyExists() {
    System.out.println("Email already exists");
  }

  public void printLoggingOut() {
    System.out.println("Logging out");
  }

  public void printDayAdvanced() {
    System.out.println("Day has been advanced!");
  }

  public void printCurrentDay(int day) {
    System.out.println("Today is day " + day);
  }

  public void printRegistrationSuccessful() {
    System.out.println("Registration successful.");
  }

  public void printRegistrationFailed() {
    System.out.println("Registration failed. A member with the same email or phone number already exists.");
  }

  public void printLoginSuccessful() {
    System.out.println("Login successful.");
  }

  public void printLoginFailed() {
    System.out.println("Login failed.");
  }

  public void printSelfDeletion() {
    System.out.println("You have successfully deleted your own account.");
  }

  public void printMemberDeleted() {
    System.out.println("The member has been successfully deleted.");
  }

  public void printMemberNotFound() {
    System.out.println("Member not found.");
  }

  public void printInvalidInput() {
    System.out.println("Invalid input!");
  }
}
