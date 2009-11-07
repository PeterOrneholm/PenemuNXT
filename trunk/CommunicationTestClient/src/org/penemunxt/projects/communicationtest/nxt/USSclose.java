package org.penemunxt.projects.communicationtest.nxt;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.communicationtest.RobotData;

import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class USSclose implements Behavior {
	SimpleNavigator simnav;
	UltrasonicSensor USS;
	NXTCommunication NXTC;
	DataShare DSL;

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

		simnav.rotate(90);
		DSL.alignUsed();
	}

	@Override
	public void suppress() {
		simnav.stop();
	}

	@Override
	public boolean takeControl() {
		return USS.getDistance() < 30;
	}

}
