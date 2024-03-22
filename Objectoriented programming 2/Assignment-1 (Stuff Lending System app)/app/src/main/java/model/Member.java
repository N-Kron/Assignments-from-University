package model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Member who can borrow and lend items.
 */
public class Member {
  private final String memberId;
  private String name;
  private String email;
  private String phoneNumber;
  private int credits;
  private int creationDate;
  private List<Item> items;

  /**
   * Constructor for the Member class.
   *
   * @param name        The name of the member.
   * @param email       The email of the member.
   * @param phoneNumber The phone number of the member.
   * @param dayCount    The current day count.
   */
  public Member(String name, String email, String phoneNumber, int dayCount, String memberId) {
    this.memberId = memberId;
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.credits = 0; // start with 0 credits
    this.creationDate = dayCount;
    this.items = new ArrayList<>(); // Initialize the items list
  }

  /**
   * Copy constructor for the Member class.
   * Initializes a new Member object using an existing Member object.
   *
   * @param member The existing Member object to copy from.
   */
  public Member(Member member) {
    this.memberId = member.memberId;
    this.name = member.name;
    this.email = member.email;
    this.phoneNumber = member.phoneNumber;
    this.credits = member.credits;
    this.creationDate = member.creationDate;
    this.items = new ArrayList<>(member.items);
  }


  public void addItem(Item item) {
    items.add(item); // Add an item to the list
    addCredits(100);
  }

  /**
   * Deletes an item from the member's list of items.
   *
   * @param name The name of the item to delete.
   * @return true if the item was successfully deleted, false otherwise.
   */
  public boolean deleteItem(String name) {
    for (Item item : items) {
      if (item.getName().equals(name)) {
        this.items.remove(item);
        return true;
      }
    }
    return false;
  }

  public void addCredits(int credits) {
    this.credits += credits;
  }

  /**
   * Removes credits from the member if they have enough.
   *
   * @param credits The number of credits to remove.
   */
  public void removeCredits(int credits) {
    if (this.credits >= credits) {
      this.credits -= credits;
    } else {
      throw new IllegalArgumentException("Insufficient credits");
    }
  }


  /**
   * Edits an item owned by this member with new details.
   *
   * @param itemName      The name of the item to edit.
   * @param newName       The new name for the item.
   * @param newDesc       The new description for the item.
   * @param newCategory   The new category for the item.
   * @param newCostPerDay The new cost per day for the item.
   * @return true if the item was successfully edited, false otherwise.
   */
  public boolean editItem(String itemName, String newName, String newDesc, String newCategory, int newCostPerDay) {
    for (Item item : this.items) {
      if (item.getName().equals(itemName)) {
        item.setName(newName);
        item.setDescription(newDesc);
        item.setCostPerDay(newCostPerDay);
        item.setCategory(newCategory);
        return true;
      }
    }
    throw new IllegalArgumentException("Item not found");
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  // getters
  public String getMemberId() {
    return memberId;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public int getCredits() {
    return credits;
  }

  public List<Item> getItems() {
    return new ArrayList<>(items); // Return a copy of the list
  }

  /**
   * Gets an item owned by this member by its name.
   *
   * @param name The name of the item to get.
   * @return The item with the given name.
   * @throws IllegalArgumentException if no item with the given name is found.
   */
  public Item getItemByName(String name) {
    for (Item item : items) {
      if (item.getName().equals(name)) {
        return item;
      }
    }
    throw new IllegalArgumentException("Item not found");
  }

  public int getCreationDate() {
    return creationDate;
  }

}
