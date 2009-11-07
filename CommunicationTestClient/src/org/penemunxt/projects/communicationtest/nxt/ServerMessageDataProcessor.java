package org.penemunxt.projects.communicationtest.nxt;

import org.penemunxt.communication.INXTCommunicationData;
import org.penemunxt.communication.INXTCommunicationDataFactories;
import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.communication.NXTDataProcessor;
import org.penemunxt.projects.communicationtest.ServerMessageData;

public class ServerMessageDataProcessor extends NXTDataProcessor {
	@Override
	public void ProcessItem(INXTCommunicationData dataItem,
			NXTCommunication NXTComm) {
		ServerMessageData ServerMessageDataItem = (ServerMessageData) dataItem;
	}

	public ServerMessageDataProcessor(NXTCommunication NXTComm,
			INXTCommunicationDataFactories DataFactories) {
		super(NXTComm, DataFactories);
	}
}