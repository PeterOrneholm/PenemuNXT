package penemunxt.pcserver.test;
import penemunxt.pcserver.communication.INXTCommunicationDataFactory;

public class ProcessedDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public ProcessedData getEmptyInstance() {
		return new ProcessedData();
	}
}
