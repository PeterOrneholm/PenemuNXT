package org.penemunxt.projects.communicationtest.pc.connection;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.communication.NXTCommunicationDataFactories;
import org.penemunxt.communication.NXTConnectionModes;
import org.penemunxt.communication.pc.PCDataStreamConnection;
import org.penemunxt.projects.communicationtest.RobotDataFactory;
import org.penemunxt.projects.communicationtest.ServerMessageDataFactory;

public class RobotConnection extends Thread implements Runnable {
	private boolean connectionActive;
	private NXTCommunication NXTC;
	private DataShare DS;
	private NXTConnectionModes ConnMode;
	private String Name;
	private String Address;
	
	public boolean isConnectionActive() {
		return connectionActive;
	}

	public RobotConnection(NXTCommunication nxtc, DataShare ds,
			NXTConnectionModes connMode, String name, String address) {
		super();
		NXTC = nxtc;
		DS = ds;
		ConnMode = connMode;
		Name = name;
		Address = address;
		connectionActive = true;
	}
	
	public void disconnect(){
		if (NXTC != null) {
			NXTC.sendShutDown();
		}
	}

	public void run() {
		connectionActive = true;

		// Setup data factories
		NXTCommunicationDataFactories DataFactories = new NXTCommunicationDataFactories(
				new RobotDataFactory(), new ServerMessageDataFactory());

		// Setup and start the communication
		PCDataStreamConnection CPDSC = new PCDataStreamConnection();
		NXTC = new NXTCommunication(false, DataFactories, CPDSC);
		NXTC.ConnectAndStartAll(ConnMode, Name, Address);

		// Setup a data processor
		PositionDataProcessor SDP = new PositionDataProcessor(DS, NXTC,
				DataFactories);
		SDP.start();

		while (connectionActive) {
			this.connectionActive = SDP.Active;

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}

		NXTC.Disconnect();
		NXTC = null;
	}
}
