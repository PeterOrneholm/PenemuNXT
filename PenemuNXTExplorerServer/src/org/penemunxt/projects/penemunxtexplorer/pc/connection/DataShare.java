package org.penemunxt.projects.penemunxtexplorer.pc.connection;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;

public class DataShare {
	public ArrayList<RobotData> NXTRobotData;
	
	public DataShare(){
		super();
		NXTRobotData = new ArrayList<RobotData>();
	}
}
