package Test;

import Communication.NXTCommunication;
import Communication.NXTCommunicationData;
import Communication.NXTConnectionModes;
import Debug.NXTDebug;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.RConsole;

public class NXTCommTest implements Runnable {
	boolean Active;

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
		NXTComm.ConnectAndStartAll(NXTConnectionModes.USB);

		// Setup a dataprocessor
		DataProcessor DP = new DataProcessor(NXTComm);

		// Add some data to send
		NXTComm.getDataSendQueue().add(
				new DistanceData(NXTCommunicationData.MAIN_STATUS_NORMAL,
						NXTCommunicationData.DATA_STATUS_NORMAL, 1, 5));
		NXTComm.getDataSendQueue().add(
				new DistanceData(NXTCommunicationData.MAIN_STATUS_NORMAL,
						NXTCommunicationData.DATA_STATUS_NORMAL, 6, 789));
		NXTComm.getDataSendQueue().add(
				new DistanceData(NXTCommunicationData.MAIN_STATUS_NORMAL,
						NXTCommunicationData.DATA_STATUS_NORMAL, 343, 534));
		NXTComm.getDataSendQueue().add(
				new DistanceData(NXTCommunicationData.MAIN_STATUS_NORMAL,
						NXTCommunicationData.DATA_STATUS_NORMAL, 258, 3218));
		NXTComm.getDataSendQueue().add(
				new DistanceData(NXTCommunicationData.MAIN_STATUS_NORMAL,
						NXTCommunicationData.DATA_STATUS_NORMAL, 3, 2));

		// Handle retrieved data
		DP.start();

		// Display status
		while (Active) {
			LCD.clear();

			LCD.drawString("-CommTest-", 1,1);
			LCD.drawString("TS:"
					+ DP.TotalSum, 1, 2);
			LCD.drawString("LSO:"
					+ NXTComm.getDataSendQueue().getQueueSize(), 1, 3);
			LCD.drawString("LSI:"
					+ NXTComm.getDataRetrievedQueue().getQueueSize(), 1, 4);

			LCD.refresh();

			this.Active = DP.Active;

			if (Button.ESCAPE.isPressed()) {
				ShutDown(NXTComm);
			}

			if (Button.RIGHT.isPressed()) {
				NXTComm.getDataSendQueue().add(
						new DistanceData(NXTCommunicationData.MAIN_STATUS_NORMAL,
								NXTCommunicationData.DATA_STATUS_NORMAL, 1256, 2158));
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
	
	private void ShutDown(NXTCommunication<ProcessedData, DistanceData> NXTComm){
		NXTComm.getDataSendQueue().add(
				new DistanceData(
						NXTCommunicationData.MAIN_STATUS_SHUT_DOWN,
						NXTCommunicationData.DATA_STATUS_ONLY_STATUS,
						true));
	}
}
