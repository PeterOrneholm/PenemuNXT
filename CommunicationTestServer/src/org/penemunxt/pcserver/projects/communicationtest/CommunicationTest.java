package org.penemunxt.pcserver.projects.communicationtest;

import org.penemunxt.pcserver.communication.NXTCommunication;
import org.penemunxt.pcserver.communication.NXTCommunicationData;
import org.penemunxt.pcserver.communication.NXTConnectionModes;
import org.penemunxt.pcserver.communication.NXTDataProcessor;

public class CommunicationTest implements Runnable {
	boolean Active;

	private void sendNormalData(
			NXTCommunication<SensorData, ServerMessageData> NXTComm,
			float Message) {
		NXTComm.getDataSendQueue().add(
				new ServerMessageData(Message));
	}

	private void ShutDown(
			NXTCommunication<SensorData, ServerMessageData> NXTComm) {
		NXTComm.getDataSendQueue().add(
				new ServerMessageData(NXTCommunicationData.MAIN_STATUS_SHUT_DOWN,
						NXTCommunicationData.DATA_STATUS_ONLY_STATUS, true));
	}

	public static void main(String[] args) {
		CommunicationTest PCCT = new CommunicationTest();
		PCCT.run();
	}

	@Override
	public void run() {
		Active = true;

		// Start up the communication
		NXTCommunication<SensorData, ServerMessageData> NXTComm = new NXTCommunication<SensorData, ServerMessageData>(
				false, new SensorDataFactory(), new ServerMessageDataFactory());
		NXTComm.ConnectAndStartAll(NXTConnectionModes.Bluetooth, "PeterF", "0016530A3D1C");

		// Setup a data processor
		NXTDataProcessor<SensorData, ServerMessageData> DP = new NXTDataProcessor<SensorData, ServerMessageData>(
				NXTComm,
				new SensorDataProcessor<SensorData, ServerMessageData>());

		// Add some data to send
		this.sendNormalData(NXTComm, 187);
		this.sendNormalData(NXTComm, 986.5f);
		this.sendNormalData(NXTComm, 456);
		this.sendNormalData(NXTComm, 34);
		this.sendNormalData(NXTComm, 55.6f);
		
		// Handle retrieved data
		DP.start();

		while (Active) {
			this.Active = DP.Active;
			
			/*
			if (Button.ESCAPE.isPressed()) {
				ShutDown(NXTComm);
			}

			if (Button.RIGHT.isPressed()) {
				this.sendNormalData(NXTComm, 10, 20, 30);
			}
*/
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		NXTComm.Disconnect();
		System.exit(0);
	}
}
