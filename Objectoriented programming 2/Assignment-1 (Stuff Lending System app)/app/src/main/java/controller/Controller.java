package controller;

import model.DataBaseManager;
import model.Member;
import model.MemberStorage;
import view.MenuView;
import view.UserView;

/**
 * This class represents a Controller for handling user interactions.
 */
public class Controller {
  private final MemberStorage memberStorage;
  private final MenuView menuView;
  private final UserView userView;
  private final DataBaseManager dbManager;

  /**
   * Constructor for the Controller class.
   * Initializes memberStorage, menuView, userView, calendar, and loggedInMember.
   */
  public Controller() {
    memberStorage = new MemberStorage();
    menuView = new MenuView();
    userView = new UserView();
    dbManager = new DataBaseManager();
  }

  /**
   * Starts the application and handles user interactions.
   */
  public void start() {
    // Load stored members and their items
    dbManager.loadData(memberStorage);

    boolean closeProgram = false;
    while (!closeProgram) {
      MenuView.MemberMenuChoice memberChoice = menuView.displayMemberMenuChoice();

      switch (memberChoice) {
        case ITEM_MENU -> handleItemMenu();
        case MEMBER_MENU -> handleMemberDisplay();
        case UPDATE_MEMBER_INFO -> handleMemberEdit();
        case ADVANCE_TIME -> {
          memberStorage.advanceDay();
          userView.printDayAdvanced();
        }
        case EXIT -> closeProgram = true;
        default -> userView.printInvalidChoice();
      }

    }
  }

  private void handleItemMenu() {
    MenuView.ItemMenuChoice itemChoice = menuView.displayItemMenu();

    switch (itemChoice) {
      case REGISTER_ITEM -> {
        // Ask the user for item details
        String itemName = userView.enterItemName();
        String itemDesc = userView.enterItemDescription();
        String category = userView.enterItemCategory();
        int costPerDay = userView.enterCostPerDay();

        // Register the new item
        memberStorage.registerItem(itemName, itemDesc, category, costPerDay, userView.enterMemberName());
      }
      case BORROW_ITEM -> {
        // Ask the user for the item name and end date
        String borrowItemName = userView.enterItemName();
        int endDate = userView.enterEndDate();

        // Borrow the item
        if (memberStorage.borrowItem(borrowItemName, userView.enterMemberName(), endDate)) {
          userView.couldNotBorrow();
        }
      }
      case EDIT_ITEM -> {
        // Ask the user for the item name and new details
        String editItemName = userView.enterItemName();
        String newName = userView.enterNewItemName();
        String newDesc = userView.enterNewItemDescription();
        String newCategory = userView.enterNewItemCategory();
        int newCostPerDay = userView.enterNewCostPerDay();

        // Edit the item
        try {
          Member member = memberStorage.getMemberByUsername(userView.enterMemberName());
          if (member != null) {
            if (member.editItem(editItemName, newName, newDesc, newCategory, newCostPerDay)) {
              userView.printItemEditedSuccessfully();
            }
          } else {
            userView.printMemberNotFound();
          }
        } catch (IllegalArgumentException e) {
          userView.printItemNotFound();
        }

      }
      case DELETE_ITEM -> {
        // Ask the user for the item name
        String deleteItemName = userView.enterItemName();

        // Iterate over all members
        for (Member member : memberStorage.getAllMembers()) {
          // Check if the item exists in the member's list
          if (member.getItemByName(deleteItemName) != null) {
            // If the item exists, delete it, print a message and break the loop
            member.deleteItem(deleteItemName);
            userView.printItemDeleted();
            break;
          }
        }
      }

      case VIEW_ITEM -> {
        // Ask the user for the item name
        String viewItemName = userView.enterItemName();
        // View the item
        try {
          Member member = memberStorage.getMemberByUsername(userView.enterMemberName());
          if (member != null) {
            menuView.displayItem(member.getItemByName(viewItemName));
          } else {
            userView.printMemberNotFound();
          }
        } catch (IllegalArgumentException e) {
          userView.printItemNotFound();
        }
      }
      default -> userView.printInvalidChoice();
    }
  }


  private void handleMemberEdit() {
    MenuView.UpdateOptionsChoice updateChoice = menuView.displayUpdateOptions();
    Member member = memberStorage.getMemberByUsername(userView.enterMemberName());

    if (member != null) {
      switch (updateChoice) {
        case NAME -> memberStorage.updateMemberName(member, userView.enterName());
        case EMAIL -> {
          try {
            if (!memberStorage.updateMemberEmail(member, userView.enterEmail())) {
              userView.emailAlreadyExists();
            }
          } catch (IllegalArgumentException e) {
            userView.printInvalidInput();
          }
        }
        case PHONE_NUMBER -> {
          try {
            if (!memberStorage.updateMemberPhoneNumber(member, userView.enterPhoneNumber())) {
              userView.phoneNumberAlreadyExists();
            }
          } catch (IllegalArgumentException e) {
            userView.printInvalidInput();
          }
        }
        default -> userView.printInvalidChoice();
      }
    } else {
      userView.printMemberNotFound();
    }
  }


  private void handleMemberDisplay() {
    MenuView.MemberDisplayMenuChoice menuChoice = menuView.displayMemberDisplayMenu();

    switch (menuChoice) {
      case VERBOSE:
        for (Member member : memberStorage.getAllMembers()) {
          menuView.displayMembersVerbose(member);
        }
        break;
      case SPECIFIC_MEMBER_VIEW:
        Member specificMember = memberStorage.getMemberByUsername(userView.enterName());
        if (specificMember != null) {
          menuView.displayOneMember(specificMember);
        } else {
          userView.printMemberNotFound();
        }
        break;
      case SIMPLE_MEMBER_VIEW:
        for (Member member : memberStorage.getAllMembers()) {
          menuView.displayMembersSimple(member);
        }
        break;
      case DELETE_MEMBER_VIEW:
        for (Member member : memberStorage.getAllMembers()) {
          menuView.displayDeleteMember(member);
        }
        deleteMember();
        break;
      case REGISTER_NEW_MEMBER:
        // Ask the user for registration details
        String name = userView.enterName();
        String email = userView.enterEmail();
        String phoneNumber = userView.enterPhoneNumber();

        // Create a new member and register
        if (memberStorage.addMember(name, email, phoneNumber, null)) {
          userView.printRegistrationSuccessful();
        } else {
          userView.printRegistrationFailed();
        }
        break;
      case BACK:
        //Leave
        break;
      default:
        userView.printInvalidChoice();
    }
  }

  private void deleteMember() {
    String memberId = menuView.promptDeletionInput();
    if (!memberId.equals("e")) {
      if (memberStorage.deleteMember(memberId)) {
        userView.printMemberDeleted();
      } else {
        userView.printMemberNotFound();
      }
    }
  }

}
