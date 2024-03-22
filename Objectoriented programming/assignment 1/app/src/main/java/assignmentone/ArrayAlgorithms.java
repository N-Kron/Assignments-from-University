package assignmentone;

import java.util.Random;

/**
 * The ArrayAlgorithms class provides various algorithms for working with
 * integer arrays.
 */
public class ArrayAlgorithms {

  /**
   * Calculates the average value of an integer array.
   *
   * @param arr the integer array to calculate the average of
   * @return the average value of the array as a double
   */
  public double average(int[] arr) {
    double sum = 0.0;
    for (int j : arr) {
      sum += j;
    }
    return sum / arr.length;
  }

  /**
   * Finds the maximum value in an integer array.
   *
   * @param arr the integer array to find the maximum value in
   * @return the maximum value in the array
   */
  public int maxValue(int[] arr) {
    int max = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (arr[i] > max) {
        max = arr[i];
      }
    }
    return max;
  }

  /**
   * Finds the index of the minimum value in an integer array.
   *
   * @param arr the integer array to find the minimum value in
   * @return the index of the minimum value in the array
   */
  public int minIndex(int[] arr) {
    int minIndex = 0;
    for (int i = 1; i < arr.length; i++) {
      if (arr[i] < arr[minIndex]) {
        minIndex = i;
      }
    }
    return minIndex;
  }

  /**
   * Shifts all elements in an integer array one position to the left.
   *
   * @param arr the integer array to shift
   * @return the shifted array
   */
  public int[] shift(int[] arr) {
    int temp = arr[0];
    for (int i = 1; i < arr.length; i++) {
      arr[i - 1] = arr[i];
    }
    arr[arr.length - 1] = temp;
    return arr;
  }

  /**
   * Randomly shuffles the elements in an integer array.
   *
   * @param arr the integer array to shuffle
   * @return the shuffled array
   */
  public int[] shuffle(int[] arr) {
    Random rand = new Random();
    for (int i = 0; i < arr.length; i++) {
      int randIndex = rand.nextInt(arr.length);
      int temp = arr[i];
      arr[i] = arr[randIndex];
      arr[randIndex] = temp;
    }
    return arr;
  }
}