package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;

import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class AlignWall implements Behavior {
	SimpleNavigator simnav;
	DataShare DS;
	NXTCommunication NXTC;
	
	final int MIN_NUMBER_OF_VALUES = 9;
	final int SINCELASTALIGN_THRESHOLD = 10;
	final int TRAVELDIST_THRESHOLD = 5;
	
	final int STARTING_VALUE = 0;
	final int ENDING_VALUE = 2;

	final int SCAN_DIF_MIN = 10;
	
	final int SCAN_DISTANCE_MIN = 100;
	final int SCAN_DISTANCE_MAX = 400;
	
	public AlignWall(SimpleNavigator simnav, DataShare ds,
			NXTCommunication NXTC) {
		this.simnav = simnav;
		this.DS = ds;
		this.NXTC = NXTC;
	}

	@Override
	public void action() {
		DS.lockBehaviour = true;
		int angle = 0;

		if (DS.LatestRobotData.size() > MIN_NUMBER_OF_VALUES) {
			NXTC.sendData(new RobotData(RobotData.POSITION_TYPE_ALIGNED,
					(int) simnav.getX(), (int) simnav.getY(), (int) simnav
							.getHeading(), 0, 0, 0, 0, 0, 0, 0));

			if (DS.isLinear(DS, STARTING_VALUE, ENDING_VALUE) > 0) {
				angle = (int) ((180 / Math.PI) * (Math.atan(DS.isLinear(DS,
						0, ENDING_VALUE))));
			} else if (DS.isLinear(DS, STARTING_VALUE, ENDING_VALUE) < 0) {
				angle = (int) -((180 / Math.PI) * (Math.atan(-DS.isLinear(DS,
						0, ENDING_VALUE))));
			}

			simnav.rotate(-angle);
			DS.alignUsed();
		}
		DS.lockBehaviour = false;
	}

	@Override
	public void suppress() {
		simnav.stop();

	}

	@Override
	public boolean takeControl() {
		if (!DS.lockBehaviour) {
			if (DS.sincelastalign >= SINCELASTALIGN_THRESHOLD
					&& DS.LatestRobotData.size() > MIN_NUMBER_OF_VALUES
					&& DS.distancetraveled(DS.LatestRobotData.get(STARTING_VALUE)
							.getPosX(), DS.LatestRobotData.get(ENDING_VALUE).getPosX(),
							DS.LatestRobotData.get(STARTING_VALUE).getPosY(),
							DS.LatestRobotData.get(ENDING_VALUE).getPosY()) > TRAVELDIST_THRESHOLD
					&& DS.sincelastturn >= SINCELASTALIGN_THRESHOLD) {
				if (DS.LatestRobotData.get(ENDING_VALUE) != null) {
					if (Math.abs(DS.LatestRobotData.get(STARTING_VALUE).getHeadDistance()
							- DS.LatestRobotData.get(ENDING_VALUE).getHeadDistance()) > SCAN_DIF_MIN
							&& DS.LatestRobotData.get(STARTING_VALUE).getHeadDistance() > SCAN_DISTANCE_MIN
							&& DS.LatestRobotData.get(STARTING_VALUE).getHeadDistance() < SCAN_DISTANCE_MAX
							&& DS.isLinear(DS, STARTING_VALUE, ENDING_VALUE) != 0) {
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
