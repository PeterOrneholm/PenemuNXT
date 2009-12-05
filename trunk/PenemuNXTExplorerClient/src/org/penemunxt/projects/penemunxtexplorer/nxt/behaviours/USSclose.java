package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;

import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;





public class USSclose implements Behavior {
	SimpleNavigator simnav;
	UltrasonicSensor USS;
	NXTCommunication NXTC;
	DataShare DSL;
	
	final int DISTANCE_THRESHOLD = 30;
	
	final int NUMBER_OF_VALUES = 10;

	public USSclose(SimpleNavigator simnav, UltrasonicSensor USS,
			NXTCommunication NXTC, DataShare dsl) {
		this.simnav = simnav;
		this.USS = USS;
		this.NXTC = NXTC;
		this.DSL = dsl;
	}

	@Override
	public void action() {
		NXTC.sendData(new RobotData(RobotData.POSITION_TYPE_BUMP_DISTANCE,
				(int) simnav.getX(), (int) simnav.getY(), (int) simnav
						.getHeading(), 0, 0));
		
		//set slow rotate
		Motor.A.rotate(-90);
		
		boolean isgrowing = false;
		int shortestdist = 1000;
		int shortestdistangle = 0;
		
		for (int x = NUMBER_OF_VALUES; x > 0; x--) {
			if (DSL.LatestRobotData.get(x).getHeadDistance()
					- DSL.LatestRobotData.get(x - 1).getHeadDistance() > 0) {
				isgrowing = true;
			}
			
			if (isgrowing) {
				if ( DSL.LatestRobotData.get(x).getHeadDistance() < shortestdist){
					shortestdist = DSL.LatestRobotData.get(x).getHeadDistance();
					shortestdistangle = DSL.LatestRobotData.get(x).getHeadHeading();
				}	
			}
		}
		
		//set fast rotate
		Motor.A.rotate(90);
		simnav.rotate(90-shortestdistangle);
		
		
		DSL.leftturnUsed();
		DSL.alignUsed();
	}

	@Override
	public void suppress() {
		simnav.stop();
	}

	@Override
	public boolean takeControl() {
		return (USS.getDistance() < DISTANCE_THRESHOLD);
	}

}
