package org.penemunxt.nxtclient.projects.communicationtest;

import lejos.nxt.LCD;

import org.penemunxt.nxtclient.communication.INXTCommunicationData;
import org.penemunxt.nxtclient.communication.INXTDataItemProcessor;
import org.penemunxt.nxtclient.communication.NXTCommunication;
import org.penemunxt.nxtclient.communication.NXTCommunicationData;
import org.penemunxt.nxtclient.communication.NXTCommunicationQueue;
import org.penemunxt.nxtclient.communication.NXTDataProcessor;

public class ServerMessageDataProcessor<CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData>
		implements INXTDataItemProcessor {
	public String LatestMessage;

	@Override
	public <CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData> void ProcessItem(
			CommDataInT dataItem,
			NXTCommunication<CommDataInT, CommDataOutT> NXTComm) {

		//ServerMessageData ServerMessageDataItem = (ServerMessageData) dataItem;

		//this.LatestMessage = ServerMessageDataItem.Message;
	}

	@Override
	public <CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData> void SendIsShuttingDownMessage(
			NXTCommunication<CommDataInT, CommDataOutT> NXTComm) {

		NXTCommunicationQueue<SensorData> SendQueue = (NXTCommunicationQueue<SensorData>) NXTComm
				.getDataSendQueue();

		SendQueue.add(new SensorData(
				NXTCommunicationData.MAIN_STATUS_SHUTTING_DOWN,
				NXTCommunicationData.DATA_STATUS_ONLY_STATUS, true));

	}
}