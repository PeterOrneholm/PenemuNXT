package org.penemunxt.nxtclient.projects.communicationtest;

import org.penemunxt.nxtclient.communication.NXTCommunication;
import org.penemunxt.nxtclient.communication.NXTCommunicationData;
import org.penemunxt.nxtclient.communication.NXTConnectionModes;
import org.penemunxt.nxtclient.communication.NXTDataProcessor;
import org.penemunxt.nxtclient.debug.NXTDebug;

import lejos.nxt.Button;
import lejos.nxt.LCD;

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

		// Start up the communication
		NXTCommunication<ServerMessageData, SensorData> NXTComm = new NXTCommunication<ServerMessageData, SensorData>(
				true, new ServerMessageDataFactory(), new SensorDataFactory());
		NXTComm.ConnectAndStartAll(NXTConnectionModes.Bluetooth);

		// Setup a data processor
		NXTDataProcessor<ServerMessageData, SensorData> DP = new NXTDataProcessor<ServerMessageData, SensorData>(
				NXTComm,
				new ServerMessageDataProcessor<ServerMessageData, SensorData>());

		// Add some data to send
		this.sendNormalData(NXTComm, 1, 5, 17);
		this.sendNormalData(NXTComm, 7, 2, 8);
		this.sendNormalData(NXTComm, 55, 120, 25);
		this.sendNormalData(NXTComm, 358, 88, 32);
		this.sendNormalData(NXTComm, 25, 10, 58);
		this.sendNormalData(NXTComm, 3, 85, 589);

		// Handle retrieved data
		DP.start();

		LCD.clear();
		while (Active) {
			this.Active = DP.Active;

			LCD.clear();

			LCD.drawString("CommTestClient", 1, 1);
			LCD.drawString("LSO:" + NXTComm.getDataSendQueue().getQueueSize(),
					1, 3);
			LCD.drawString("LSI:"
					+ NXTComm.getDataRetrievedQueue().getQueueSize(), 1, 4);

			LCD.refresh();

			if (Button.ESCAPE.isPressed()) {
				ShutDown(NXTComm);
			}

			if (Button.RIGHT.isPressed()) {
				this.sendNormalData(NXTComm, 10, 20, 30);
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		NXTComm.Disconnect();
		NXTDebug.WriteMessageAndWait("Finished!");
		System.exit(0);
	}
}
