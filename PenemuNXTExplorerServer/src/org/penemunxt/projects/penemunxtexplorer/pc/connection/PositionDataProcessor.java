package org.penemunxt.projects.penemunxtexplorer.pc.connection;

import org.penemunxt.communication.*;
import org.penemunxt.projects.communicationtest.*;

public class PositionDataProcessor extends NXTDataProcessor {
	DataShare DS;

	@Override
	public void ProcessItem(INXTCommunicationData dataItem,
			NXTCommunication NXTComm) {
		RobotData PositionDataItem = (RobotData) dataItem;

		DS.NXTRobotData.add(PositionDataItem);
	}

	public PositionDataProcessor(DataShare ds, NXTCommunication NXTComm,
			INXTCommunicationDataFactories DataFactories) {
		super(NXTComm, DataFactories);
		DS = ds;
	}
}