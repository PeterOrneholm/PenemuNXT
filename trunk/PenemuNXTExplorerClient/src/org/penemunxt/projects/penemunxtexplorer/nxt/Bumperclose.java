package org.penemunxt.projects.penemunxtexplorer.nxt;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.RobotData;

import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class Bumperclose implements Behavior {
	SimpleNavigator simnav;
	TouchSensor TS;
	NXTCommunication NXTC;
	DataShare DSL;

	public Bumperclose(SimpleNavigator simnav, TouchSensor TS, NXTCommunication NXTC, DataShare dsl) {
		this.simnav = simnav;
		this.TS = TS;
		this.NXTC = NXTC;
		this.DSL = dsl;
	}

	@Override
	public void action() {
		NXTC.sendData(new RobotData(RobotData.POSITION_TYPE_BUMP_BUMPER, (int) simnav.getX(), (int) simnav
				.getY(), (int) simnav.getHeading(), 0 , 0));
		
		simnav.backward();
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
		}
		simnav.stop();
		simnav.rotate(20);
		DSL.alignUsed();

	}

	@Override
	public void suppress() {
		simnav.stop();
	}

	@Override
	public boolean takeControl() {
		return TS.isPressed();
	}

}
