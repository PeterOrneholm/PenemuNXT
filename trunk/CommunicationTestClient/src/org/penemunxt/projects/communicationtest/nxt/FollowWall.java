package org.penemunxt.projects.communicationtest.nxt;

import lejos.nxt.Motor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class FollowWall implements Behavior {

	OpticalDistanceSensor ODS;
	SimpleNavigator simnav;
	DataShare DS;
	boolean active;

	public FollowWall(SimpleNavigator simnav, OpticalDistanceSensor ods, DataShare DS) {
		this.ODS = ods;
		this.simnav = simnav;
		this.DS = DS;
	}

	public void action() {
		int oldODS = ODS.getDistance();
		while (active) {
			if (oldODS < ODS.getDistance()) {
				simnav.rotate(-5);
			} else if (oldODS > ODS.getDistance()) {
				simnav.rotate(5);
			}

			simnav.forward();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void suppress() {
		active = false;
		simnav.stop();

	}

	@Override
	public boolean takeControl() {
		active = true;
		return (ODS.getDistance() < 60 
				&& Motor.A.getTachoCount() < 160 && Motor.A.getTachoCount() > 20);
	}

}
