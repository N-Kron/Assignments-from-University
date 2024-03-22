package se.lnu.os.ht23.a1.required;

import se.lnu.os.ht23.a1.provided.data.VisitEntry;

public class Hammock extends Thread {
  
  private VisitEntry napper;
  private boolean occupied;
  private WaitingHall hall;
  private int id;
  private NapServerImpl server;
  private boolean isDead;

  public Hammock(WaitingHall hall, int id, NapServerImpl server, boolean isDead) {
    this.hall = hall;
    this.id = id;
    this.server = server;
    this.isDead = isDead;
  }

  private void setNapper(VisitEntry v) {
    this.napper = v;
  }

  public boolean isOccupied() {
    return occupied;
  }

  public boolean isDead() {
    return isDead;
  }

  public int getHammockId() {
    return id;
  }

  //public void wakeUp() {
  //  System.out.println(id + " is woken up and hall is empty " + hall.isEmpty());
  //  if (!hall.isEmpty()) {
  //    setNapper(hall.getNapper());
  //    //occupied = true;
  //    napper.setWaitEndTime(System.currentTimeMillis());
  //    try {
  //      start();
  //    } catch (IllegalThreadStateException e) {
  //      System.out.println(id + " didnt start, just running");
  //      run();
  //    }
  //  }
  //}


  @Override
  public void run() {
    if (!hall.isEmpty()) {
      setNapper(hall.getNapper());
      occupied = true;
      napper.setWaitEndTime(System.currentTimeMillis());
      long duration = (long) (napper.getNapTimeWanted() * 1000); 
      try {
        sleep(duration);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      napper.setNapEndTime(System.currentTimeMillis());
      server.queue.add(napper);
      server.wakeUp();
      //addVisit(napper);
      //new LazyRegistrarAbuser(r, napper);
      run();
      isDead = true;
    }
  }

}