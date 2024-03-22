package model;

/**
 * This interface represents a Subject in the Observer design pattern.
 * A Subject maintains a list of observers and notifies them of any state changes.
 */
public interface Subject {
  void registerObserver(Observer o);

  void removeObserver(Observer o);
}
