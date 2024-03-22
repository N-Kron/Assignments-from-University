package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class represents a storage for all members.
 */
public class MemberStorage {
  private List<Member> members;
  private final Calendar calendar;

  public MemberStorage() {
    members = new ArrayList<>();
    calendar = new Calendar();
  }

  /**
   * Constructor for the MemberStorage class.
   */
  public boolean isEmailUnique(String email) {
    for (Member existingMember : members) {
      if (existingMember.getEmail().equals(email)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if a phone number is unique among all members.
   *
   * @param phoneNumber The phone number to check.
   * @return true if the phone number is unique, false otherwise.
   */
  public boolean isPhoneNumberUnique(String phoneNumber) {
    for (Member existingMember : members) {
      if (existingMember.getPhoneNumber().equals(phoneNumber)) {
        return false;
      }
    }
    return true;
  }

  public void advanceDay() {
    calendar.advanceDay();
  }

  /**
   * Checks if a phone number is unique among all members.
   *
   * @param id The id to check.
   * @return true if the id is unique, false otherwise.
   */
  public boolean isIdUnique(String id) {
    for (Member existingMember : members) {
      if (existingMember.getMemberId().equals(id)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Adds a member to the storage if their email and phone number are unique. a unique ID is generated.
   *
   * @param name        The name of the member to add.
   * @param email       The email of the member to add.
   * @param phoneNumber The phone number of the member to add.
   * @param id          The ID for the member to add. If null or not unique, a unique ID is generated.
   * @return true if the member was successfully added, false otherwise.
   */
  public boolean addMember(String name, String email, String phoneNumber, String id) {
    while (id == null || !isIdUnique(id)) {
      id = UUID.randomUUID().toString().substring(0, 6);
    }

    if (isEmailUnique(email) && isPhoneNumberUnique(phoneNumber)) {
      members.add(new Member(name, email, phoneNumber, calendar.getDay(), id));
      return true;
    }
    return false;
  }

  public boolean deleteMember(String memberId) {
    return members.removeIf(member -> member.getMemberId().equals(memberId));
  }

  /**
   * Gets a member from the storage by their member ID.
   *
   * @param memberId The ID of the member to get.
   * @return The member with the given ID, or null if no such member exists.
   */
  public Member getMember(String memberId) {
    for (Member member : members) {
      if (member.getMemberId().equals(memberId)) {
        return member;
      }
    }
    return null; // No such member exists
  }

  /**
   * Gets a member from the storage by their username.
   *
   * @param username The username of the member to get.
   * @return The member with the given username, or null if no such member exists.
   */
  public Member getMemberByUsername(String username) {
    for (Member member : members) {
      if (member.getName().equals(username)) {
        return member;
      }
    }
    return null; // No such member exists
  }

  /**
   * This method allows a member to borrow an item from another member.
   * The method works by first finding the member who wants to borrow the item. If the member exists,
   * it then iterates over all the items owned by all members to find the item to be borrowed.
   * If the item is found, it creates a new contract for the item with the borrowing member,
   * the lending member, the current day, and the end date. The method returns true if the contract
   * is successfully created and added to the item, and false otherwise.
   */
  public boolean borrowItem(String borrowerName, String itemName, int endDate) {
    boolean borrow = false;
    Member borrower = getMemberByUsername(borrowerName);
    if (borrower != null) {
      // Get the item from all members' items
      for (Member loaner : members) {
        for (Item item : loaner.getItems()) {
          if (item.getName().equals(itemName)) {
            // Check if the
            borrow = item.addContract(loaner, borrower, calendar.getDay(), endDate, calendar);
          }
          return borrow;
        }
      }
    }
    return false;
  }

  public ArrayList<Member> getAllMembers() {
    return new ArrayList<>(members);
  }

  public void updateMemberName(Member member, String newName) {
    member.setName(newName);
  }

  /**
   * Updates a member's email in the storage if it is unique.
   *
   * @param member   The member whose email will be updated.
   * @param newEmail The new email for the member.
   * @return true if the email was successfully updated, false otherwise.
   */
  public boolean updateMemberEmail(Member member, String newEmail) {
    if (isEmailUnique(newEmail)) {
      member.setEmail(newEmail);
      return true;
    }
    return false;
  }

  /**
   * Updates a member's phone number in the storage if it is unique.
   *
   * @param member         The member whose phone number will be updated.
   * @param newPhoneNumber The new phone number for the member.
   * @return true if the phone number was successfully updated, false otherwise.
   */
  public boolean updateMemberPhoneNumber(Member member, String newPhoneNumber) {
    if (isPhoneNumberUnique(newPhoneNumber)) {
      member.setPhoneNumber(newPhoneNumber);
      return true;
    }
    return false;
  }

  /**
   * This method allows a member to register a new item.
   * The method works by first finding the member who wants to register the item. If the member exists,
   * it then creates a new item with the provided details and the current day as the registration date.
   * The new item is then added to the member's list of items.
   */
  public void registerItem(String itemName, String itemDesc, String category, int costPerDay, String memberName) {
    Member member = getMemberByUsername(memberName);
    Item item = new Item(itemName, itemDesc, category, costPerDay, calendar.getDay());
    member.addItem(item);
  }
}
