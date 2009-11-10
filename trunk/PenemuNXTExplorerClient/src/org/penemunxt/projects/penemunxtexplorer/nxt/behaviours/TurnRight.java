package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;

import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class TurnRight implements Behavior {

	SimpleNavigator simnav;
	DataShare DSL;
	NXTCommunication NXTC;

	public TurnRight(SimpleNavigator simnav, DataShare dsl,
			NXTCommunication nxtc) {
		this.simnav = simnav;
		this.DSL = dsl;
		this.NXTC = nxtc;
	}

	@Override
	public void action() {
		DSL.lockBehaviour = true;
		simnav.rotate(-90);
		DSL.leftturnUsed();
		DSL.lockBehaviour = false;
	}

	@Override
	public void suppress() {
		simnav.stop();

	}

	@Override
	public boolean takeControl() {
		return (DSL.LatestRobotData.get(0).getHeadDistance() > 600 && DSL.sincelastturn > 20) && !DSL.lockBehaviour;
	}

}
