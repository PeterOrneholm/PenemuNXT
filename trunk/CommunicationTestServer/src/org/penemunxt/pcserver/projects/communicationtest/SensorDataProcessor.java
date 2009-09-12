package org.penemunxt.pcserver.projects.communicationtest;

import org.penemunxt.pcserver.communication.INXTCommunicationData;
import org.penemunxt.pcserver.communication.INXTCommunicationDataFactories;
import org.penemunxt.pcserver.communication.NXTCommunication;
import org.penemunxt.pcserver.communication.NXTCommunicationQueue;
import org.penemunxt.pcserver.communication.NXTDataProcessor;

public class SensorDataProcessor extends NXTDataProcessor {
	DataShare DS;

	@Override
	public void ProcessItem(INXTCommunicationData dataItem,
			NXTCommunication NXTComm) {
		SensorData SensorDataItem = (SensorData) dataItem;

		String TempOut = "";
		for (int i = 0; i < (int) (SensorDataItem.SoundDB); i++) {
			TempOut += "*";
		}

		NXTCommunicationQueue SendQueue = NXTComm.getDataSendQueue();

		DS.SoundDB = SensorDataItem.SoundDB;

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

	public SensorDataProcessor(DataShare ds, NXTCommunication NXTComm,
			INXTCommunicationDataFactories DataFactories) {
		super(NXTComm, DataFactories);
		DS = ds;
	}
}