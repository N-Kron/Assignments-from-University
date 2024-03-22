package assignmenttwo.solarsystem;

/**
 * This class represents a moon, which is a type of heavenly body that orbits a
 * planet.
 * It extends the abstract class HeavenlyBody and implements the abstract method
 * checkAvgRadiusInKM.
 * It also has a specific attribute of average orbit radius.
 */

public abstract class HeavenlyBody implements Comparable<HeavenlyBody> {
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
  public int compareTo(HeavenlyBody other) {
    return Integer.compare(this.getAvgRadiusInKm(), other.getAvgRadiusInKm());
  }

  @Override
  public String toString() {
    return "Heavenly Body : " + name + ", average radius " + radius + " km";
  }
}