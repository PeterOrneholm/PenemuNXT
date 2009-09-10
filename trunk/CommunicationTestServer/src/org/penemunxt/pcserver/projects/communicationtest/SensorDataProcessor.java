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

		System.out.println(SensorDataItem.SoundDB);

		NXTCommunicationQueue<ServerMessageData> SendQueue = (NXTCommunicationQueue<ServerMessageData>) NXTComm
				.getDataSendQueue();

		if (SensorDataItem.SoundDB > 100) {
			SendQueue.add(new ServerMessageData(20.3f));
		} else {
			SendQueue.add(new ServerMessageData(10.1f));
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