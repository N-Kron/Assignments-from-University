package assignmenttwo.solitares;

/**
 * Represents a planet with a name and average radius in kilometers.
 * Planets must have a radius between 2,000 km and 200,000 km.
 */

public class Planet {
  private String name;
  private int radius;

  public Planet(String name, int radius) {
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
    if (radius < 2000 || radius > 200000) {
      throw new IllegalArgumentException("Radius of planets must be between 2_000 km and 200_000 km");
    }
    this.radius = radius;
  }

  public String getName() {
    return this.name;
  }

  public int getAvgRadiusInKm() {
    return this.radius;
  }

  @Override
  public String toString() {
    return "Planet: " + name + " avrage radius " + radius + "km";
  }
}