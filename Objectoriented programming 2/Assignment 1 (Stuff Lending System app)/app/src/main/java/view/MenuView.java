package view;

import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.Contract;
import model.Item;
import model.Member;

/**
 * This class represents a MenuView that handles user interactions.
 */
public class MenuView {
  private Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

  /**
   * Enum for member menu choices.
   */
  public enum MemberMenuChoice {
    ITEM_MENU,
    MEMBER_MENU,
    UPDATE_MEMBER_INFO,
    ADVANCE_TIME,
    EXIT
  }

  /**
   * Enum for member display menu choices.
   */
  public enum MemberDisplayMenuChoice {
    VERBOSE,
    SPECIFIC_MEMBER_VIEW,
    SIMPLE_MEMBER_VIEW,
    DELETE_MEMBER_VIEW,
    REGISTER_NEW_MEMBER,
    BACK
  }

  /**
   * Enum for item menu choices.
   */
  public enum ItemMenuChoice {
    REGISTER_ITEM,
    BORROW_ITEM,
    EDIT_ITEM,
    DELETE_ITEM,
    VIEW_ITEM
  }

  /**
   * Enum for update options choices.
   */
  public enum UpdateOptionsChoice {
    NAME,
    EMAIL,
    PHONE_NUMBER
  }


  /**
   * Displays the member menu and returns the user's choice.
   *
   * @return The user's choice from the member menu.
   */
  public MemberMenuChoice displayMemberMenuChoice() {
    System.out.println("MEMBER MENU");
    System.out.println("1. Item menu");
    System.out.println("2. Member menu");
    System.out.println("3. Update your member info");
    System.out.println("4. Advance Time");
    System.out.println("5. Exit");

    try {
      int choice = scanner.nextInt();
      scanner.nextLine(); // consume newline

      return switch (choice) {
        case 1 -> MemberMenuChoice.ITEM_MENU;
        case 2 -> MemberMenuChoice.MEMBER_MENU;
        case 3 -> MemberMenuChoice.UPDATE_MEMBER_INFO;
        case 4 -> MemberMenuChoice.ADVANCE_TIME;
        case 5 -> MemberMenuChoice.EXIT;
        default -> throw new IllegalArgumentException("Invalid choice.");
      };
    } catch (InputMismatchException e) {
      scanner.nextLine(); // consume the invalid input
      System.out.println("Invalid input. Please enter an integer.");
      return displayMemberMenuChoice(); // recursively call the method until valid input is provided
    }
  }

  /**
   * Displays the member display menu and returns the user's choice.
   *
   * @return The user's choice from the member display menu.
   */
  public MemberDisplayMenuChoice displayMemberDisplayMenu() {
    System.out.println("How do you want to display members?");
    System.out.println("1. Verbose member view");
    System.out.println("2. Specific member view");
    System.out.println("3. Simple member view");
    System.out.println("4. Delete member view");
    System.out.println("5. Register new member");
    System.out.println("6. back");

    try {
      int choice = scanner.nextInt();
      scanner.nextLine(); // consume newline

      return switch (choice) {
        case 1 -> MemberDisplayMenuChoice.VERBOSE;
        case 2 -> MemberDisplayMenuChoice.SPECIFIC_MEMBER_VIEW;
        case 3 -> MemberDisplayMenuChoice.SIMPLE_MEMBER_VIEW;
        case 4 -> MemberDisplayMenuChoice.DELETE_MEMBER_VIEW;
        case 5 -> MemberDisplayMenuChoice.REGISTER_NEW_MEMBER;
        case 6 -> MemberDisplayMenuChoice.BACK;
        default -> throw new IllegalArgumentException("Invalid choice.");
      };
    } catch (InputMismatchException e) {
      scanner.nextLine(); // consume the invalid input
      System.out.println("Invalid input. Please enter an integer.");
      return displayMemberDisplayMenu(); // recursively call the method until valid input is provided
    }
  }

