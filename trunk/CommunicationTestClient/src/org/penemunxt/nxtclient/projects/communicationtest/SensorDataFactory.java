package org.penemunxt.nxtclient.projects.communicationtest;
import org.penemunxt.nxtclient.communication.INXTCommunicationDataFactory;

public class SensorDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public SensorData getEmptyInstance() {
		return new SensorData();
	}
}
