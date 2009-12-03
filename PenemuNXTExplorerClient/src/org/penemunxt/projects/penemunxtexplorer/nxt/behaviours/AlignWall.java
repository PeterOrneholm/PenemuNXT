package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;

import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class AlignWall implements Behavior {
	SimpleNavigator simnav;
	DataShare DSL;
	NXTCommunication NXTC;
	
	final int MIN_NUMBER_OF_VALUES = 9;
	final int SINCELASTALIGN_THRESHOLD = 10;
	final int TRAVELDIST_THRESHOLD = 5;
	
	final int STARTING_VALUE = 0;
	final int ENDING_VALUE = 3;

	final int SCAN_DIF_MIN = 10;
	
	final int SCAN_DISTANCE_MIN = 100;
	final int SCAN_DISTANCE_MAX = 300;
	
	public AlignWall(SimpleNavigator simnav, DataShare dsl,
			NXTCommunication NXTC) {
		this.simnav = simnav;
		this.DSL = dsl;
		this.NXTC = NXTC;
	}

	@Override
	public void action() {
		DSL.lockBehaviour = true;
		int angle = 0;

		if (DSL.LatestRobotData.size() > MIN_NUMBER_OF_VALUES) {
			NXTC.sendData(new RobotData(RobotData.POSITION_TYPE_ALIGNED,
					(int) simnav.getX(), (int) simnav.getY(), (int) simnav
							.getHeading(), 0, 0));

			if (DSL.isLinear(DSL, STARTING_VALUE, ENDING_VALUE) > 0) {
				angle = (int) ((180 / Math.PI) * (Math.atan(DSL.isLinear(DSL,
						0, ENDING_VALUE))));
			} else if (DSL.isLinear(DSL, STARTING_VALUE, ENDING_VALUE) < 0) {
				angle = (int) -((180 / Math.PI) * (Math.atan(-DSL.isLinear(DSL,
						0, ENDING_VALUE))));
			}

			simnav.rotate(-angle);
			DSL.alignUsed();
		}
		DSL.lockBehaviour = false;
	}

	@Override
	public void suppress() {
		simnav.stop();

	}

	@Override
	public boolean takeControl() {
		if (!DSL.lockBehaviour) {
			if (DSL.sincelastalign >= SINCELASTALIGN_THRESHOLD
					&& DSL.LatestRobotData.size() > MIN_NUMBER_OF_VALUES
					&& DSL.distancetraveled(DSL.LatestRobotData.get(STARTING_VALUE)
							.getPosX(), DSL.LatestRobotData.get(ENDING_VALUE).getPosX(),
							DSL.LatestRobotData.get(STARTING_VALUE).getPosY(),
							DSL.LatestRobotData.get(ENDING_VALUE).getPosY()) > TRAVELDIST_THRESHOLD
					&& DSL.sincelastturn >= SINCELASTALIGN_THRESHOLD) {
				if (DSL.LatestRobotData.get(ENDING_VALUE) != null) {
					if (Math.abs(DSL.LatestRobotData.get(STARTING_VALUE).getHeadDistance()
							- DSL.LatestRobotData.get(ENDING_VALUE).getHeadDistance()) > SCAN_DIF_MIN
							&& DSL.LatestRobotData.get(STARTING_VALUE).getHeadDistance() > SCAN_DISTANCE_MIN
							&& DSL.LatestRobotData.get(STARTING_VALUE).getHeadDistance() < SCAN_DISTANCE_MAX
							&& DSL.isLinear(DSL, STARTING_VALUE, ENDING_VALUE) != 0) {
						return true;
					} else
						return false;
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
