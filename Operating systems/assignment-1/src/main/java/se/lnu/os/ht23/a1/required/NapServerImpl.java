package se.lnu.os.ht23.a1.required;

import java.util.LinkedList;
import java.util.List;

import se.lnu.os.ht23.a1.provided.NapServer;
import se.lnu.os.ht23.a1.provided.Registrar;
import se.lnu.os.ht23.a1.provided.data.VisitEntry;

public class NapServerImpl implements NapServer {

  private NapServerImpl(Registrar r) {
    registrar = r;
  }

  LinkedList<Hammock> hammocks = new LinkedList<>();
  Registrar registrar;

  QueueHandler handler;

  WaitingHall hall = new WaitingHall();

  LinkedList<VisitEntry> queue = new LinkedList<>();

  public static NapServer createInstance(Registrar r) {
    NapServer n = (new NapServerImpl(r)).initialize();
    return n;
  }

  private NapServer initialize() {

    //TODO You have to write this method to initialize your server:
    //For instance, create the Hammocks, the waiting hall, etc.
    handler = new QueueHandler(registrar, this);
    handler.start();
    for (int i = 0; i < 5; i++) {
      Hammock hammock = new Hammock(hall, i, this, true);
      this.hammocks.add(hammock);
      hammock.start();
    }

    return this;
  }

  @Override
  public List<VisitEntry> getVisitRegistry() {
    return registrar.getVisitRegistry();
  }

  @Override
  public void newNapRequest(String clientName, double napDuration) {
    VisitEntry v = VisitEntry.createVisitEntry();
    v.setClientName(clientName);
    v.setArrivalTime(System.currentTimeMillis());
    v.setNapTimeWanted(napDuration);
    hall.addNapper(v);
    for (int i = 0; i < hammocks.size(); i++) {
      if (!hammocks.get(i).isAlive()) {
        int id = hammocks.get(i).getHammockId();
        hammocks.set(i, new Hammock(hall, id, this, false));
        hammocks.get(i).start();
      }
    }
    //registrar.addVisit(v);
  }

  public void wakeUp() {
    if (!handler.isRunning) {
      synchronized(this) {
        handler.run();
      }
    }
  }


  @Override
  public void stop() {
    while (!hammocks.isEmpty()) {
      hammocks.removeIf(hammock -> !hammock.isOccupied());
    }
  }
}
