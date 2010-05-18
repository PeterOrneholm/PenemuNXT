package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;
import org.penemunxt.sensors.SensorRanges;

import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class USSclose implements Behavior {
	SimpleNavigator simnav;
	UltrasonicSensor USS;
	NXTCommunication NXTC;
	DataShare DS;

	final int DISTANCE_THRESHOLD = 30;

	final int NUMBER_OF_VALUES = 35;

	final int GROWING_THRESHOLD = 4;

	public USSclose(SimpleNavigator simnav, UltrasonicSensor USS,
			NXTCommunication NXTC, DataShare ds) {
		this.simnav = simnav;
		this.USS = USS;
		this.NXTC = NXTC;
		this.DS = ds;
	}

	@Override
	public void action() {
		if (USS.getDistance() < DISTANCE_THRESHOLD) {
			int a,b,c;
			a = USS.getDistance();
			b = DISTANCE_THRESHOLD;
			c = 0;
			if(a<b)
				c = 1;
			
			NXTC
					.sendData(new RobotData(
							RobotData.POSITION_TYPE_BUMP_DISTANCE, (int) simnav
									.getX(), (int) simnav.getY(), (int) simnav
									.getHeading(), 0, 0, 0, a, b, c, USS
									.getDistance()));
			simnav.stop();
			Motor.A.rotateTo(90);
			Motor.A.setSpeed(30);
			Motor.A.rotateTo(-90, false);
			int isgrowing = 0;
			int shortestdist = 1000;
			int shortestdistangle = 85;
			for (int x = NUMBER_OF_VALUES; x > 0; x--) {
				if (DS.LatestRobotData.get(x).getHeadDistance()
						- DS.LatestRobotData.get(x - 1).getHeadDistance() > 0) {
					isgrowing++;
				}

				if (isgrowing > GROWING_THRESHOLD) {
					if (DS.LatestRobotData.get(x).getHeadDistance() < shortestdist) {
						shortestdist = DS.LatestRobotData.get(x)
								.getHeadDistance();
						shortestdistangle = DS.LatestRobotData.get(x)
								.getHeadHeading();
					}
				}
			}
			Motor.A.setSpeed(200);
			Motor.A.rotateTo(90);
			if (DS.movestowardstargetPoint()) {
				simnav.rotate(DS.closestangle(90 - shortestdistangle, -90
						- shortestdistangle, DS));
			} else
				simnav.rotate(90 - shortestdistangle, false);
			DS.leftturnUsed();
			DS.alignUsed();
		}
	}

	@Override
	public void suppress() {
		simnav.stop();
	}

	@Override
	public boolean takeControl() {
		int distance = 255;
		try {
			if (DS.LatestRobotData != null && DS.LatestRobotData.get(0) != null)
				distance = DS.LatestRobotData.get(0).getUssDistance();
		} catch (Exception e) {

		}

		return (SensorRanges.checkUltrasonicDistanceRange(distance) && distance < DISTANCE_THRESHOLD);
	}

}
