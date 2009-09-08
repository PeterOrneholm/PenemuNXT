package org.penemunxt.nxtclient.test;
import org.penemunxt.nxtclient.communication.INXTCommunicationDataFactory;

public class ProcessedDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public ProcessedData getEmptyInstance() {
		return new ProcessedData();
	}
}
