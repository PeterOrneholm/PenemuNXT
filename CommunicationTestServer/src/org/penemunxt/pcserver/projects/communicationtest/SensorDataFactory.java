package org.penemunxt.pcserver.projects.communicationtest;

import org.penemunxt.pcserver.communication.INXTCommunicationDataFactory;
import org.penemunxt.pcserver.communication.NXTCommunicationData;

public class SensorDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public SensorData getEmptyInstance() {
		return new SensorData(NXTCommunicationData.MAIN_STATUS_NORMAL, NXTCommunicationData.DATA_STATUS_EMPTY);
	}
}
