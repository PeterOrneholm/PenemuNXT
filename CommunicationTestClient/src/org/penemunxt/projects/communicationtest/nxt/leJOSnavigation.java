package org.penemunxt.projects.communicationtest.nxt;
import org.penemunxt.communication.NXTCommunication;

import lejos.robotics.navigation.*;
import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.robotics.subsumption.*;


public class leJOSnavigation extends Thread {
	SimpleNavigator simnav;
	NXTCommunication NXTC;
	OpticalDistanceSensor ODS;
	
	public leJOSnavigation(SimpleNavigator simnav, NXTCommunication NXTC,
			OpticalDistanceSensor ODS) {
		this.simnav = simnav;
		this.NXTC = NXTC;
		this.ODS = ODS;
	}

	@Override
	public void run() {
		OpticalDistanceSensor ODS = new OpticalDistanceSensor(SensorPort.S2);
		UltrasonicSensor USS = new UltrasonicSensor(SensorPort.S3);
		TouchSensor TS = new TouchSensor(SensorPort.S4);
		
		simnav.setTurnSpeed(50);
		simnav.setMoveSpeed(500);

		Behavior b1 = (Behavior) new Forward(simnav);
	    Behavior b2 = (Behavior) new USSclose(simnav, USS, NXTC);
	    Behavior b3 = (Behavior) new Bumperclose (simnav, TS, NXTC);
	    Behavior [] bArray = {b1, b2, b3};
	    Arbitrator arby = new Arbitrator(bArray);
	    arby.start();
	}
}
	