package assignmenttwo.solitares;

/**
 * A class representing a moon object with a name and radius in kilometers.
 * The name cannot be null or empty and the radius must be between 6 km and
 * 10,000 km.
 */
public class Moon {

  private String name;
  private int radius;

  public Moon(String name, int radius) {
    setName(name);
    setRadius(radius);
  }

  private void setName(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
    this.name = name;
  }

  private void setRadius(int radius) {
    if (radius < 6 || radius > 10_000) {
      throw new IllegalArgumentException("Radius of moons must be between 6 km and 10_000 km");
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
    return "Moon: " + name + ", average radius " + radius + " km";
  }
}
