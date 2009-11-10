package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.projects.penemunxtexplorer.nxt.DataShare;

import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class Forward implements Behavior {

	SimpleNavigator comnav;
	boolean active;
	DataShare DS;

	public Forward(SimpleNavigator comnav, DataShare DS) {
		this.comnav = comnav;
		this.DS = DS;
	}

	@Override
	public void action() {
		comnav.forward();
		active = true;
	}

	@Override
	public void suppress() {
		comnav.stop();
		active = false;

	}

	@Override
	public boolean takeControl() {
		return true;
	}

}
