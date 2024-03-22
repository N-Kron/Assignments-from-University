package assignment4.solarsystem;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class that represents a menu for a Solar System program.
 */
public class ShowMenu {

  private ArrayList<Star> universe;
  private Scanner scanner;

  /**
   * Constructor for ShowMenu class that initializes the universe ArrayList and
   * the Scanner object.
   */
  public ShowMenu() {
    universe = FileEditor.readFromFile();
    scanner = new Scanner(System.in);
  }

  /**
   * Method that prints the main menu and reads user input to execute the selected
   * option.
   */
  public void menu() {
    boolean quit = false;
    while (!quit) {
      printMenu();
      String uchoice = scanner.nextLine();

      switch (uchoice) {
        case "1" -> createSolarSystem();
        case "2" -> listAllSolarSystems();
        case "3" -> quit = true;
        default -> System.out.println("Invalid input. Please try again.");
      }
    }
    assignment4.solarsystem.FileEditor.writeToFile(universe);
  }

  /**
   * Method that prints the main menu options.
   */
  private void printMenu() {
    System.out.println("****************************************");
    System.out.println("***                                  ***");
    System.out.println("*** Welcome to the Solarsystem Menu! ***");
    System.out.println("***                                  ***");
    System.out.println("****************************************");
    System.out.println("1 Create new solarsystem");
    System.out.println("2 List all solarsystems");
    System.out.println("3 Quit");
  }

  /**
   * Method that prompts the user to create a new solar system,
   * and reads user input to add the star and its planets and moons.
   */
  private void createSolarSystem() {
    Star star = null;
    boolean validInputSun = false;
    while (!validInputSun) {
      try {
        System.out.println("What is the name of the star: ");
        String name = scanner.nextLine();
        System.out.println("What is the radius of the star: ");
        int radius = Integer.parseInt(scanner.nextLine());
        star = new Star(name, radius);
        validInputSun = true;
      } catch (IllegalArgumentException e) {
        System.out.println("Invalid input. Please enter a value greater or equal to 16,700"
                + " and name can not be empty.");
      }
    }
    System.out.println("Star created!");
    addPlanets(star);
    universe.add(star);
  }

  /**
   * Method that prompts the user to add planets to a given star.
   *
   * @param star The star to which the planets are added.
   */
  private void addPlanets(Star star) {
    boolean addingPlanets = true;
    while (addingPlanets) {
      System.out.println("Do you want to add a planet?\n1 yes\n2 no");
      String wantToAddPlanet = scanner.nextLine();
      if (wantToAddPlanet.equals("1")) {
        String pname = "";
        boolean validInputPlanet = false;
        while (!validInputPlanet) {
          try {
            System.out.println("What is its name: ");
            pname = scanner.nextLine();
            System.out.println("What is the radius: ");
            int radius = Integer.parseInt(scanner.nextLine());
            System.out.println("What is the orbit radius: ");
            double orbitRadius = Double.parseDouble(scanner.nextLine());
            star.addPlanet(pname, radius, orbitRadius);
            validInputPlanet = true;
          } catch (IllegalArgumentException e) {
            System.out.println("Invalid input. Name can not be empty, radius has to be 2,000-200,000"
                    + " and orbit bigger than twice the radius of the sun.");
          }
        }

        System.out.println("Planet created!");
        addMoons(star, pname);
      } else {
        addingPlanets = false;
      }
    }
  }

  /**
   * Adds moons to a planet.
   *
   * @param star  the Star containing the planet to which to add moons
   * @param pname the name of the planet to which to add moons
   */
  private void addMoons(Star star, String pname) {
    boolean addingMoons = true;
    while (addingMoons) {
      System.out.println("Do you want to add a moon?\n1 yes\n2 no");
      String wantToAddMoon = scanner.nextLine();
      if (wantToAddMoon.equals("1")) {
        String mname = "";
        boolean validInputMoon = false;
        while (!validInputMoon) {
          try {
            System.out.println("What is its name: ");
            mname = scanner.nextLine();
            System.out.println("What is the radius: ");
            int radius = Integer.parseInt(scanner.nextLine());
            System.out.println("What is the orbit radius: ");
            double orbitRadius = Double.parseDouble(scanner.nextLine());
            Planet planet = star.findPlanet(pname);
            planet.addMoon(mname, radius, orbitRadius);
            validInputMoon = true;
          } catch (IllegalArgumentException e) {
            System.out.println("Invalid input. Name can not be empty, radius has to be 6-20,000"
                    + " and orbit bigger than twice the radius of the planet.");
          }
        }
        System.out.println("Moon created!");
      } else {
        addingMoons = false;
      }
    }
  }

