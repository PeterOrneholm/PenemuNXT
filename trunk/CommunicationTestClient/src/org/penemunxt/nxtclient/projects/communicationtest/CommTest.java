package org.penemunxt.nxtclient.projects.communicationtest;

import org.penemunxt.nxtclient.communication.NXTCommunication;
import org.penemunxt.nxtclient.communication.NXTCommunicationData;
import org.penemunxt.nxtclient.communication.NXTConnectionModes;
import org.penemunxt.nxtclient.communication.NXTDataProcessor;
import org.penemunxt.nxtclient.debug.NXTDebug;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;

public class CommTest implements Runnable {
	boolean Active;

	private void sendNormalData(
			NXTCommunication<ServerMessageData, SensorData> NXTComm,
			int IRDistance, int USDistance, int SoundDB) {
		NXTComm.getDataSendQueue().add(
				new SensorData(IRDistance, USDistance, SoundDB));
	}

	private void ShutDown(
			NXTCommunication<ServerMessageData, SensorData> NXTComm) {
		NXTComm.getDataSendQueue().add(
				new SensorData(NXTCommunicationData.MAIN_STATUS_SHUT_DOWN,
						NXTCommunicationData.DATA_STATUS_ONLY_STATUS, true));
	}

	public static void main(String[] args) {
		CommTest NXTCT = new CommTest();
		NXTCT.run();
	}

	@Override
	public void run() {
		Active = true;
		DataShare DS = new DataShare();

		// Start up the communication
		NXTCommunication<ServerMessageData, SensorData> NXTComm = new NXTCommunication<ServerMessageData, SensorData>(
				true, new ServerMessageDataFactory(), new SensorDataFactory());
		NXTComm.ConnectAndStartAll(NXTConnectionModes.USB);

		// Setup a data processor
		NXTDataProcessor<ServerMessageData, SensorData> DP = new NXTDataProcessor<ServerMessageData, SensorData>(
				NXTComm,
				new ServerMessageDataProcessor<ServerMessageData, SensorData>(
						DS));

		DP.start();

		LCD.clear();
		SoundSensor SS = new SoundSensor(SensorPort.S1);
		while (Active) {
			this.Active = DP.Active;

			LCD.clear();

			LCD.drawString("CommTestClient", 1, 1);
			LCD.drawString("LSO: " + NXTComm.getDataSendQueue().getQueueSize(),
					1, 3);
			LCD.drawString("LSI: "
					+ NXTComm.getDataRetrievedQueue().getQueueSize(), 1, 4);

			LCD
					.drawString("M: "
							+ ServerMessageData
									.getMessageDescription(DS.Message), 1, 5);

			LCD.refresh();

			if (Button.ESCAPE.isPressed()) {
				ShutDown(NXTComm);
			}

			this.sendNormalData(NXTComm, 0, 0, SS.readValue());

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		NXTComm.Disconnect();
		System.exit(0);
	}
}
