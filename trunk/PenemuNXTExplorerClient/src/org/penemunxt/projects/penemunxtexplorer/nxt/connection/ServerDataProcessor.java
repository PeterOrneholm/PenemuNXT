package org.penemunxt.projects.penemunxtexplorer.nxt.connection;

import org.penemunxt.communication.INXTCommunicationData;
import org.penemunxt.communication.INXTCommunicationDataFactories;
import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.communication.NXTDataProcessor;
import org.penemunxt.projects.penemunxtexplorer.ServerData;

public class ServerDataProcessor extends NXTDataProcessor {
	@Override
	public void ProcessItem(INXTCommunicationData dataItem,
			NXTCommunication NXTComm) {
		ServerData ServerMessageDataItem = (ServerData) dataItem;
	}

	public ServerDataProcessor(NXTCommunication NXTComm,
			INXTCommunicationDataFactories DataFactories) {
		super(NXTComm, DataFactories);
	}
}