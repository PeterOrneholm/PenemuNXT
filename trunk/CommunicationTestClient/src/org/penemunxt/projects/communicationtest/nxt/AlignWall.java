package org.penemunxt.projects.communicationtest.nxt;

import org.penemunxt.communication.NXTCommunication;

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
		int angle;
		int ODSdif;
		int xposdif;
		int yposdif;
		double Traveldist;

		if (DSL.LatestRobotData.size() > 9) {
			ODSdif = DSL.LatestRobotData.get(3).getHeadDistance()
					- DSL.LatestRobotData.get(0).getHeadDistance();
			xposdif = DSL.LatestRobotData.get(3).getPosX()
					- DSL.LatestRobotData.get(0).getPosX();
			yposdif = DSL.LatestRobotData.get(3).getPosY()
					- DSL.LatestRobotData.get(0).getPosY();
			Traveldist = Math.sqrt((xposdif * xposdif) + (yposdif * yposdif));
			angle = (int) Math.atan(Traveldist / ODSdif);

			if (angle > 180) {
				angle = angle - 360;
			}
			simnav.rotate(angle);
			DSL.alignUsed();
		}
	}

	@Override
	public void suppress() {
		simnav.stop();

	}

	@Override
	public boolean takeControl() {

		if (DSL.sincelastalign >= 10 && DSL.LatestRobotData.size() > 9) {
			if (DSL.LatestRobotData.get(3) != null) {
				if (Math.abs(DSL.LatestRobotData.get(0).getHeadDistance()
						- DSL.LatestRobotData.get(3).getHeadDistance()) > 50
						&& DSL.LatestRobotData.get(0).getHeadDistance() > 100 && DSL.LatestRobotData.get(0).getHeadDistance() < 300) {
					return true;
				} else
					return false;
			} else
				return false;
		} else
			return false;
	}

}
