package Test;
import Communication.INXTCommunicationDataFactory;

public class DistanceDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public DistanceData getEmptyInstance() {
		return new DistanceData();
	}
}
