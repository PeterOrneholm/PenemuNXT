package org.penemunxt.nxtclient.test;
import org.penemunxt.nxtclient.communication.INXTCommunicationDataFactory;

public class DistanceDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public DistanceData getEmptyInstance() {
		return new DistanceData();
	}
}
