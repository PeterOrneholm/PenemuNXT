package org.penemunxt.projects.penemunxtexplorer.nxt;

import org.penemunxt.communication.INXTCommunicationData;
import org.penemunxt.communication.INXTCommunicationDataFactories;
import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.communication.NXTDataProcessor;
import org.penemunxt.projects.penemunxtexplorer.ServerData;

public class ServerMessageDataProcessor extends NXTDataProcessor {
	@Override
	public void ProcessItem(INXTCommunicationData dataItem,
			NXTCommunication NXTComm) {
		ServerData ServerMessageDataItem = (ServerData) dataItem;
	}

	public ServerMessageDataProcessor(NXTCommunication NXTComm,
			INXTCommunicationDataFactories DataFactories) {
		super(NXTComm, DataFactories);
	}
}