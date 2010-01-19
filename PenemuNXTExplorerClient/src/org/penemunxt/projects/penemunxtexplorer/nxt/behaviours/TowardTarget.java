package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;

import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class TowardTarget implements Behavior {
	
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
		simnav.goTo(DS.TargetPos.x, DS.TargetPos.y);

	}

	@Override
	public void suppress() {
		simnav.stop();

	}

	@Override
	public boolean takeControl() {
		return DS.movestowardstargetPoint();
	}

}
