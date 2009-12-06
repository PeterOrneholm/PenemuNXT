package org.penemunxt.projects.penemunxtexplorer.pc.connection;

import org.penemunxt.communication.INXTCommunicationData;
import org.penemunxt.communication.INXTCommunicationDataFactories;
import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.communication.NXTDataProcessor;
import org.penemunxt.projects.penemunxtexplorer.RobotData;

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