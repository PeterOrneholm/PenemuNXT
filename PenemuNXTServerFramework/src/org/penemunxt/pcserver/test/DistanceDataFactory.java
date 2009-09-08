package org.penemunxt.pcserver.test;
import org.penemunxt.pcserver.communication.INXTCommunicationDataFactory;

public class DistanceDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public DistanceData getEmptyInstance() {
		return new DistanceData();
	}
}
