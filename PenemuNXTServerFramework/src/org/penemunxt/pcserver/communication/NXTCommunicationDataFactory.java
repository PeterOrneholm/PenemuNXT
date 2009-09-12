package org.penemunxt.pcserver.communication;

public class NXTCommunicationDataFactory implements
		INXTCommunicationDataFactory {
	@Override
	public NXTCommunicationData getEmptyInstance() {
		return new NXTCommunicationData();
	}

	@Override
	public INXTCommunicationData getEmptyIsShuttingDownInstance() {
		return new NXTCommunicationData(
				NXTCommunicationData.MAIN_STATUS_SHUTTING_DOWN,
				NXTCommunicationData.DATA_STATUS_ONLY_STATUS);
	}

	@Override
	public INXTCommunicationData getEmptyShutDownInstance() {
		return new NXTCommunicationData(
				NXTCommunicationData.MAIN_STATUS_SHUT_DOWN,
				NXTCommunicationData.DATA_STATUS_ONLY_STATUS);
	}
}