package assignment4.solarsystem;

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

  private final double avgOrbitRadiusInKm;

  /**
   * Creates a Moon object with the given name, radius, and average orbit radius.
   *
   * @param name           the name of the Moon.
   * @param radius         the radius of the Moon in kilometers.
   * @param avgOrbitRadius the average orbit radius of the Moon in kilometers.
   * @throws IllegalArgumentException if the radius is not between 6 and 10,000
   *                                  kilometers, or if the
   *                                  average orbit radius is less than 60
   *                                  kilometers.
   */
  public Moon(String name, int radius, double avgOrbitRadius) {
    super(name, radius);
    if (radius < 6 || radius > 10_000 || avgOrbitRadius < 60) {
      throw new IllegalArgumentException(
          "Radius needs to be between 6 - 10_000 and orbit can not be less than 60");
    }
    this.avgOrbitRadiusInKm = avgOrbitRadius;
  }

  public double getAvgOrbitRadiusInKm() {
    return this.avgOrbitRadiusInKm;
  }

  /**
   * Creates a copy of the current HeavenlyBody object using the clone() method.
   *
   * @return a copy of the current HeavenlyBody object
   * @throws AssertionError if the clone() method is not supported
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
    return getName() + ":" + getAvgRadiusInKm() + ":" + getAvgOrbitRadiusInKm();
  }
}
