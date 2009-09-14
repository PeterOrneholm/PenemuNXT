package org.penemunxt.projects.communicationtest.nxt;

import org.penemunxt.communication.*;
import org.penemunxt.communication.nxt.*;
import org.penemunxt.projects.communicationtest.*;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;
import lejos.nxt.addon.OpticalDistanceSensor;

public class CommTest implements Runnable {
	boolean Active;
	DataShare DS;
	NXTCommunication NXTC;

	public static void main(String[] args) {
		CommTest NXTCT = new CommTest();
		NXTCT.run();
	}

	@Override
	public void run() {
		Active = true;

		// Object to share data internal
		DS = new DataShare();

		// Setup data factories
		NXTCommunicationDataFactories DataFactories = new NXTCommunicationDataFactories(
				new ServerMessageDataFactory(), new SensorDataFactory());

		// Setup and start the communication
		NXTC = new NXTCommunication(true, DataFactories,
				new NXTDataStreamConnection());
		NXTC.ConnectAndStartAll(NXTConnectionModes.USB);

		// Setup a data processor
		ServerMessageDataProcessor SMDP = new ServerMessageDataProcessor(DS,
				NXTC, DataFactories);
		SMDP.start();

		LCD.clear();
		SoundSensor SS = new SoundSensor(SensorPort.S1, true);
		OpticalDistanceSensor ODS = new OpticalDistanceSensor(SensorPort.S2);
		while (Active) {
			this.Active = SMDP.Active;

			LCD.clear();

			LCD.drawString("CommTestClient", 1, 1);
			LCD.drawString("LSO: " + NXTC.getDataSendQueue().getQueueSize(), 1,
					3);
			LCD.drawString("LSI: "
					+ NXTC.getDataRetrievedQueue().getQueueSize(), 1, 4);

			LCD
					.drawString("M: "
							+ ServerMessageData
									.getMessageDescription(DS.Message), 1, 5);

			LCD
			.drawString("S: "
					+ SS.readValue(), 1, 6);
			
			LCD.refresh();

			if (Button.ESCAPE.isPressed()) {
				NXTC.sendShutDown();
			}

			NXTC.sendData(new SensorData(ODS.getDistance(), 0, SS.readValue()));

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		NXTC.Disconnect();
		System.exit(0);
	}
}
