package org.penemunxt.nxtclient.test;

import org.penemunxt.nxtclient.communication.NXTCommunication;
import org.penemunxt.nxtclient.communication.NXTCommunicationData;

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
				if (DataItem.getMainStatus() == NXTCommunicationData.MAIN_STATUS_SHUT_DOWN) {
					NXTComm.getDataSendQueue().add(
							new DistanceData(NXTCommunicationData.MAIN_STATUS_SHUTTING_DOWN,
									NXTCommunicationData.DATA_STATUS_ONLY_STATUS, true));
					Active = false;
				}

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
