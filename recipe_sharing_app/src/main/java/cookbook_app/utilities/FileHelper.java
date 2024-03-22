package cookbook_app.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Helper to work with files.
 */
public class FileHelper {

  public FileHelper (String filename) {
    this.filename = filename;
  }

  private String filename;
  public void save(String data) {
    try {
      File file = new File(filename);
      if (file.createNewFile()) {
        System.out.println("File created: " + file.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    
    
    Writer fstream = null;
    try {
      fstream = new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8);
      fstream.write(data);
      fstream.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  
  public String load() {
    String data = "";
    try {
      File file = new File(filename);
      Scanner myReader = new Scanner(file, StandardCharsets.UTF_8);
      while (myReader.hasNextLine()) {
        data += myReader.nextLine() + "\n";
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("Load file was not found");
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    if (!data.isEmpty()) {
      data = data.substring(0, data.length() - 1);
    }
    return data;
  }
}
