package org.penemunxt.projects.communicationtest.pc;

import org.penemunxt.communication.*;
import org.penemunxt.projects.communicationtest.*;

public class SensorDataProcessor extends NXTDataProcessor {
	DataShare DS;

	@Override
	public void ProcessItem(INXTCommunicationData dataItem,
			NXTCommunication NXTComm) {
		SensorData SensorDataItem = (SensorData) dataItem;

		DS.SoundDB = SensorDataItem.getSoundDB();

		if (SensorDataItem.getSoundDB() > 70) {
			NXTComm.sendData(new ServerMessageData(
					ServerMessageData.SOUND_TOO_HIGH));
		} else if (SensorDataItem.getSoundDB() > 40
				&& SensorDataItem.getSoundDB() <= 70) {
			NXTComm.sendData(new ServerMessageData(
					ServerMessageData.SOUND_MEDIUM));
		} else {
			NXTComm.sendData(new ServerMessageData(
					ServerMessageData.SOUND_TOO_LOW));
		}
	}

	public SensorDataProcessor(DataShare ds, NXTCommunication NXTComm,
			INXTCommunicationDataFactories DataFactories) {
		super(NXTComm, DataFactories);
		DS = ds;
	}
}