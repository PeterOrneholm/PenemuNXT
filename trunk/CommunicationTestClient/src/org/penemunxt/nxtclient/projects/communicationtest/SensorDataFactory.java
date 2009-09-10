package org.penemunxt.nxtclient.projects.communicationtest;
import org.penemunxt.nxtclient.communication.*;

public class SensorDataFactory implements	INXTCommunicationDataFactory {
	@Override
	public SensorData getEmptyInstance() {
		return new SensorData(NXTCommunicationData.MAIN_STATUS_NORMAL, NXTCommunicationData.DATA_STATUS_EMPTY);
	}
}