  /**
   * Lists all solar systems and provides options to display them in different
   * orders, delete a body or add a planet/moon.
   */
  private void listAllSolarSystems() {
    for (assignment4.solarsystem.Star system : universe) {
      System.out.println(system.getName());
    }

    System.out.println("Enter the name of the star who's system you want to select");
    System.out.println("Or press ENTER to return to menu");
    String usystem = scanner.nextLine();
    if (!usystem.equals("")) {
      System.out.println("How would you like to display " + usystem);
      System.out.println("1 By size");
      System.out.println("2 By orbit radius");
      System.out.println("3 By hierarchical order");
      String uorder = scanner.nextLine();
      for (assignment4.solarsystem.Star system : universe) {
        if (system.getName().equals(usystem)) {
          assignment4.solarsystem.HeavenlyBody[] bodies = system.getHeavenlyBodies();
          sortHeavenlyBodies(bodies, uorder);
          for (assignment4.solarsystem.HeavenlyBody body : bodies) {
            if (body instanceof assignment4.solarsystem.Moon) {
              System.out.println("--" + body);
            } else if (body instanceof assignment4.solarsystem.Planet) {
              System.out.println("-" + body);
            } else {
              System.out.println(body);
            }
          }
          System.out.println("1 Delete Body");
          System.out.println("2 Add planet or moon");
          String uoperation = scanner.nextLine();
          if (uoperation.equals("1")) {
            System.out.println("What kind of body do you want to delete");
            System.out.println("1 The Star");
            System.out.println("2 Planet");
            System.out.println("3 Moon");
            String ubody = scanner.nextLine();
            if (ubody.equals("1")) {
              universe.remove(system);
            }
            if (ubody.equals("2")) {
              System.out.println("Enter the name of the planet to delete");
              String uname = scanner.nextLine();
              system.delPlanet(uname);
            }
            if (ubody.equals("3")) {
              System.out.println("Enter the name of the moon to delete");
              String uname = scanner.nextLine();
              System.out.println("Enter the name of the planet who's moon you are deleting");
              String unamePlanet = scanner.nextLine();
              system.delMoon(unamePlanet, uname);
            }
          }
          if (uoperation.equals("2")) {
            System.out.println("What kind of body do you want to add");
            System.out.println("1 Planet");
            System.out.println("2 Moon");
            String uadd = scanner.nextLine();
            if (uadd.equals("1")) {
              addPlanets(system);
            }
            if (uadd.equals("2")) {
              System.out.println("What planet do you want to add the moon to?");
              String pname = scanner.nextLine();
              addMoons(system, pname);
            }
          }
          break;
        }
      }
    }
  }

  /**
   * Sorts an array of HeavenlyBody objects based on the specified order.
   *
   * @param bodies the array of HeavenlyBody objects to be sorted
   * @param uorder the order in which to sort the HeavenlyBody objects
   *               "1" for sorting by average radius in descending order
   *               "2" for sorting by average orbit radius in ascending order
   */
  private void sortHeavenlyBodies(assignment4.solarsystem.HeavenlyBody[] bodies, String uorder) {
    int n = bodies.length;
    if (uorder.equals("1")) {
      for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
          if (bodies[j].getAvgRadiusInKm() < bodies[j + 1].getAvgRadiusInKm()) {
            // swap bodies[j] and bodies[j+1]
            assignment4.solarsystem.HeavenlyBody temp = bodies[j];
            bodies[j] = bodies[j + 1];
            bodies[j + 1] = temp;
          }
        }
      }
    }
    if (uorder.equals("2")) {
      int t = bodies.length;
      for (int i = 0; i < t - 1; i++) {
        for (int j = 0; j < t - i - 1; j++) {
          double o1 = bodies[j] instanceof Star ? 0 : ((bodies[j] instanceof Planet)
                  ? ((Planet) bodies[j]).getAvgOrbitRadiusInKm() : ((Moon) bodies[j]).getAvgOrbitRadiusInKm());
          double o2 = bodies[j + 1] instanceof Star ? 0 : ((bodies[j + 1] instanceof Planet) ? ((Planet)
                  bodies[j + 1]).getAvgOrbitRadiusInKm() : ((Moon) bodies[j + 1]).getAvgOrbitRadiusInKm());

          if (o1 < o2) {
            // swap bodies[j] and bodies[j+1]
            HeavenlyBody temp = bodies[j];
            bodies[j] = bodies[j + 1];
            bodies[j + 1] = temp;
          }
        }
      }
    }
  }

}