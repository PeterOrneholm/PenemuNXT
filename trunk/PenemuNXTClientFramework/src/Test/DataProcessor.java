package Test;

import Communication.NXTCommunication;
import Communication.NXTCommunicationData;

public class DataProcessor extends Thread {
	public boolean Active;
	public int TotalSum;
	NXTCommunication<ProcessedData, DistanceData> NXTComm;

	public DataProcessor(NXTCommunication<ProcessedData, DistanceData> NXTComm) {
		this.NXTComm = NXTComm;
		this.Active = true;
	}

	@Override
	public void run() {
		// Handle retrieved data
		while (Active) {
			ProcessedData DataItem = (ProcessedData) NXTComm
					.getDataRetrievedQueue().getAndDeleteNextItem();
			if (DataItem != null) {
				if (DataItem.getMainStatus() == NXTCommunicationData.MAIN_STATUS_SHUTTING_DOWN) {
					Active = false;
				} else {
					this.TotalSum += DataItem.getSum();
				}
			}

			Thread.yield();
		}
	}
}
