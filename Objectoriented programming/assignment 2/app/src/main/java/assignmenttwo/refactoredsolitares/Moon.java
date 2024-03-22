package assignmenttwo.refactoredsolitares;

/**
 * The `Moon` class represents a moon with a name and an average radius in
 * kilometers.
 * This class extends the `HeavenlyBody` class and overrides its
 * `checkAvgRadiusInKm()`
 * method to enforce specific constraints on the average radius of a moon.
 * The `Moon` class provides a constructor to set the name and average radius of
 * the moon,
 * as well as a `toString()` method to generate a string representation of the
 * moon.
 */

public class Moon extends HeavenlyBody {

  /**
   * A Moon is a natural satellite that orbits a planet. It extends the
   * HeavenlyBody class and adds the specific
   * properties and behavior of a Moon.
   *
   * @param name   the name of the Moon
   * @param radius the radius of the Moon in kilometers
   * @throws IllegalArgumentException if the radius is not between 6 and 10,000
   *                                  (inclusive)
   */
  public Moon(String name, int radius) {
    super(name, radius);
    if (radius < 6 || radius > 10_000) {
      throw new IllegalArgumentException("radius has to be between 6 and 10_000");
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
