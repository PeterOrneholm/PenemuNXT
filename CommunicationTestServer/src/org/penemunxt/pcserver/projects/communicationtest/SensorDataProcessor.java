package org.penemunxt.pcserver.projects.communicationtest;

import org.penemunxt.pcserver.communication.INXTCommunicationData;
import org.penemunxt.pcserver.communication.INXTDataItemProcessor;
import org.penemunxt.pcserver.communication.NXTCommunication;
import org.penemunxt.pcserver.communication.NXTCommunicationData;
import org.penemunxt.pcserver.communication.NXTCommunicationQueue;
import org.penemunxt.pcserver.communication.NXTDataProcessor;

public class SensorDataProcessor<CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData>
		implements INXTDataItemProcessor {
	@Override
	public <CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData> void ProcessItem(
			CommDataInT dataItem,
			NXTCommunication<CommDataInT, CommDataOutT> NXTComm) {

		SensorData SensorDataItem = (SensorData) dataItem;

		String TempOut = "";
		for (int i = 0; i < (int) (SensorDataItem.SoundDB); i++) {
			TempOut += "*";
		}
		System.out.println(TempOut);

		NXTCommunicationQueue<ServerMessageData> SendQueue = (NXTCommunicationQueue<ServerMessageData>) NXTComm
				.getDataSendQueue();

		if (SensorDataItem.SoundDB > 70) {
			SendQueue.add(new ServerMessageData(
					ServerMessageData.SOUND_TOO_HIGH));
		} else if (SensorDataItem.SoundDB > 40 && SensorDataItem.SoundDB <= 70) {
			SendQueue
					.add(new ServerMessageData(ServerMessageData.SOUND_MEDIUM));
		} else {
			SendQueue
					.add(new ServerMessageData(ServerMessageData.SOUND_TOO_LOW));
		}
	}

	@Override
	public <CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData> void SendIsShuttingDownMessage(
			NXTCommunication<CommDataInT, CommDataOutT> NXTComm) {

		NXTCommunicationQueue<ServerMessageData> SendQueue = (NXTCommunicationQueue<ServerMessageData>) NXTComm
				.getDataSendQueue();

		SendQueue.add(new ServerMessageData(
				NXTCommunicationData.MAIN_STATUS_SHUTTING_DOWN,
				NXTCommunicationData.DATA_STATUS_ONLY_STATUS, true));

	}
}