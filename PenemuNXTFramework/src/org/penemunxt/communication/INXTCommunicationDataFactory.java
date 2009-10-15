package org.penemunxt.communication;
public interface INXTCommunicationDataFactory {
	public INXTCommunicationData getEmptyInstance();
	public INXTCommunicationData getEmptyShutDownInstance();
	public INXTCommunicationData getEmptyIsShuttingDownInstance();
}
