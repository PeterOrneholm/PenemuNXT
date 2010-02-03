package org.penemunxt.projects.penemunxtexplorer.nxt;
import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.nxt.behaviours.AlignWall;
import org.penemunxt.projects.penemunxtexplorer.nxt.behaviours.Bumperclose;
import org.penemunxt.projects.penemunxtexplorer.nxt.behaviours.Forward;
import org.penemunxt.projects.penemunxtexplorer.nxt.behaviours.Initialization;
import org.penemunxt.projects.penemunxtexplorer.nxt.behaviours.RightCorner;
import org.penemunxt.projects.penemunxtexplorer.nxt.behaviours.TowardTarget;
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
	DataShare DS;
	
	
	public ExplorerNavigator(SimpleNavigator simnav, NXTCommunication NXTC, DataShare DS) {
		this.simnav = simnav;
		this.NXTC = NXTC;
		this.DS = DS;
	}

	@Override
	public void run() {
		UltrasonicSensor USS = new UltrasonicSensor(SensorPort.S3);
		TouchSensor TS = new TouchSensor(SensorPort.S4);
		

		Behavior b1 = new Forward(simnav, DS);
		Behavior b2 = new Initialization (simnav, DS, NXTC);
		Behavior b1_5 = new TurnRight ( simnav, DS, NXTC);
		Behavior b3 = new AlignWall(simnav, DS, NXTC);
		Behavior b4 = new RightCorner(simnav, DS, NXTC);
		Behavior b4_5 = new TowardTarget (simnav, DS, NXTC);
	    Behavior b5 = new USSclose(simnav, USS, NXTC, DS);
	    Behavior b6 = new Bumperclose (simnav, TS, NXTC, DS);
	    //Behavior b4 = (Behavior) new FollowWall (simnav, ODS);
	    //Behavior [] bArray = {b1, b2, b3, b4, b5};
	    Behavior [] bArray = {b1, b2, b3, b4, b4_5, b5, b6};
	    Arbitrator arby = new Arbitrator(bArray);
	    arby.start();
	}
}
