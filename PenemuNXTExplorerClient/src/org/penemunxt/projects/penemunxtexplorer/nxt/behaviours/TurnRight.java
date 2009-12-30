package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;

import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class TurnRight implements Behavior {

	SimpleNavigator simnav;
	DataShare DS;
	NXTCommunication NXTC;

	public TurnRight(SimpleNavigator simnav, DataShare ds,
			NXTCommunication nxtc) {
		this.simnav = simnav;
		this.DS = ds;
		this.NXTC = nxtc;
	}

	@Override
	public void action() {
		DS.lockBehaviour = true;
		simnav.rotate(-90);
		DS.leftturnUsed();
		DS.lockBehaviour = false;
	}

	@Override
	public void suppress() {
		simnav.stop();

	}

	@Override
	public boolean takeControl() {
		return (DS.LatestRobotData.get(0).getHeadDistance() > 600 && DS.sincelastturn > 20) && !DS.lockBehaviour;
	}

}
