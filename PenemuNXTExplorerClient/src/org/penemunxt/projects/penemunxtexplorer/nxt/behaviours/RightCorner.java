package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;

import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class RightCorner implements Behavior {
	SimpleNavigator simnav;
	DataShare DSL;
	NXTCommunication NXTC;

	public RightCorner(SimpleNavigator simnav, DataShare dsl,
			NXTCommunication nxtc) {
		this.simnav = simnav;
		this.DSL = dsl;
		this.NXTC = nxtc;
	}

	public void action() {
		DSL.lockBehaviour = true;
		NXTC.sendData(new RobotData(RobotData.POSITION_TYPE_RIGHT_CORNER,
				(int) simnav.getX(), (int) simnav.getY(), (int) simnav
						.getHeading(), 0, 0));

		simnav.rotate(-90);
		DSL.leftturnUsed();
		DSL.lockBehaviour = false;
	}

	public void suppress() {
		simnav.stop();
	}

	public boolean takeControl() {
		if (!DSL.lockBehaviour) {
			if (DSL.LatestRobotData.size() > 9 && DSL.sincelastturn > 5) {
				if ((DSL.LatestRobotData.get(4).getHeadDistance() - DSL.LatestRobotData
						.get(6).getHeadDistance()) > 200
						&& DSL.isLinear(DSL, 6, 9) != 0) {
					return true;
				} else
					return false;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
