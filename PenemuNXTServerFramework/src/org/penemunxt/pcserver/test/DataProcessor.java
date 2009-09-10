package org.penemunxt.pcserver.test;

import org.penemunxt.pcserver.communication.NXTCommunication;
import org.penemunxt.pcserver.communication.NXTCommunicationData;

public class DataProcessor extends Thread {
	public boolean Active;
	public int TotalSum;
	NXTCommunication<DistanceData, ProcessedData> NXTComm;

	public DataProcessor(NXTCommunication<DistanceData, ProcessedData> NXTComm) {
		this.NXTComm = NXTComm;
		this.Active = true;
	}

	@Override
	public void run() {
		System.out.println("DP Started");
		// Handle retrieved data
		while (Active) {
			DistanceData DataItem = (DistanceData) NXTComm
					.getDataRetrievedQueue().getAndDeleteNextItem();
			if (DataItem != null) {
				if (DataItem.getMainStatus() == NXTCommunicationData.MAIN_STATUS_SHUT_DOWN) {
					NXTComm.getDataSendQueue().add(
							new ProcessedData(NXTCommunicationData.MAIN_STATUS_SHUTTING_DOWN,
									NXTCommunicationData.DATA_STATUS_ONLY_STATUS, true));
					Active = false;
				}
				if (DataItem.getMainStatus() == NXTCommunicationData.MAIN_STATUS_SHUTTING_DOWN) {
					Active = false;
				}

				System.out.println("Data: " + DataItem.getMainStatus() + ','
						+ DataItem.getDataStatus() + ',' + DataItem.getParam1()
						+ ',' + DataItem.getParam2());
				
				NXTComm.getDataSendQueue().add(
						new ProcessedData(NXTCommunicationData.MAIN_STATUS_NORMAL,
								NXTCommunicationData.DATA_STATUS_NORMAL, (DataItem.getParam1() + DataItem.getParam2())));

			}

			Thread.yield();
		}
	}
}
