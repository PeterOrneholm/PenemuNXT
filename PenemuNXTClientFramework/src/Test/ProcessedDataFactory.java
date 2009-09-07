package Test;
import Communication.INXTCommunicationDataFactory;

public class ProcessedDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public ProcessedData getEmptyInstance() {
		return new ProcessedData();
	}
}
