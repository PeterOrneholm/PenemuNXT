package org.penemunxt.pcserver.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;

public class NXTDataStreamConnection {
	DataOutputStream DataOut;
	DataInputStream DataIn;
	NXTConnector Connection;
	
	public DataOutputStream getDataOut() {
		return DataOut;
	}

	public void setDataOut(DataOutputStream dataOut) {
		DataOut = dataOut;
	}

	public DataInputStream getDataIn() {
		return DataIn;
	}

	public void setDataIn(DataInputStream dataIn) {
		DataIn = dataIn;
	}
	
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

		this.setDataIn(Connection.getDataIn());
		this.setDataOut(Connection.getDataOut());
		
		return (Connection != null && ConnectSuccess);
	}
	
	public void close(){
		try {
			this.Connection.close();
		} catch (IOException e) {
		}
	}
	
	public NXTDataStreamConnection(){
		super();
	}
}
