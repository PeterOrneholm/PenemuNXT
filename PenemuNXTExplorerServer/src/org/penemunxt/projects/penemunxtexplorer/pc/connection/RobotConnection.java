package org.penemunxt.projects.penemunxtexplorer.pc.connection;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.communication.NXTCommunicationDataFactories;
import org.penemunxt.communication.NXTConnectionModes;
import org.penemunxt.communication.pc.PCDataStreamConnection;
import org.penemunxt.projects.penemunxtexplorer.RobotDataFactory;
import org.penemunxt.projects.penemunxtexplorer.ServerDataFactory;

public class RobotConnection extends Thread implements Runnable {
	private boolean connectionActive;
	public NXTCommunication NXTC;
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

	@Override
	public void run() {
		connectionActive = true;

		// Setup data factories
		NXTCommunicationDataFactories DataFactories = new NXTCommunicationDataFactories(
				new RobotDataFactory(), new ServerDataFactory());

		// Setup and start the communication
		PCDataStreamConnection CPDSC = new PCDataStreamConnection();
		NXTC = new NXTCommunication(false, DataFactories, CPDSC);
		NXTC.ConnectAndStartAll(ConnMode, Name, Address);

		// Setup a data processor
		PositionDataProcessor PDP = new PositionDataProcessor(DS, NXTC,
				DataFactories);
		NXTC.getDataRetrievedQueue().addNewItemListeners(PDP);

		while (connectionActive) {
			this.connectionActive = PDP.Active;

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}

		NXTC.Disconnect();
		NXTC = null;
	}
}
