package org.penemunxt.projects.penemunxtexplorer.nxt.connection;

import java.util.ArrayList;

import lejos.geom.Point;

import org.penemunxt.projects.penemunxtexplorer.RobotData;

public class DataShare {
	public int sincelastalign = 0;
	public int sincelastturn = 0;
	public Point TargetPos = new Point (1,1);
	public boolean lockBehaviour;
	public ArrayList<RobotData> LatestRobotData;
	public boolean SendData = true;
	
	final double ISLINEAR_THRESHOLD = 0.1;
	final int ROBOTDATA_MAX_SIZE = 100;

	public DataShare() {
		super();
		LatestRobotData = new ArrayList<RobotData>();
	}

	public void addRobotData(RobotData RD) {
		LatestRobotData.add(0, RD);
		if (LatestRobotData.size() > ROBOTDATA_MAX_SIZE) {
			LatestRobotData.remove(LatestRobotData.size() - 1);
		}

		sincelastalign++;
		sincelastturn++;
	}
	
	public double isLinear(DataShare dsl, int startpos, int endpos) {

		double coef = linCoef ( dsl, startpos, endpos);

		for (int n = startpos; n < endpos; n++) {
			if ((linCoef ( dsl, n, endpos) / coef) > (1+ISLINEAR_THRESHOLD) 
					||  ((linCoef ( dsl, n, endpos) / coef) < (1 - ISLINEAR_THRESHOLD))) return 0;
				
		}

		return coef;
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
	
	public void stopSendData() {
		SendData = false;
	}
	
	public void startSendData() {
		SendData = true;
	}
	public void setTargetPos( int x, int y){
		TargetPos = new Point (x, y);
	}
	
}
