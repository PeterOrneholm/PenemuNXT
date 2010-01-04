package org.penemunxt.projects.penemunxtexplorer;

import org.penemunxt.communication.INXTCommunicationData;
import org.penemunxt.communication.INXTCommunicationDataFactory;
import org.penemunxt.communication.NXTCommunicationData;


public class RobotDataFactory implements INXTCommunicationDataFactory {
	@Override
	public INXTCommunicationData getEmptyInstance() {
		return new RobotData(NXTCommunicationData.MAIN_STATUS_NORMAL,
				NXTCommunicationData.DATA_STATUS_EMPTY);
	}

	@Override
	public INXTCommunicationData getEmptyIsShuttingDownInstance() {
		return new RobotData(NXTCommunicationData.MAIN_STATUS_SHUTTING_DOWN,
				NXTCommunicationData.DATA_STATUS_ONLY_STATUS);
	}

	@Override
	public INXTCommunicationData getEmptyShutDownInstance() {
		return new RobotData(NXTCommunicationData.MAIN_STATUS_SHUT_DOWN,
				NXTCommunicationData.DATA_STATUS_ONLY_STATUS);
	}
}
