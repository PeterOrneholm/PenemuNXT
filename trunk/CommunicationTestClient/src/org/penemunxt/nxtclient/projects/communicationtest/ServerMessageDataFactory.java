package org.penemunxt.nxtclient.projects.communicationtest;
import org.penemunxt.nxtclient.communication.INXTCommunicationDataFactory;
import org.penemunxt.nxtclient.communication.NXTCommunicationData;

public class ServerMessageDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public ServerMessageData getEmptyInstance() {
		return new ServerMessageData(NXTCommunicationData.MAIN_STATUS_NORMAL, NXTCommunicationData.DATA_STATUS_EMPTY);
	}
}
