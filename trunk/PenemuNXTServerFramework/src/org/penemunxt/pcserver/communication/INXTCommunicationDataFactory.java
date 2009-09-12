package org.penemunxt.pcserver.communication;
public interface INXTCommunicationDataFactory {
	public INXTCommunicationData getEmptyInstance();
	public INXTCommunicationData getEmptyShutDownInstance();
	public INXTCommunicationData getEmptyIsShuttingDownInstance();
}
