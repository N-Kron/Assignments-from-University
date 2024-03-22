package assignmentone;

/**
 * The AlbumGenerator class contains a single method that generates an array of
 * Album objects.
 * The generated array contains four instances of Album objects with pre-defined
 * attributes.
 */
public class AlbumGenerator {
  /**
   * Generates an array of Album objects with pre-defined attributes.
   *
   * @return an array of Album objects
   */
  public Album[] generateAlbums() {
    Album[] albums = new Album[4];

    albums[0] = new Album("Dark Side of the Moon", 1973, "Pink Floyd");
    albums[1] = new Album("Wish You Were Here", 1975, "Pink Floyd");
    albums[2] = new Album("Animals", 1977, "Pink Floyd");
    albums[3] = new Album("The Wall", 1979, "Pink Floyd");

    return albums;
  }

  /**
   * The main method generates an array of Album objects using the
   * generateAlbums() method,
   * and prints out the name of the album, its release year and the artist who
   * released it.
   */
  public void main(String[] args) {
    Album[] albums = generateAlbums();

    for (Album album : albums) {
      System.out.println(album.getName() + " was released in " + album.getYear() + " by " + album.getArtist());
    }
  }
}