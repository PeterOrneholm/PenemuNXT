package org.penemunxt.projects.communicationtest;

import org.penemunxt.communication.*;

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
