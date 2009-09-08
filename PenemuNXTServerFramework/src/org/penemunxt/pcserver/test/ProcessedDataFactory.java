package org.penemunxt.pcserver.test;
import org.penemunxt.pcserver.communication.INXTCommunicationDataFactory;

public class ProcessedDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public ProcessedData getEmptyInstance() {
		return new ProcessedData();
	}
}
