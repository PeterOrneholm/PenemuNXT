package org.penemunxt.projects.penemunxtexplorer.nxt;

import java.util.ArrayList;
import org.penemunxt.projects.penemunxtexplorer.RobotData;

public class DataShare {
	public int sincelastalign = 0;
	public int sincelastturn = 0;
	public boolean lockBehaviour;
	public ArrayList<RobotData> LatestRobotData;

	public DataShare() {
		super();
		LatestRobotData = new ArrayList<RobotData>();
	}

	public void addRobotData(RobotData RD) {
		LatestRobotData.add(0, RD);
		if (LatestRobotData.size() > 10) {
			LatestRobotData.remove(LatestRobotData.size() - 1);
		}

		sincelastalign++;
		sincelastturn++;
	}
	
	public double isLinear(DataShare dsl, int startpos, int endpos) {

		double coef = linCoef ( dsl, startpos, endpos);

		for (int n = startpos; n < endpos; n++) {
			if (!((linCoef ( dsl, n, (n+1)) / coef) > 1.1 
					||  ((linCoef ( dsl, n, (n+1)) / coef) < 0.9))) return coef;
				
		}

		return 0;
	}
	
	public double linCoef ( DataShare dsl, int startpos, int endpos){
		return ((dsl.LatestRobotData.get(startpos).getHeadDistance() - dsl.LatestRobotData
				.get(endpos).getHeadDistance()) / distancetraveled(
						dsl.LatestRobotData.get(startpos).getPosX(),
						dsl.LatestRobotData.get(endpos).getPosX(), dsl.LatestRobotData
								.get(startpos).getPosY(), dsl.LatestRobotData.get(
								endpos).getPosY()));
	}
	
	public double distancetraveled(int x1, int x2, int y1, int y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2) + ((y1 - y2) * (y1 - y2))));
	}
	
	public void leftturnUsed() {
		sincelastturn = 0;
	}
	
	public void alignUsed() {
		sincelastalign = 0;
	}
}
