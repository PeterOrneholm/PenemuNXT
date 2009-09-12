package org.penemunxt.pcserver.projects.communicationtest;

import org.penemunxt.pcserver.communication.INXTCommunicationData;
import org.penemunxt.pcserver.communication.INXTCommunicationDataFactory;
import org.penemunxt.pcserver.communication.NXTCommunicationData;

public class SensorDataFactory implements INXTCommunicationDataFactory {
	@Override
	public SensorData getEmptyInstance() {
		return new SensorData(NXTCommunicationData.MAIN_STATUS_NORMAL,
				NXTCommunicationData.DATA_STATUS_EMPTY);
	}

	@Override
	public INXTCommunicationData getEmptyIsShuttingDownInstance() {
		return new SensorData(NXTCommunicationData.MAIN_STATUS_SHUTTING_DOWN,
				NXTCommunicationData.DATA_STATUS_ONLY_STATUS);
	}

	@Override
	public INXTCommunicationData getEmptyShutDownInstance() {
		return new SensorData(NXTCommunicationData.MAIN_STATUS_SHUT_DOWN,
				NXTCommunicationData.DATA_STATUS_ONLY_STATUS);
	}
}
