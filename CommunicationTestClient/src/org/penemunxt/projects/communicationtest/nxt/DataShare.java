package org.penemunxt.projects.communicationtest.nxt;


import java.util.ArrayList;
import org.penemunxt.projects.communicationtest.RobotData;


public class DataShare {
	int sincelastalign = 0;
	// public RobotData LatestRobotData[];
	public ArrayList<RobotData> LatestRobotData;

	public DataShare() {
		super();
		LatestRobotData = new ArrayList<RobotData>();
	}

	public void alignUsed() {
		sincelastalign = 0;
	}

	public void addRobotData(RobotData RD) {
		LatestRobotData.add(0, RD);
		if (LatestRobotData.size() > 10) {
			LatestRobotData.remove(LatestRobotData.size() - 1);
		}

		sincelastalign++;
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
