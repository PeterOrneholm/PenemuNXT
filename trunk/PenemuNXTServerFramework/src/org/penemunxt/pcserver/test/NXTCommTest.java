package org.penemunxt.pcserver.test;

import org.penemunxt.pcserver.communication.NXTCommunication;
import org.penemunxt.pcserver.communication.NXTCommunicationData;
import org.penemunxt.pcserver.communication.NXTConnectionModes;

public class NXTCommTest implements Runnable {
	boolean Active;

	private void sendNormalData(
			NXTCommunication<DistanceData, ProcessedData> NXTComm, int Sum) {
		NXTComm.getDataSendQueue()
				.add(
						new ProcessedData(
								NXTCommunicationData.MAIN_STATUS_NORMAL,
								NXTCommunicationData.DATA_STATUS_NORMAL,
								Sum));
	}

	private void ShutDown(NXTCommunication<DistanceData, ProcessedData> NXTComm) {
		NXTComm.getDataSendQueue().add(
				new ProcessedData(NXTCommunicationData.MAIN_STATUS_SHUT_DOWN,
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
		NXTCommunication<DistanceData, ProcessedData> NXTComm = new NXTCommunication<DistanceData, ProcessedData>(
				false, new DistanceDataFactory(), new ProcessedDataFactory());
		NXTComm.ConnectAndStartAll(NXTConnectionModes.Bluetooth, "PeterF", "0016530A3D1C");
		
		System.out.println("Started");

		// Setup a data processor
		DataProcessor DP = new DataProcessor(NXTComm);

		// Add some data to send
		this.sendNormalData(NXTComm, 50);
		this.sendNormalData(NXTComm, 100);
		// Handle retrieved data
		DP.start();

		// Display status
		while (Active) {
			this.Active = DP.Active;

			/*
			if (Button.ESCAPE.isPressed()) {
				ShutDown(NXTComm);
			}

			if (Button.RIGHT.isPressed()) {
				this.sendNormalData(NXTComm, 10, 20);
			}*/

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		NXTComm.Disconnect();
		System.exit(0);
	}
}
