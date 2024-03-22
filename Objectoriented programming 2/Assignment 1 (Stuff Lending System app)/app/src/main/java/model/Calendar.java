package model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Calendar that keeps track of the current day.
 */
public class Calendar implements Subject {
  private int day;
  private List<Observer> observers;

  public Calendar() {
    this.day = 0;
    this.observers = new ArrayList<>();
  }

  public int getDay() {
    return this.day;
  }

  /**
   * Advances the current day by one and notifies all observers.
   * Observers that return true on update are removed from the observer list.
   */
  public void advanceDay() {
    this.day++;
    observers.removeIf(observer -> observer.update(this.day));
  }

  @Override
  public void registerObserver(Observer o) {
    observers.add(o);
  }

  @Override
  public void removeObserver(Observer o) {
    observers.remove(o);
  }
}
