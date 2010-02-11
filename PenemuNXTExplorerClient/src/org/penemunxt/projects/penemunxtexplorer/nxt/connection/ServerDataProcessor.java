package org.penemunxt.projects.penemunxtexplorer.nxt.connection;

import lejos.nxt.Sound;

import org.penemunxt.communication.INXTCommunicationData;
import org.penemunxt.communication.INXTCommunicationDataFactories;
import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.communication.NXTDataProcessor;
import org.penemunxt.projects.penemunxtexplorer.ServerData;

public class ServerDataProcessor extends NXTDataProcessor {
	DataShare DS;
	
	public void ProcessItem(INXTCommunicationData dataItem,
			NXTCommunication NXTComm) {
		ServerData ServerDataItem = (ServerData) dataItem;
		DS.setTargetPos( ServerDataItem.getTargetPosX(), ServerDataItem.getTargetPosY());
	}

	public ServerDataProcessor(DataShare ds, NXTCommunication NXTComm,
			INXTCommunicationDataFactories DataFactories) {
		super(NXTComm, DataFactories);
		DS = ds;
	}
}