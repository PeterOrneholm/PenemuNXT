package org.penemunxt.projects.communicationtest.nxt;

import org.penemunxt.communication.*;
import org.penemunxt.projects.communicationtest.*;

public class ServerMessageDataProcessor extends NXTDataProcessor {
	private DataShare DS;

	@Override
	public void ProcessItem(INXTCommunicationData dataItem,
			NXTCommunication NXTComm) {
		ServerMessageData ServerMessageDataItem = (ServerMessageData) dataItem;

		this.DS.Message = ServerMessageDataItem.getMessage();
	}

	public ServerMessageDataProcessor(DataShare ds, NXTCommunication NXTComm,
			INXTCommunicationDataFactories DataFactories) {
		super(NXTComm, DataFactories);
		this.DS = ds;
	}
}