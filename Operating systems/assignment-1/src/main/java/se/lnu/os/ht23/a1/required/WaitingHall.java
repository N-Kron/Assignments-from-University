package se.lnu.os.ht23.a1.required;

import java.util.LinkedList;

import se.lnu.os.ht23.a1.provided.data.VisitEntry;

public class WaitingHall {
  LinkedList<VisitEntry> queue = new LinkedList<>();

  public void addNapper (VisitEntry v) {
    queue.add(v);
  }

  public synchronized VisitEntry getNapper() {
    return queue.poll();
  }

  public synchronized boolean isEmpty() {
    return queue.isEmpty();
  }
}
