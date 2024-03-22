package model;

/**
 * Manages database operations for members. Implements the Persistence interface.
 * Provides methods to save and load member data, and get all members.
 */
public class DataBaseManager implements Persistence {

  public DataBaseManager() {
  }

  @Override
  public void saveData() {
    // Code to save data to a database
    // For now, this method can be left empty
  }

  @Override
  public void loadData(MemberStorage memberStorage) {
    // Code to load data from a database
    // Currently just hard coded
    Calendar calendar = new Calendar();

    memberStorage.addMember("TU1", "test1@example.com", "123-456-7890", "000001");
    Member testMember1 = memberStorage.getMemberByUsername("TU1");
    Item item1 = new Item("Item1", "Description1", "Category1", 10, 0);
    testMember1.addItem(item1);

    memberStorage.addMember("TU2", "test2@example.com", "098-765-4321", "000002");
    Member testMember2 = memberStorage.getMemberByUsername("TU1");
    Item item2 = new Item("Item2", "Description2", "Category2", 20, 0);
    testMember2.addItem(item2);

    // Hardcode a contract between testMember1 and testMember2 for item1
    item1.addContract(testMember1, testMember2, 0, 10, calendar);


  }

}
