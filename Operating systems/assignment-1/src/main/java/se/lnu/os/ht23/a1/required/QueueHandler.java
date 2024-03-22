package se.lnu.os.ht23.a1.required;

import java.util.LinkedList;

import se.lnu.os.ht23.a1.provided.Registrar;
import se.lnu.os.ht23.a1.provided.data.VisitEntry;

public class QueueHandler extends Thread {
  LinkedList<VisitEntry> queue;
  Registrar r;
  Boolean isRunning;

  public QueueHandler(Registrar r, NapServerImpl server) {
    this.r = r;
    queue = server.queue;
  }

  //public synchronized void addVisitEntry (VisitEntry v) {
  //  queue.add(v);
  //  wakeUp();
  //}

  @Override
  public void run() {
    while (!queue.isEmpty()) {
      isRunning = true;
      r.addVisit(queue.poll());
    }
    isRunning = false;
  }

  //@Override
  //public void run() {
  //  
  //}
}
