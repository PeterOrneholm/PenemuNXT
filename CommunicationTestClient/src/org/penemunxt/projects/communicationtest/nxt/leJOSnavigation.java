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
	DataShare DSL;
	
	public leJOSnavigation(SimpleNavigator simnav, NXTCommunication NXTC,
			OpticalDistanceSensor ODS, DataShare DSL) {
		this.simnav = simnav;
		this.NXTC = NXTC;
		this.ODS = ODS;
		this.DSL = DSL;
	}

	@Override
	public void run() {
		UltrasonicSensor USS = new UltrasonicSensor(SensorPort.S3);
		TouchSensor TS = new TouchSensor(SensorPort.S4);
		
		simnav.setTurnSpeed(50);
		simnav.setMoveSpeed(500);
		
		Motor.A.rotate(90);
		Motor.A.setSpeed(50);

		Behavior b1 = (Behavior) new Forward(simnav);
		Behavior b2 = (Behavior) new AlignWall(simnav, DSL, NXTC);
		Behavior b3 = (Behavior) new RightCorner(simnav, DSL, NXTC);
	    Behavior b4 = (Behavior) new USSclose(simnav, USS, NXTC, DSL);
	    Behavior b5 = (Behavior) new Bumperclose (simnav, TS, NXTC, DSL);
	    //Behavior b4 = (Behavior) new FollowWall (simnav, ODS);
	    Behavior [] bArray = {b1, b2, b3, b4, b5};
	    Arbitrator arby = new Arbitrator(bArray);
	    arby.start();
	   
	    
	}
}
	