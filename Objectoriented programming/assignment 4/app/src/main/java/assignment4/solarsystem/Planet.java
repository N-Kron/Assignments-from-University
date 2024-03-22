package assignment4.solarsystem;

import java.util.ArrayList;

/**
 * A class representing a planet, a type of heavenly body in a solar system.
 * Extends the abstract class HeavenlyBody.
 */

public class Planet extends HeavenlyBody {

  private final double avrageOrbitRadiusInKm;
  private ArrayList<Moon> moons = new ArrayList<>();

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
  }

  public Planet(Planet planet) {
    super(planet.getName(), planet.getAvgRadiusInKm());
    this.avrageOrbitRadiusInKm = planet.avrageOrbitRadiusInKm;
  }

  /**
   * Adds a new moon to the planet.
   *
   * @param moonName      the name of the moon.
   * @param avgRadiusInKm the avrage radius of the moon in kilometers.
   * @param avgOrbitInKm  the avrage orbit radius of the moon in kilometers.
   */

  public void addMoon(String moonName, int avgRadiusInKm, double avgOrbitInKm) {
    if (avgOrbitInKm < (2 * this.getAvgRadiusInKm())) {
      throw new IllegalArgumentException("Moon can not have orbit smaller than twice it of the planet");
    }
    Moon moon = new Moon(moonName, avgRadiusInKm, avgOrbitInKm);
    moons.add(moon);
  }

  /**
   * Returns an array of HeavenlyBody objects that includes both the Planet object
   * that this method is called on
   * and all of its Moons. The returned HeavenlyBody objects are copies of the
   * original ones to prevent
   * unintended modification of the original objects.
   *
   * @return an array of HeavenlyBody objects that includes the Planet object and
   *         all of its Moons
   */
  public HeavenlyBody[] getHeavenlyBodies() {
    HeavenlyBody[] heavenlyCopies = new HeavenlyBody[moons.size() + 1];
    HeavenlyBody p = new Planet(this); // Add the Planet itself
    heavenlyCopies[0] = p;
    int i = 1;
    for (Moon m : moons) {
      heavenlyCopies[i] = new Moon(m.getName(), m.getAvgRadiusInKm(),
          m.getAvgOrbitRadiusInKm());
      i++;
    }
    return heavenlyCopies;
  }

  /**
   * Removes the Moon with the given name from the list of moons orbiting this
   * Planet.
   *
   * @param name the name of the Moon to be removed.
   */
  public void delMoon(String name) {
    for (Moon m : moons) {
      if (name.equals(m.getName())) {
        moons.remove(m);
        break;
      }
    }
  }

  public double getAvgOrbitRadiusInKm() {
    return this.avrageOrbitRadiusInKm;
  }

  @Override
  public void checkAvgRadiusInKm(int radius) throws IllegalArgumentException {
    if (radius < 2000 || radius > 200000) {
      throw new IllegalArgumentException("Radius of planets must be between 2_000 km and 200_000 km");
    }
  }

  @Override
  public String toString() {
    return getName() + ":" + getAvgRadiusInKm() + ":" + getAvgOrbitRadiusInKm();
  }
}