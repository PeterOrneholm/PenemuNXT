package org.penemunxt.projects.communicationtest.nxt;

import org.penemunxt.communication.*;
import org.penemunxt.projects.communicationtest.*;

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