  /**
   * Displays the item menu and returns the user's choice.
   *
   * @return The user's choice from the item menu.
   */
  public ItemMenuChoice displayItemMenu() {
    System.out.println("ITEM MENU");
    System.out.println("1. Register item");
    System.out.println("2. Borrow item");
    System.out.println("3. Edit item");
    System.out.println("4. Delete item");
    System.out.println("5. View item");

    try {
      int choice = scanner.nextInt();
      scanner.nextLine(); // consume newline

      return switch (choice) {
        case 1 -> ItemMenuChoice.REGISTER_ITEM;
        case 2 -> ItemMenuChoice.BORROW_ITEM;
        case 3 -> ItemMenuChoice.EDIT_ITEM;
        case 4 -> ItemMenuChoice.DELETE_ITEM;
        case 5 -> ItemMenuChoice.VIEW_ITEM;
        default -> throw new IllegalArgumentException("Invalid choice.");
      };
    } catch (InputMismatchException e) {
      scanner.nextLine(); // consume the invalid input
      System.out.println("Invalid input. Please enter an integer.");
      return displayItemMenu(); // recursively call the method until valid input is provided
    }
  }

  /**
   * Displays the update options and returns the user's choice.
   *
   * @return The user's choice from the update options.
   */
  public UpdateOptionsChoice displayUpdateOptions() {
    System.out.println("What would you like to update?");
    System.out.println("1. Name");
    System.out.println("2. Email");
    System.out.println("3. Phone number");

    try {
      int choice = scanner.nextInt();
      scanner.nextLine(); // consume newline

      return switch (choice) {
        case 1 -> UpdateOptionsChoice.NAME;
        case 2 -> UpdateOptionsChoice.EMAIL;
        case 3 -> UpdateOptionsChoice.PHONE_NUMBER;
        default -> throw new IllegalArgumentException("Invalid choice.");
      };
    } catch (InputMismatchException e) {
      scanner.nextLine(); // consume the invalid input
      System.out.println("Invalid input. Please enter an integer.");
      return displayUpdateOptions(); // recursively call the method until valid input is provided
    }
  }

  /**
   * Displays a member's details in a verbose format.
   *
   * @param member The member whose details will be displayed.
   */
  public void displayMembersVerbose(Member member) {
    System.out.println("--------------------------------|");
    System.out.println("Name: " + member.getName());
    System.out.println("Email: " + member.getEmail());
    System.out.println("OWNED ITEMS:");
    for (Item item : member.getItems()) {
      displayItem(item);
    }
    System.out.println("--------------------------------|");
  }

  /**
   * Displays a member's details.
   *
   * @param member The member whose details will be displayed.
   */
  public void displayOneMember(Member member) {
    System.out.println("--------------------------------|");
    System.out.println("Name: " + member.getName());
    System.out.println("Email: " + member.getEmail());
    System.out.println("OWNED ITEMS:");
    for (Item item : member.getItems()) {
      displayItem(item);
    }
    System.out.println("--------------------------------|");
  }

  /**
   * Displays a member's name and email.
   *
   * @param member The member whose name and email will be displayed.
   */
  public void displayMembersSimple(Member member) {
    System.out.println("--------------------------------|");
    System.out.println("Name: " + member.getName());
    System.out.println("Email: " + member.getEmail());
    System.out.println("--------------------------------|");
  }

  /**
   * Displays a member's name and ID for deletion.
   *
   * @param member The member whose name and ID will be displayed.
   */
  public void displayDeleteMember(Member member) {
    System.out.println("--------------------------------|");
    System.out.println("Name: " + member.getName());
    System.out.println("ID: " + member.getMemberId());
    System.out.println("--------------------------------|");
  }

  /**
   * Displays an item's details.
   *
   * @param item The item whose details will be displayed.
   */
  public void displayItem(Item item) {
    System.out.println("--------------------------------");
    System.out.println("Item Name: " + item.getName());
    System.out.println("Item Description: " + item.getDescription());
    System.out.println("Item Category: " + item.getCategory());
    System.out.println("Cost Per Day: " + item.getCostPerDay());
    Contract contract = item.getContract();
    if (contract != null) {
      displayContract(contract);
    }
    System.out.println("--------------------------------");
  }

  public void displayContract(Contract contract) {
    System.out.println("Contract start date: " + contract.getCreationDate());
    System.out.println("Contract end date: " + contract.getEndDate());
  }

  /**
   * Prompts the user for the ID of the member to delete.
   *
   * @return The ID of the member to delete.
   */
  public String promptDeletionInput() {
    System.out.println("\npress \"e\" to exit");
    System.out.println("Enter MemberID to delete member:");

    return scanner.nextLine();
  }


}