package assignmenttwo.refactoredsolitares;

/**
 * The Star class represents a celestial object that emits light and heat. It
 * extends the HeavenlyBody abstract class
 * and overrides the checkAvgRadiusInKm method to ensure that the average radius
 * is larger than 16,700 km.
 */

public class Star extends HeavenlyBody {

  /**
   * Constructs a Star object with the given name and radius.
   *
   * @param name   the name of the star
   * @param radius the radius of the star
   * @throws IllegalArgumentException if the radius is less than 16,700 km
   */
  public Star(String name, int radius) {
    super(name, radius);
    if (radius < 16_700) {
      throw new IllegalArgumentException("radius has to be between 6 and 10_000");
    }
  }

  @Override
  public void checkAvgRadiusInKm(int radius) throws IllegalArgumentException {
    if (radius < 16_700) {
      throw new IllegalArgumentException("Average radius must be larger than 16_700 for a star.");
    }
  }

  @Override
  public String toString() {
    return "Star: " + getName() + " avrage radius " + getAvgRadiusInKm() + "km";
  }
}