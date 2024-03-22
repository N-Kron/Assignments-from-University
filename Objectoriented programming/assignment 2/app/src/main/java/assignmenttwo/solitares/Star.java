package assignmenttwo.solitares;

/**
 * The Star class represents a star with a name and average radius in
 * kilometers.
 * The radius of the star must be greater than 16,700 km.
 */

public class Star {

  private String name;
  private int radius;

  public Star(String name, int radius) {
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
    if (radius < 16_700) {
      throw new IllegalArgumentException("Radius of star must be bigger than 16_700 km");
    }
    this.radius = radius;
  }

  public String getName() {
    return this.name;
  }

  public int getAvgRadiusInKm() {
    return this.radius;
  }

  public String toString() {
    return "Star: " + name + " average radius " + radius + "km";
  }
}
