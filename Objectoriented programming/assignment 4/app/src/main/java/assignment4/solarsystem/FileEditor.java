package assignment4.solarsystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * The FileEditor class provides methods to read from and write to the
 * Solarsystem.data file.
 * It contains a static method writeToFile which accepts an ArrayList of Star
 * objects and writes their HeavenlyBodies to the file.
 * It also has a static method readFromFile which reads the contents of the file
 * and returns an ArrayList of Star objects.
 * Additionally, the class has a private static method getIndent which is used
 * by the writeToFile method to determine the proper indentation for each
 * HeavenlyBody.
 * Any IOException that occurs during file read/write is caught and printed to
 * the console.
 */
public class FileEditor {
  /**
   * Writes an ArrayList of Star objects representing a solar system to a file in
   * the Solarsystem.data format.
   * Each HeavenlyBody object (including planets and moons) is written to the file
   * along with its indent level, determined by its type.
   *
   * @param universe ArrayList of Star objects representing a solar system
   */
  public static void writeToFile(ArrayList<Star> universe) {
    try (FileWriter fileWriter = new FileWriter("Solarsystem.data", StandardCharsets.UTF_8)) {
      for (Star solarsystem : universe) {
        for (HeavenlyBody heavenlyBody : solarsystem.getHeavenlyBodies()) {
          String indent = getIndent(heavenlyBody);
          fileWriter.write(indent + heavenlyBody.toString() + "\n");
        }
      }
    } catch (IOException e) {
      System.out.println("Error writing to file.");
      e.printStackTrace();
    }
  }

  /**
   * Reads the contents of the "Solarsystem.data" file and creates an ArrayList of
   * Star objects representing the universe.
   * The method first creates an empty ArrayList of Star objects and then reads
   * the lines of the file, one by one.
   * If the line starts with "-", it creates a new Planet object and adds it to
   * the current Star object in the ArrayList.
   * If the line starts with "--", it creates a new Moon object and adds it to the
   * last created Planet object in the ArrayList.
   * If the line doesn't start with "-" or "--", it creates a new Star object and
   * adds it to the ArrayList.
   *
   * @return an ArrayList of Star objects representing the universe
   */
  public static ArrayList<Star> readFromFile() {
    ArrayList<Star> universe = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader("Solarsystem.data", StandardCharsets.UTF_8))) {
      String line = reader.readLine();
      Star star = null;
      String pname = "";

      if (line != null) {
        String[] parts = line.split(":");
        String name = parts[0].replace("-", "");
        int radius = Integer.parseInt(parts[1]);
        star = new Star(name, radius);
        universe.add(star);
        line = reader.readLine();
      }
      while (line != null) {
        String[] parts = line.split(":");
        String name = parts[0].replace("-", "");
        int radius = Integer.parseInt(parts[1]);
        if (line.startsWith("--")) {
          double orbitRadius = Double.parseDouble(parts[2]);
          star.addMoon(name, pname, radius, orbitRadius);
        } else if (line.startsWith("-")) {
          double orbitRadius = Double.parseDouble(parts[2]);
          star.addPlanet(name, radius, orbitRadius);
          pname = name;
        } else {
          star = new Star(name, radius);
          universe.add(star);
        }
        line = reader.readLine();
      }
    } catch (IOException e) {
      System.out.println("Error while reading from file.");
      e.printStackTrace();
    }
    return universe;
  }

  private static String getIndent(HeavenlyBody heavenlyBody) {
    if (heavenlyBody instanceof Star) {
      return "";
    } else if (heavenlyBody instanceof Planet) {
      return "-";
    } else if (heavenlyBody instanceof Moon) {
      return "--";
    } else {
      return "";
    }
  }
}