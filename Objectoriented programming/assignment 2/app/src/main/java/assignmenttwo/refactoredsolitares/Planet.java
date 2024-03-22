package assignmenttwo.refactoredsolitares;

/**
 * The `Planet` class represents a planet with a name and an average radius in
 * kilometers.
 * This class extends the `HeavenlyBody` class and overrides its
 * `checkAvgRadiusInKm()`
 * method to enforce specific constraints on the average radius of a planet.
 * The `Planet` class provides a constructor to set the name and average radius
 * of the planet, as well as a `toString()` method to generate a string
 * representation of the planet.
 */

public class Planet extends HeavenlyBody {
  /**
   * Represents a planet, a type of heavenly body that orbits around a star.
   *
   * @param name   the name of the planet
   * @param radius the radius of the planet in kilometers
   * @throws IllegalArgumentException if the radius is not between 2,000 and
   *                                  200,000 kilometers
   */
  public Planet(String name, int radius) {
    super(name, radius);
    if (radius < 2_000 || radius > 200_000) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
  }

  @Override
  public void checkAvgRadiusInKm(int radius) throws IllegalArgumentException {
    if (radius < 2000 || radius > 200000) {
      throw new IllegalArgumentException("Radius of planets must be between 2_000 km and 200_000 km");
    }
  }

  @Override
  public String toString() {
    return "Planet: " + getName() + ", average radius " + getAvgRadiusInKm() + " km";
  }
}