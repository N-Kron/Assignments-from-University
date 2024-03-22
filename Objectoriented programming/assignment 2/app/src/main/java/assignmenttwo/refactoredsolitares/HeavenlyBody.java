package assignmenttwo.refactoredsolitares;

/**
 * The abstract `HeavenlyBody` class represents a celestial object, such as a
 * planet or a moon,
 * with a name and an average radius in kilometers.
 * This class provides methods to set and get the name and average radius of the
 * object,
 * as well as to check the validity of the radius.
 * Subclasses of `HeavenlyBody` must implement the `checkAvgRadiusInKM()`
 * method to enforce specific constraints on the average radius.
 */

public abstract class HeavenlyBody {

  private String name;
  private int radius;

  public HeavenlyBody(String name, int radius) {
    setName(name);
    setAvgRadiusInKm(radius);
  }

  private void setName(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
    this.name = name;
  }

  private void setAvgRadiusInKm(int radius) {
    this.radius = radius;
  }

  public String getName() {
    return this.name;
  }

  public int getAvgRadiusInKm() {
    return this.radius;
  }

  public abstract void checkAvgRadiusInKm(int radius) throws IllegalArgumentException;

  @Override
  public String toString() {
    return "Moon: " + name + ", average radius " + radius + " km";
  }
}
