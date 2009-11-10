package org.penemunxt.projects.penemunxtexplorer.nxt;
import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.nxt.behaviours.AlignWall;
import org.penemunxt.projects.penemunxtexplorer.nxt.behaviours.Bumperclose;
import org.penemunxt.projects.penemunxtexplorer.nxt.behaviours.Forward;
import org.penemunxt.projects.penemunxtexplorer.nxt.behaviours.RightCorner;
import org.penemunxt.projects.penemunxtexplorer.nxt.behaviours.TurnRight;
import org.penemunxt.projects.penemunxtexplorer.nxt.behaviours.USSclose;
import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;

import lejos.robotics.navigation.*;
import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.robotics.subsumption.*;


public class ExplorerNavigator extends Thread {
	SimpleNavigator simnav;
	NXTCommunication NXTC;
	OpticalDistanceSensor ODS;
	DataShare DSL;
	
	public ExplorerNavigator(SimpleNavigator simnav, NXTCommunication NXTC,
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
		
		simnav.setTurnSpeed(30);
		simnav.setMoveSpeed(350);
		
		Motor.A.rotate(90);
		Motor.A.setSpeed(50);

		Behavior b1 = new Forward(simnav, DSL);
		Behavior b1_5 = new TurnRight ( simnav, DSL, NXTC);
		Behavior b2 = new AlignWall(simnav, DSL, NXTC);
		Behavior b3 = new RightCorner(simnav, DSL, NXTC);
	    Behavior b4 = new USSclose(simnav, USS, NXTC, DSL);
	    Behavior b5 = new Bumperclose (simnav, TS, NXTC, DSL);
	    //Behavior b4 = (Behavior) new FollowWall (simnav, ODS);
	    //Behavior [] bArray = {b1, b2, b3, b4, b5};
	    Behavior [] bArray = {b1, b2, b4, b5};
	    Arbitrator arby = new Arbitrator(bArray);
	    arby.start();
	}
}
	