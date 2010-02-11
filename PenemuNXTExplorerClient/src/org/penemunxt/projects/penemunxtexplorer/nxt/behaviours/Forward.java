package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;

import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class Forward implements Behavior {

	SimpleNavigator simnav;
	boolean active;
	DataShare DS;

	public Forward(SimpleNavigator comnav, DataShare DS) {
		this.simnav = comnav;
		this.DS = DS;
	}

	@Override
	public void action() {
		simnav.forward();
		active = true;
	}

	@Override
	public void suppress() {
		simnav.stop();
		active = false;

	}

	@Override
	public boolean takeControl() {
		return true;
	}

}
