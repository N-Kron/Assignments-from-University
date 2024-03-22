package model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an Item that can be borrowed by a Member.
 */
public class Item {
  private String name;
  private String description;
  private String category; // Added category field
  private int costPerDay;
  private int dayOfCreation;
  private Contract contract; // Added field to store the contract
  private List<Contract> pastCurrentContracts;

  /**
   * Constructor for the Item class.
   *
   * @param name        The name of the item.
   * @param description The description of the item.
   * @param category    The category of the item.
   * @param costPerDay  The cost per day of the item.
   * @param currentDay  The current day.
   */
  public Item(String name, String description, String category, int costPerDay, int currentDay) {
    this.name = name;
    this.description = description;
    this.category = category; // Initialize the category
    this.costPerDay = costPerDay;
    this.contract = null;
    this.pastCurrentContracts = new ArrayList<Contract>();
    this.dayOfCreation = currentDay;
  }

  /**
   * Copy constructor for the Item class.
   * Initializes a new Item object using an existing Item object.
   *
   * @param item The existing Item object to copy from.
   */
  public Item(Item item) {
    this.name = item.name;
    this.description = item.description;
    this.category = item.category;
    this.costPerDay = item.costPerDay;
    this.contract = item.getContract();
    this.dayOfCreation = item.dayOfCreation;
    this.pastCurrentContracts = new ArrayList<>(item.pastCurrentContracts);
  }


  /**
   * Adds a contract for this item if it is free and the borrower has enough credits.
   *
   * @param lender       The member who is lending the item.
   * @param borrower     The member who is borrowing the item.
   * @param creationDate The date when the contract was created.
   * @param endDate      The date when the item is expected to be returned.
   * @return true if the contract was successfully added, false otherwise.
   */
  public boolean addContract(Member lender, Member borrower, int creationDate, int endDate, Calendar calendar) {
    // Can only be added if borrower has enough credits and item is free
    int oneTimeCost = this.costPerDay * (endDate - creationDate);
    if (this.contract == null && borrower.getCredits() >= oneTimeCost) {
      this.contract = new Contract(this, lender, borrower, creationDate, endDate, calendar);
      this.pastCurrentContracts.add(contract);
      borrower.removeCredits(oneTimeCost);
      return true;
    }
    return false;
  }

  // getters
  public String getName() {
    return name;
  }

  public int getCostPerDay() {
    return costPerDay;
  }

  public Contract getContract() {
    return this.contract;
  }

  public String getDescription() {
    return description;
  }

  public String getCategory() {
    return category;
  }

  public int getDayOfCreation() {
    return this.dayOfCreation;
  }

  public List<Contract> getPastCurrentContracts() {
    return new ArrayList<>(pastCurrentContracts); // Return a copy of the list
  }

  public void setCategory(String newCategory) {
    this.category = newCategory;
  }

  public void removeContract() {
    this.contract = null;
  }

  public void setName(String newName) {
    this.name = newName;
  }

  public void setDescription(String newDesc) {
    this.description = newDesc;
  }

  public void setCostPerDay(int newCostPerDay) {
    this.costPerDay = newCostPerDay;
  }

}
