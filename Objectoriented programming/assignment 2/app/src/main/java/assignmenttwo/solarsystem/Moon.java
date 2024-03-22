package assignmenttwo.solarsystem;

/**
 * This is an abstract class representing a Heavenly Body, with a name and
 * average radius in kilometers.
 * It provides basic getters and setters for these properties, as well as a
 * method to check if the
 * average radius is within a valid range for the specific type of Heavenly
 * Body. It also implements
 * the Comparable interface to allow sorting by average radius, and provides a
 * toString method for
 * displaying information about the object.
 */

public class Moon extends HeavenlyBody {

  private double avgOrbitRadiusInKm;

  /**
   * Constructs a new Moon object with the given name, radius, and average orbit
   * radius around the Earth.
   *
   * @param name           the name of the Moon
   * @param radius         the radius of the Moon
   * @param avgOrbitRadius the average orbit radius of the Moon around the Earth
   *                       in kilometers
   * @throws IllegalArgumentException if the radius is not between 6-10,000 km or
   *                                  the orbit radius is less than 60 km
   */
  public Moon(String name, int radius, double avgOrbitRadius) {
    super(name, radius);
    if (radius < 6 || radius > 10_000 || avgOrbitRadius < 60) {
      throw new IllegalArgumentException(
          "Radius needs to be between 6 - 10_000 and orbit can not be less than 60");
    }
    this.avgOrbitRadiusInKm = avgOrbitRadius;
  }

  public Moon(Moon moon) {
    super(moon.getName(), moon.getAvgRadiusInKm());
  }

  public double getAvgOrbitRadiusInKm() {
    return this.avgOrbitRadiusInKm;
  }

  /**
   * Returns a copy of this HeavenlyBody object. The copy is obtained through
   * cloning the current object using
   * the Object.clone() method.
   *
   * @return a copy of this HeavenlyBody object
   * @throws AssertionError if the cloning is not supported by this object
   */
  public HeavenlyBody copy() {
    try {
      return (HeavenlyBody) this.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError("Unexpected CloneNotSupportedException");
    }
  }

  @Override
  public void checkAvgRadiusInKm(int radius) throws IllegalArgumentException {
    if (radius < 6 || radius > 10_000) {
      throw new IllegalArgumentException("Average radius must be between 6 and 10_000 km for a moon.");
    }
  }

  @Override
  public String toString() {
    return "Moon: " + getName() + ", average radius " + getAvgRadiusInKm() + " km";
  }
}
