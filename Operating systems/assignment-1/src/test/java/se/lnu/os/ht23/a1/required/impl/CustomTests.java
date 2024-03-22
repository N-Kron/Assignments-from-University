package se.lnu.os.ht23.a1.required.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.lnu.os.ht23.a1.TestUtils;
import se.lnu.os.ht23.a1.provided.NapServer;
import se.lnu.os.ht23.a1.provided.data.RegistrarType;
import se.lnu.os.ht23.a1.provided.data.VisitEntry;
import se.lnu.os.ht23.a1.provided.impl.RegistrarFactoryImpl;
import se.lnu.os.ht23.a1.required.NapServerImpl;


public class CustomTests extends Thread {
  NapServer server;
	ArrayList<VisitEntry> toCheck;

	@BeforeEach
	public void createNewServer() {
		server = NapServerImpl.createInstance(RegistrarFactoryImpl.createRegistrar(RegistrarType.LABORIOUS));
		toCheck = new ArrayList<VisitEntry>();
	}
	

  // race conditions can occur when all hammocks are looped and they can both take one napper at the same time.

	@Test
	void checkManyElements() {
		
		System.out.println("======Starting checkSixElements=====");
		
		long startT = System.currentTimeMillis();
		double napTime = 1.0;

    for (int i = 0; i < 400; i++) {
      String name = "Cli " + i;
      VisitEntry v = createRequestAndEntry(name, napTime);
      int waitTime = i / 5;
      v.setWaitEndTime(v.getWaitEndTime()+waitTime);
		  v.setNapEndTime(v.getNapEndTime()+waitTime);
    }
			
		Assertions.assertTrue(System.currentTimeMillis()-startT<1000, "Creating the six elements and sleep 1 second each needed more than 1 second. It should be almost immediate");
		
		//Give some time to finish the first round
		try {
			Thread.sleep(200000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(server.getVisitRegistry().size(), 400, "At this point the five first clients should have finished, but not the sixth"); 
		
    try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    //System.out.println(toCheck);
    //System.out.println(server.getVisitRegistry());
		//assertTrue(TestUtils.checkEqual(server.getVisitRegistry(), toCheck));

	}

  @Test
  void check3Batches41Visits() {
    System.out.println("======Starting checkFiveElements=====");
		
		long startT = System.currentTimeMillis();
		double napTime = 1.0;

    createRequestAndEntry("Cli 1", 1.0);
    createRequestAndEntry("Cli 2", 1.1);
    createRequestAndEntry("Cli 3", 1.2);
    createRequestAndEntry("Cli 4", 1.3);
    createRequestAndEntry("Cli 5", 1.6);
    createRequestAndEntry("Cli 6", 1.0);
    createRequestAndEntry("Cli 7", 1.1);
    createRequestAndEntry("Cli 8", 1.2);
    createRequestAndEntry("Cli 9", 1.3);
    createRequestAndEntry("Cli 10", 1.6);
    createRequestAndEntry("Cli 11", 1.0);
    createRequestAndEntry("Cli 12", 1.1);
    createRequestAndEntry("Cli 13", 1.2);
    createRequestAndEntry("Cli 14", 1.3);
    createRequestAndEntry("Cli 15", 1.6);
    createRequestAndEntry("Cli 16", 1.2);
    try {
			Thread.sleep(6000);
		  } catch (InterruptedException e) {
		  	// TODO Auto-generated catch block
		  	e.printStackTrace();
		  }
    createRequestAndEntry("Cli 2-1", 1.0);
    createRequestAndEntry("Cli 2-2", 2.002);
    createRequestAndEntry("Cli 2-3", 3.004);
    createRequestAndEntry("Cli 2-4", 4.006);
    createRequestAndEntry("Cli 2-5", 5.008);
    createRequestAndEntry("Cli 2-6", 1.01);
    createRequestAndEntry("Cli 2-7", 2.012);
    createRequestAndEntry("Cli 2-8", 3.014);
    createRequestAndEntry("Cli 2-9", 4.016);
    createRequestAndEntry("Cli 2-10", 5.018);
    createRequestAndEntry("Cli 2-11", 1.02);
    createRequestAndEntry("Cli 2-12", 2.022);
    createRequestAndEntry("Cli 2-13", 3.024);
    createRequestAndEntry("Cli 2-14", 4.026);
    createRequestAndEntry("Cli 2-15", 5.028);
    createRequestAndEntry("Cli 2-16", 1.03);
    createRequestAndEntry("Cli 2-17", 2.032);
    createRequestAndEntry("Cli 2-18", 3.034);
    createRequestAndEntry("Cli 2-19", 4.036);
    createRequestAndEntry("Cli 2-20", 5.038);
    createRequestAndEntry("Cli 2-21", 1.04);
    createRequestAndEntry("Cli 2-22", 2.042);
    createRequestAndEntry("Cli 2-23", 3.044);
    createRequestAndEntry("Cli 2-24", 4.046);
    createRequestAndEntry("Cli 2-25", 5.048);

		try {
			Thread.sleep(21111);
		  } catch (InterruptedException e) {
		  	// TODO Auto-generated catch block
		  	e.printStackTrace();
		  }
		

		//Assertions.assertTrue(System.currentTimeMillis()-startT<2000, "Creating the five elements and sleep 1 second each needed more than 2 seconds. It should be around the 1.5 seconds of sleep");
		//Assertions.assertTrue(System.currentTimeMillis()-startT>900, "Creating the five elements and sleep 1 second each needed less than 900 milliseconds");

	System.out.println(server.getVisitRegistry());
	System.out.println(toCheck);
	assertTrue(TestUtils.checkEqual(server.getVisitRegistry(), toCheck));
    System.out.println(server.getVisitRegistry().size());
    assertEquals(server.getVisitRegistry().size(), toCheck.size());
  }

  @Test
  void IlleagalThreadStateExceptionTest() {
    createRequestAndEntry("Cli 1", 1.0);
    createRequestAndEntry("Cli 2", 1.1);
    createRequestAndEntry("Cli 3", 1.2);
    createRequestAndEntry("Cli 4", 1.3);
    createRequestAndEntry("Cli 5", 1.6);
    try {
			Thread.sleep(6000);
		  } catch (InterruptedException e) {
		  	// TODO Auto-generated catch block
		  	e.printStackTrace();
		  }
    createRequestAndEntry("Cli 2-1", 1.0);
    
    try {
			Thread.sleep(1000);
		  } catch (InterruptedException e) {
		  	// TODO Auto-generated catch block
		  	e.printStackTrace();
		  }

    assertTrue(server.getVisitRegistry().size() == 6);
  }


  @Test
  void checkLazyRegistrar() {
    System.out.println("======Starting checkFiveElements=====");

		SleepTester sleeptester = new SleepTester(this);
		sleeptester.start();
		
		long startT = System.currentTimeMillis();
		double napTime = 1.0;

		createRequestAndEntry("Cli 1", napTime);
		createRequestAndEntry("Cli 2", napTime+0.05);
		createRequestAndEntry("Cli 3", napTime+0.1);
		createRequestAndEntry("Cli 4", napTime+0.15);
		createRequestAndEntry("Cli 5", napTime+0.2);
		createRequestAndEntry("Cli 6", napTime+0.3);
		
		
		try {
			Thread.sleep(14000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(toCheck);
    System.out.println(server.getVisitRegistry());
		assertTrue(TestUtils.checkEqual(server.getVisitRegistry(), toCheck));
  }

	

	private VisitEntry createRequestAndEntry(String cliName, double napTime) {
		VisitEntry v = VisitEntry.createVisitEntry().setArrivalTime(System.currentTimeMillis()).setClientName(cliName)
				.setNapEndTime(System.currentTimeMillis() + (int) (napTime * 1000.0)).setNapTimeWanted(napTime)
				.setWaitEndTime(System.currentTimeMillis());
		
		server.newNapRequest(cliName, napTime);
		toCheck.add(v);
		return v;
	}
}
