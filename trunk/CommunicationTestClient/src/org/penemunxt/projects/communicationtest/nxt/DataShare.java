package org.penemunxt.projects.communicationtest.nxt;

import java.io.Writer;
import java.util.ArrayList;
import org.penemunxt.projects.communicationtest.RobotData;
import org.penemunxt.debug.nxt.NXTDebug;

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
}
