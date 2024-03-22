package assignmenttwo.solarsystem;

/**
 * A class representing a planet, a type of heavenly body in a solar system.
 * Extends the abstract class HeavenlyBody.
 */

public class Planet extends HeavenlyBody {

  private double avrageOrbitRadiusInKm;
  private Moon[] moons;
  private int numberOfMoons = 0;

  /**
   * Creates a new Planet object with the specified name, radius, and average
   * orbit radius.
   *
   * @param name           name the name of the planet.
   * @param radius         radius the radius of the planet in kilometers.
   * @param avgOrbitRadius OrbitRadius the average orbit radius of the planet in
   *                       kilometers.
   */

  public Planet(String name, int radius, double avgOrbitRadius) {
    super(name, radius);
    if (radius < 2_000 || radius > 200_000 || avgOrbitRadius < 18_000) {
      throw new IllegalArgumentException(
          "Radius needs to be between 2_000 - 200_000 and orbit can not be less than 18_000");
    }
    this.avrageOrbitRadiusInKm = avgOrbitRadius;
    moons = new Moon[20];
  }

  public Planet(Planet planet) {
    super(planet.getName(), planet.getAvgRadiusInKm());
    this.avrageOrbitRadiusInKm = planet.avrageOrbitRadiusInKm;
  }

  /**
   * Adds a new moon to the current Moon object with the given name, average
   * radius in kilometers, and average orbit
   * radius around the Moon in kilometers.
   *
   * @param moonName      the name of the new moon
   * @param avgRadiusInKm the average radius of the new moon in kilometers
   * @param avgOrbitInKm  the average orbit radius of the new moon around the Moon
   *                      in kilometers
   * @return the new Moon object with the added moon
   * @throws IllegalArgumentException if the average radius of the new moon is
   *                                  larger than half of the parent planet's
   *                                  radius
   */

  public Moon addMoon(String moonName, int avgRadiusInKm, double avgOrbitInKm) {

    if (avgRadiusInKm > getAvgRadiusInKm() / 2) {
      throw new IllegalArgumentException("Moon radius cannot be larger than half of the parent planet's radius.");
    }

    Moon moon = new Moon(moonName, avgRadiusInKm, avgOrbitInKm);
    this.numberOfMoons++;
    int i = 0;
    while (i < moons.length && moons[i] != null) {
      i++;
    }
    if (i == moons.length) {
      throw new IllegalStateException("Cannot add more than " + moons.length + " moons.");
    }
    moons[i] = moon;

    return new Moon(moonName, avgRadiusInKm, avgOrbitInKm);

  }

  /**
   * Returns an array of the planet and its moons as HeavenlyBody objects. The
   * first element in the array is the planet
   * itself, and the remaining elements are the moons. Each moon is represented as
   * a new Moon object created from the
   * name, average radius, and average orbit radius of the moon.
   *
   * @return an array of HeavenlyBody objects representing the planet and its
   *         moons
   */
  public HeavenlyBody[] getHeavenlyBodies() {
    HeavenlyBody[] heavenlyCopies = new HeavenlyBody[numberOfMoons + 1];
    HeavenlyBody p = new Planet(this); // Add the Planet itself
    heavenlyCopies[0] = (HeavenlyBody) p;
    for (int i = 1; i <= numberOfMoons; i++) {
      heavenlyCopies[i] = new Moon(moons[i - 1].getName(), moons[i - 1].getAvgRadiusInKm(),
          moons[i - 1].getAvgOrbitRadiusInKm());
    }
    return heavenlyCopies;
  }

  public double getAvgOrbitRadiusInKm() {
    return this.avrageOrbitRadiusInKm;
  }

  public int getNumberOfMoons() {
    return this.numberOfMoons;
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