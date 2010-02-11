package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;

import lejos.nxt.Sound;
import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class TowardTarget implements Behavior {

	static final int TARGET_DISTANCE_THRESHOLD = 10;

	SimpleNavigator simnav;
	DataShare DS;
	NXTCommunication NXTC;

	public TowardTarget(SimpleNavigator simnav, DataShare ds,
			NXTCommunication nxtc) {
		this.simnav = simnav;
		this.DS = ds;
		this.NXTC = nxtc;
	}

	@Override
	public void action() {
		//Sound.buzz();
		simnav.goTo(DS.TargetPos.x, DS.TargetPos.y);

	}

	@Override
	public void suppress() {
		simnav.stop();

	}

	@Override
	public boolean takeControl() {
		return (DS.movestowardstargetPoint() && DS.distancetraveled(
				(int) simnav.getX(), (int) DS.TargetPos.x, (int) simnav.getY(),
				(int) DS.TargetPos.y) > TARGET_DISTANCE_THRESHOLD);

	}

}
