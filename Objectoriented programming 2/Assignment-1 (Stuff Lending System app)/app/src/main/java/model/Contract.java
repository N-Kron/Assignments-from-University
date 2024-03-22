package model;

/**
 * This class represents a Contract for an item between a lender and a borrower.
 */
public class Contract implements Observer {
  private Item item;
  private Member lender;
  private Member borrower;
  private int creationDate;
  private int endDate;

  /**
   * Constructor for the Contract class.
   *
   * @param item         The item that is being borrowed.
   * @param lender       The member who is lending the item.
   * @param borrower     The member who is borrowing the item.
   * @param creationDate The date when the contract was created.
   * @param endDate      The date when the item is expected to be returned.
   */
  public Contract(Item item, Member lender, Member borrower, int creationDate, int endDate, Calendar calendar) {
    this.item = new Item(item); // Create a new Item object with the same state
    this.lender = new Member(lender); // Create a new Member object with the same state
    this.borrower = new Member(borrower); // Create a new Member object with the same state
    this.creationDate = creationDate;
    this.endDate = endDate;
    calendar.registerObserver(this);
  }


  @Override
  public boolean update(int date) {
    if (this.endDate == date) {
      this.item.removeContract();
      return true;
    }
    return false;
  }

  // getters

  public int getCreationDate() {
    return creationDate;
  }

  public int getEndDate() {
    return endDate;
  }

}