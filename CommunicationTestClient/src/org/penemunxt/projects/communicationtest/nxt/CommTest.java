package org.penemunxt.projects.communicationtest.nxt;

import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.robotics.navigation.*;

import org.penemunxt.communication.*;
import org.penemunxt.communication.nxt.NXTDataStreamConnection;
import org.penemunxt.projects.communicationtest.*;



public class CommTest implements Runnable {
	boolean Active;
	NXTCommunication NXTC;


	public static void main(String[] args) {
		CommTest NXTCT = new CommTest();
		NXTCT.run();
	}

	@Override
	public void run() {
		Active = true;

		// Setup data factories
		NXTCommunicationDataFactories DataFactories = new NXTCommunicationDataFactories(
				new ServerDataFactory(), new RobotDataFactory());

		// Setup and start the communication
		NXTC = new NXTCommunication(true, DataFactories,
				new NXTDataStreamConnection());
		NXTC.ConnectAndStartAll(NXTConnectionModes.Bluetooth);

		// Setup a data processor
		ServerMessageDataProcessor SMDP = new ServerMessageDataProcessor(NXTC,
				DataFactories);
		SMDP.start();
		//Sensors
		
		OpticalDistanceSensor ODS = new OpticalDistanceSensor(SensorPort.S1);
		
		// Navigation
		CompassPilot compil = new CompassPilot(
				new CompassSensor(SensorPort.S2), 49, 125, Motor.C, Motor.B);
		TachoPilot tacho = new TachoPilot(49, 125, Motor.C, Motor.B);
		SimpleNavigator simnav = new SimpleNavigator(tacho);
		
		//Array med 10 senaste värdena
		DataShare DSL = new DataShare();
		
		leJOSnavigation lenav = new leJOSnavigation(simnav, NXTC, ODS, DSL);
		lenav.start();

		LCD.clear();
		
		
		
		while (Active) {
			this.Active = SMDP.Active;

			LCD.clear();

			LCD.drawString("CommTestClient", 1, 1);
			LCD.drawString("LSO: " + NXTC.getDataSendQueue().getQueueSize(), 1,
					3);
			LCD.drawString("LSI: "
					+ NXTC.getDataRetrievedQueue().getQueueSize(), 1, 4);

			LCD.refresh();

			if (Button.ESCAPE.isPressed()) {
				NXTC.sendShutDown();
			}

			simnav.updatePosition();
			RobotData RD = new RobotData(RobotData.POSITION_TYPE_DRIVE,
					(int) simnav.getX(), (int) simnav.getY(), (int) simnav
					.getHeading(), ODS.getDistance(), Motor.A.getTachoCount());
			
			NXTC.sendData(RD);
			
			RobotData RDL = new RobotData(RobotData.POSITION_TYPE_DRIVE,
					(int) simnav.getX(), (int) simnav.getY(), (int) simnav
					.getHeading(), ODS.getDistance(), Motor.A.getTachoCount());
			
			
			DSL.addRobotData(RDL);
			

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		Motor.A.stop();
		Motor.A.rotateTo(0);
		NXTC.Disconnect();
		System.exit(0);
	}
}


