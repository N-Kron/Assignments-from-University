package assignmenttwo.solarsystem;

/**
 * A class representing a Star, which extends the HeavenlyBody class.
 * It contains a list of planets orbiting the star and provides methods to add
 * new planets
 * and moons to existing planets.
 */

public class Star extends HeavenlyBody {

  private Planet[] planets = new Planet[30];
  private int numberOfPlanets = 0;

  /**
   * A class representing a Star, which is a type of celestial body with a
   * relatively large radius.
   *
   * @param name   the name of the star.
   * @param radius the radius of the star in kilometers.
   * @throws IllegalArgumentException if the radius is less than 16,700 km.
   */
  public Star(String name, int radius) {
    super(name, radius);
    if (radius < 16700) {
      throw new IllegalArgumentException("Star can not be smaller than 16_700");
    }
  }

  public Star(Star star) {
    super(star.getName(), star.getAvgRadiusInKm());
    this.planets = star.planets;
  }

  /**
   * Adds a new planet to the array of planets in the solar system.
   *
   * @param planetName    the name of the planet to be added
   * @param avgRadiusInKm the average radius of the planet in kilometers
   * @param avgOrbitInKm  the average orbit radius of the planet in kilometers
   * @return the newly created planet object
   */

  public Planet addPlanet(String planetName, int avgRadiusInKm, double avgOrbitInKm) {
    Planet planet = new Planet(planetName, avgRadiusInKm, avgOrbitInKm);
    planets[this.numberOfPlanets] = planet;
    this.numberOfPlanets++;
    return planet;
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