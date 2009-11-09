package org.penemunxt.projects.communicationtest.nxt;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.communicationtest.RobotData;

import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class AlignWall implements Behavior {
	SimpleNavigator simnav;
	DataShare DSL;
	NXTCommunication NXTC;

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

		if (DSL.LatestRobotData.size() > 9) {
			NXTC.sendData(new RobotData(RobotData.POSITION_TYPE_ALIGNED,
					(int) simnav.getX(), (int) simnav.getY(), (int) simnav
							.getHeading(), 0, 0));

			if (DSL.isLinear(DSL, 0, 3) > 0) {
				angle = (int) ((180 / Math.PI) * (Math.atan(DSL.isLinear(DSL,
						0, 3))));
			} else if (DSL.isLinear(DSL, 0, 3) < 0) {
				angle = (int) -((180 / Math.PI) * (Math.atan(-DSL.isLinear(DSL,
						0, 3))));
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
			if (DSL.sincelastalign >= 10
					&& DSL.LatestRobotData.size() > 9
					&& DSL.distancetraveled(DSL.LatestRobotData.get(0)
							.getPosX(), DSL.LatestRobotData.get(3).getPosX(),
							DSL.LatestRobotData.get(0).getPosY(),
							DSL.LatestRobotData.get(3).getPosY()) > 5
					&& DSL.sincelastturn >= 10) {
				if (DSL.LatestRobotData.get(3) != null) {
					if (Math.abs(DSL.LatestRobotData.get(0).getHeadDistance()
							- DSL.LatestRobotData.get(3).getHeadDistance()) > 10
							&& DSL.LatestRobotData.get(0).getHeadDistance() > 100
							&& DSL.LatestRobotData.get(0).getHeadDistance() < 300
							&& DSL.isLinear(DSL, 0, 3) != 0) {
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
