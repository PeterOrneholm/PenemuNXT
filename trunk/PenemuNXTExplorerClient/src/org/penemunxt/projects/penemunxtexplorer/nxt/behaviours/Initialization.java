package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;

import lejos.nxt.Motor;
import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class Initialization implements Behavior {
	SimpleNavigator simnav;
	DataShare DS;
	NXTCommunication NXTC;
	
	boolean takeControl = true;

	public Initialization(SimpleNavigator simnav, DataShare ds,
			NXTCommunication NXTC) {
		
		this.simnav = simnav;
		this.DS = ds;
		this.NXTC = NXTC;
	}
	
	public void giveControl(){
		takeControl = true;
	}
	@Override
	public void action() {
		
		simnav.setTurnSpeed(30);
		simnav.setMoveSpeed(50);
		DS.stopSendData();
		
		Motor.A.setSpeed(30);
		Motor.A.rotate(360, false);
		
		int shortestdist = 1000;
		int shortestdistangle = 0;
		
		for (int x = (DS.LatestRobotData.size()-1); x > 0; x--) {
				if ( DS.LatestRobotData.get(x).getHeadDistance() < shortestdist){
					shortestdist = DS.LatestRobotData.get(x).getHeadDistance();
					shortestdistangle = DS.LatestRobotData.get(x).getHeadHeading();
				}	
		}
		
		
		Motor.A.setSpeed(200);
		Motor.A.rotate(-270);
		Motor.A.setSpeed(50);
		
		if ( shortestdistangle < 180 )simnav.rotate(shortestdistangle);
		else simnav.rotate (shortestdistangle - 360);
		
		DS.startSendData();
		takeControl = false;

	}

	@Override
	public void suppress() {
		simnav.stop();

	}

	@Override
	public boolean takeControl() {
		return takeControl;
	}

}
