package org.penemunxt.communication.pc;

import java.io.IOException;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import org.penemunxt.communication.*;

public class PCDataStreamConnection extends DataStreamConnection {
	NXTConnector Connection;

	public NXTConnector getConnection() {
		return Connection;
	}

	public void setConnection(NXTConnector connection) {
		Connection = connection;
	}

	@Override
	public boolean Connect(NXTConnectionModes ConnMode, String Name,
			String Address) {
		Connection = new NXTConnector();
		boolean ConnectSuccess = false;
		int ConnectionMode = 0;

		if (ConnMode == NXTConnectionModes.USB) {
			ConnectionMode = NXTCommFactory.USB;
		} else if (ConnMode == NXTConnectionModes.Bluetooth) {
			ConnectionMode = NXTCommFactory.BLUETOOTH;
		}

		ConnectSuccess = Connection.connectTo(Name, Address, ConnectionMode);

		//
		
		this.setDataIn(Connection.getDataIn());
		this.setDataOut(Connection.getDataOut());
		
		return (Connection != null && ConnectSuccess);
	}
	
	@Override
	public void close(){
		try {
			this.Connection.close();
		} catch (IOException e) {
		}
	}
	
	public PCDataStreamConnection(){
		super();
	}
}
