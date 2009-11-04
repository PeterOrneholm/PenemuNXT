package org.penemunxt.projects.communicationtest.nxt;
import lejos.nxt.Motor;
import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;



public class Forward implements Behavior {

	SimpleNavigator comnav;
	boolean active;


	public Forward(SimpleNavigator comnav) {
		this.comnav = comnav;

	}

	@Override
	public void action() {
		comnav.forward();
	    

	}

	@Override
	public void suppress() {
		comnav.stop();
		active = false;

	}

	@Override
	public boolean takeControl() {
		active = true;
		return true;
	}

}
