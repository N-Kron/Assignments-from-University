package se.lnu.os.ht23.a1.required.impl;

/**
 * testing whether the main thread is sleeping or not
 */
public class SleepTester extends Thread {
  CustomTests test;

  public SleepTester (CustomTests test) {
    this.test = test;
  }

  @Override
  public void run() {
    while (true) {
      if (test.getState() == Thread.State.TIMED_WAITING) {
        System.out.println("the main thread is sleeping");
      }
    }
  }
}
