package assignment4.solarsystem;

import java.util.ArrayList;

/**
 * A class representing a Star, which extends the HeavenlyBody class.
 * It contains a list of planets orbiting the star and provides methods to add
 * new planets
 * and moons to existing planets.
 */

public class Star extends HeavenlyBody {

  private ArrayList<Planet> planets = new ArrayList<>();

  /**
   * Constructs a new Star with the given name and radius. The radius of the star
   * must be within the
   * range of 16,700 to 7,000,000 kilometers, otherwise an
   * IllegalArgumentException is thrown.
   *
   * @param name   the name of the new Star.
   * @param radius the radius of the new Star.
   * @throws IllegalArgumentException if the radius is smaller than 16,700 or
   *                                  bigger than 7,000,000.
   */

  public Star(String name, int radius) {
    super(name, radius);
    if (radius < 16700 || radius > 7_000_000) {
      throw new IllegalArgumentException("Star can not be smaller than 16_700 nor bigger than 7,000,000");
    }
  }

  public Star(Star star) {
    super(star.getName(), star.getAvgRadiusInKm());
    this.planets = star.planets;
  }

  /**
   * Adds a new planet to the list of planets orbiting this star.
   *
   * @param planetName    the name of the planet to be added.
   * @param avgRadiusInKm the average radius of the planet in kilometers.
   * @param avgOrbitInKm  the average orbit radius of the planet in kilometers.
   */

  public void addPlanet(String planetName, int avgRadiusInKm, double avgOrbitInKm) {
    if (avgOrbitInKm < (2 * this.getAvgRadiusInKm())) {
      throw new IllegalArgumentException("Planet can not have orbit smaller than twice it of the star");
    }
    Planet planet = new Planet(planetName, avgRadiusInKm, avgOrbitInKm);
    planets.add(planet);
  }

  /**
   * Adds a moon to one of the stars orbiting planets.
   *
   * @param moonName              the name of the moon to be added.
   * @param planetName            the name of the planet the moon is going to be
   *                              added to.
   * @param radius                the radius of the moon.
   * @param avrageOrbitRadiusInKm the average orbit radius of the moon.
   */

  public void addMoon(String moonName, String planetName, int radius, double avrageOrbitRadiusInKm) {
    for (Planet planet : planets) {
      if (planet != null && planet.getName().equals(planetName)) {
        planet.addMoon(moonName, radius, avrageOrbitRadiusInKm);
        break;
      }
    }
  }

  /**
   * returns all planets and their respective moons as an array.
   *
   * @return returns the array with all heavenly bodies.
   */

  public HeavenlyBody[] getHeavenlyBodies() {
    int totalLength = 1;
    for (Planet p : planets) {
      if (p == null) {
        break;
      }
      totalLength += p.getHeavenlyBodies().length;
    }
    HeavenlyBody[] heavenlyArray = new HeavenlyBody[totalLength];
    int i = 0;
    Star s = new Star(this);
    heavenlyArray[i] = s; // Add the Star itself
    i++;
    for (Planet p : planets) {
      if (p == null) {
        break;
      }
      HeavenlyBody[] bodies = p.getHeavenlyBodies();
      for (HeavenlyBody body : bodies) {
        heavenlyArray[i] = body;
        i++;
      }
    }
    return heavenlyArray;
  }

  /**
   * Removes the planet with the given name from the list of planets orbiting this
   * star.
   *
   * @param pname the name of the planet to be removed.
   */
  public void delPlanet(String pname) {
    for (Planet p : planets) {
      if (pname.equals(p.getName())) {
        planets.remove(p);
        break;
      }
    }
  }

  /**
   * Removes the moon with the given name from the planet with the given name.
   *
   * @param pname the name of the planet to which the moon belongs.
   * @param mname the name of the moon to be removed.
   */
  public void delMoon(String pname, String mname) {
    for (Planet p : planets) {
      if (pname.equals(p.getName())) {
        p.delMoon(mname);
        break;
      }
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
    return getName() + ":" + getAvgRadiusInKm();
  }

  /**
   * Searches for a planet with the given name in the list of planets and returns
   * it.
   * If no planet is found with the given name, returns null.
   *
   * @param pname the name of the planet to search for
   * @return the planet with the given name, or null if not found
   */
  public Planet findPlanet(String pname) {
    for (Planet p : planets) {
      if (p.getName().equals(pname)) {
        return p;
      }
    }
    return null;
  }
}