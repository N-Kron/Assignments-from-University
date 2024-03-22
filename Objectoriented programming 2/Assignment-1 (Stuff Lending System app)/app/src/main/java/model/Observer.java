package model;

/**
 * This interface represents an Observer in the Observer design pattern.
 * An Observer is an object that wishes to be informed about changes in the state of another object (the subject).
 */
public interface Observer {
  boolean update(int date);
}
