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
	
	final int MIN_NUMBER_OF_VALUES = 15;
	final int SINCELASTALIGN_THRESHOLD = 5;
	
	final int CORNER_STARTING_VALUE = 6;
	final int CORNER_ENDING_VALUE = 8;
	
	final int SCAN_DIF_MIN = 200;
	
	final int OLD_WALL_STARTING_VALUE = 8;
	final int OLD_WALL_ENDING_VALUE = 12;

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
			if (DSL.LatestRobotData.size() > MIN_NUMBER_OF_VALUES && DSL.sincelastturn > SINCELASTALIGN_THRESHOLD) {
				if ((DSL.LatestRobotData.get(CORNER_STARTING_VALUE).getHeadDistance() - DSL.LatestRobotData
						.get(CORNER_ENDING_VALUE).getHeadDistance()) > SCAN_DIF_MIN
						&& DSL.isLinear(DSL, OLD_WALL_STARTING_VALUE, OLD_WALL_ENDING_VALUE) != 0) {
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
