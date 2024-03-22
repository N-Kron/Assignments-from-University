package assignmentone;

/**
 * The Time class provides a static method to convert time from hours, minutes
 * and seconds to total seconds.
 */
public class Time {
  /**
   * Converts time from hours, minutes and seconds to total seconds.
   *
   * @param hours   the number of hours
   * @param minutes the number of minutes
   * @param seconds the number of seconds
   * @return the total time in seconds
   */
  public int toSeconds(int hours, int minutes, int seconds) {
    int min = hours * 60;
    int sec = (min + minutes) * 60;
    return sec + seconds;
  }
}
