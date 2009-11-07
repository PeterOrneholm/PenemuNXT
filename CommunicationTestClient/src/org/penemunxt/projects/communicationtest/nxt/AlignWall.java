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
			Traveldist = distancetraveled(DSL.LatestRobotData.get(0).getPosX(),
					DSL.LatestRobotData.get(3).getPosX(), DSL.LatestRobotData
							.get(0).getPosY(), DSL.LatestRobotData.get(3)
							.getPosY());
			if (ODSdif <= 0) {
				angle = (int) (Math.atan(Traveldist / ODSdif) * (180 / Math.PI));
			} else {
				angle = (int) -(Math.atan(Traveldist / (-ODSdif)) * (180 / Math.PI));
			}
			while (angle > 180) {
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
						- DSL.LatestRobotData.get(3).getHeadDistance()) > 10
						&& DSL.LatestRobotData.get(0).getHeadDistance() > 100
						&& DSL.LatestRobotData.get(0).getHeadDistance() < 600 && isLinear(DSL, 0, 3)) {
					return true;
				} else
					return false;
			} else
				return false;
		} else
			return false;
	}

	public boolean isLinear(DataShare dsl, int startpos, int endpos) {

		double coef = ((dsl.LatestRobotData.get(startpos).getHeadDistance() - dsl.LatestRobotData
				.get(endpos).getHeadDistance()) / distancetraveled(
				dsl.LatestRobotData.get(startpos).getPosX(),
				dsl.LatestRobotData.get(endpos).getPosX(), dsl.LatestRobotData
						.get(startpos).getPosY(), dsl.LatestRobotData.get(
						endpos).getPosY()));

		for (int n = startpos; n < endpos; n++) {
			if (!((dsl.LatestRobotData.get(n).getHeadDistance() - dsl.LatestRobotData
					.get(n + 1).getHeadDistance())
					/ distancetraveled(dsl.LatestRobotData.get(n).getPosX(),
							dsl.LatestRobotData.get(n + 1).getPosX(),
							dsl.LatestRobotData.get(n).getPosY(),
							dsl.LatestRobotData.get(n + 1).getPosY()) < (coef) - 0.2)
					|| (dsl.LatestRobotData.get(n).getHeadDistance() - dsl.LatestRobotData
							.get(n + 1).getHeadDistance())
							/ distancetraveled(dsl.LatestRobotData.get(n)
									.getPosX(), dsl.LatestRobotData.get(n + 1)
									.getPosX(), dsl.LatestRobotData.get(n)
									.getPosY(), dsl.LatestRobotData.get(n + 1)
									.getPosY()) > (coef) + 0.2) return true;
				
		}

		return false;
	}

	public double distancetraveled(int x1, int x2, int y1, int y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2) + ((y1 - y2) * (y1 - y2))));
	}
}
