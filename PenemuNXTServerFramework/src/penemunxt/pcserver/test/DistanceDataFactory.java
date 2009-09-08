package penemunxt.pcserver.test;
import penemunxt.pcserver.communication.INXTCommunicationDataFactory;

public class DistanceDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public DistanceData getEmptyInstance() {
		return new DistanceData();
	}
}
