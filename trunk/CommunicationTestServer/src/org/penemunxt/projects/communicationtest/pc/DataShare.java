package org.penemunxt.projects.communicationtest.pc;
import java.util.ArrayList;

import org.penemunxt.projects.communicationtest.RobotData;

public class DataShare {
	public ArrayList<RobotData> NXTRobotData;
	
	public DataShare(){
		super();
		NXTRobotData = new ArrayList<RobotData>();
	}
}
