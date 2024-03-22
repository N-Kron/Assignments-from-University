package model;


/**
 * This interface defines the methods for saving and loading data.
 */
public interface Persistence {
  void saveData();

  void loadData(MemberStorage memberStorage);
}
