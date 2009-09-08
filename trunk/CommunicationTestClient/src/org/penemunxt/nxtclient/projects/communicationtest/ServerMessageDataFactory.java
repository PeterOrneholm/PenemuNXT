package org.penemunxt.nxtclient.projects.communicationtest;
import org.penemunxt.nxtclient.communication.INXTCommunicationDataFactory;

public class ServerMessageDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public ServerMessageData getEmptyInstance() {
		return new ServerMessageData();
	}
}
