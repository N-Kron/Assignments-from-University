package assignmentone;

/**
 * The Album class represents an album, containing information such as its name,
 * artist and year of release.
 * It also provides methods for getting and setting the attributes of an album,
 * as well as a toString() method
 * for displaying the album details.
 */
public class Album {
  private String name;
  private String artist;
  private int year;

  /**
   * Constructs an Album object with a name, artist and year of release.
   *
   * @param name   the name of the album
   * @param artist the name of the artist who released the album
   * @param year   the year the album was released
   */
  public Album(String name, int year, String artist) {
    setName(name);
    setArtist(artist);
    setYear(year);
  }

  public String getName() {
    return name;
  }

  /**
   * Sets the name of the album. If the name is less than 4 characters long, the
   * name will be set to "No name".
   *
   * @param name the name of the album
   */
  public void setName(String name) {
    if (name == null || name.length() < 4) {
      this.name = "No name";
    } else {
      this.name = name;
    }
  }

  public String getArtist() {
    return artist;
  }

  /**
   * Sets the name of the artist who released the album. If the artist name is
   * less than 4 characters long, the name will be set to "No artist".
   *
   * @param artist the name of the artist
   */
  public void setArtist(String artist) {
    if (artist == null || artist.length() < 4) {
      this.artist = "No artist";
    } else {
      this.artist = artist;
    }
  }

  public int getYear() {
    return year;
  }

  /**
   * Sets the year the album was released. If the year is less than 1800 or
   * greater than 2029, the year will be set to -1.
   *
   * @param year the year the album was released
   */
  public void setYear(int year) {
    if (year <= 1800 || year >= 2029) {
      this.year = -1;
    } else {
      this.year = year;
    }
  }

  public String toString() {
    return "Album: " + name + "\nArtist: " + artist + "\nYear: " + year;
  }
}