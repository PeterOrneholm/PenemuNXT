package org.penemunxt.nxtclient.test;

import org.penemunxt.nxtclient.communication.NXTCommunication;
import org.penemunxt.nxtclient.communication.NXTCommunicationData;
import org.penemunxt.nxtclient.communication.NXTConnectionModes;
import org.penemunxt.nxtclient.debug.NXTDebug;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class NXTCommTest implements Runnable {
	boolean Active;

	private void sendNormalData(
			NXTCommunication<ProcessedData, DistanceData> NXTComm, int Param1,
			int Param2) {
		NXTComm.getDataSendQueue()
				.add(
						new DistanceData(
								NXTCommunicationData.MAIN_STATUS_NORMAL,
								NXTCommunicationData.DATA_STATUS_NORMAL,
								Param1, Param2));
	}

	private void ShutDown(NXTCommunication<ProcessedData, DistanceData> NXTComm) {
		NXTComm.getDataSendQueue().add(
				new DistanceData(NXTCommunicationData.MAIN_STATUS_SHUT_DOWN,
						NXTCommunicationData.DATA_STATUS_ONLY_STATUS, true));
	}
	
	public static void main(String[] args) {
		NXTCommTest NXTCT = new NXTCommTest();
		NXTCT.run();
	}

	@Override
	public void run() {
		Active = true;

		// Start up the communication
		NXTCommunication<ProcessedData, DistanceData> NXTComm = new NXTCommunication<ProcessedData, DistanceData>(
				true, new ProcessedDataFactory(), new DistanceDataFactory());
		NXTComm.ConnectAndStartAll(NXTConnectionModes.Bluetooth);

		// Setup a data processor
		DataProcessor DP = new DataProcessor(NXTComm);

		// Add some data to send
		this.sendNormalData(NXTComm, 1, 5);
		this.sendNormalData(NXTComm, 7, 2);
		this.sendNormalData(NXTComm, 55, 120);
		this.sendNormalData(NXTComm, 358, 88);
		this.sendNormalData(NXTComm, 25, 10);
		this.sendNormalData(NXTComm, 3, 85);

		// Handle retrieved data
		DP.start();

		// Display status
		while (Active) {
			LCD.clear();

			LCD.drawString("-CommTest-", 1, 1);
			LCD.drawString("TS:" + DP.TotalSum, 1, 2);
			LCD.drawString("LSO:" + NXTComm.getDataSendQueue().getQueueSize(),
					1, 3);
			LCD.drawString("LSI:"
					+ NXTComm.getDataRetrievedQueue().getQueueSize(), 1, 4);

			LCD.refresh();

			this.Active = DP.Active;

			if (Button.ESCAPE.isPressed()) {
				ShutDown(NXTComm);
			}

			if (Button.RIGHT.isPressed()) {
				this.sendNormalData(NXTComm, 10, 20);
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
