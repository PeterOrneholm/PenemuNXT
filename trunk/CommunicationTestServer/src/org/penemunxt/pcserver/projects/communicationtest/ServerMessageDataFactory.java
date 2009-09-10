package org.penemunxt.pcserver.projects.communicationtest;

import org.penemunxt.pcserver.communication.INXTCommunicationDataFactory;
import org.penemunxt.pcserver.communication.NXTCommunicationData;

public class ServerMessageDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public ServerMessageData getEmptyInstance() {
		return new ServerMessageData(NXTCommunicationData.MAIN_STATUS_NORMAL, NXTCommunicationData.DATA_STATUS_EMPTY);
	}
}
