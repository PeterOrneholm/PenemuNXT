package org.penemunxt.communication.nxt;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;

import org.penemunxt.communication.DataStreamConnection;
import org.penemunxt.communication.NXTConnectionModes;

public class NXTDataStreamConnection extends DataStreamConnection {
	NXTConnection Connection;

	@Override
	public boolean Connect(NXTConnectionModes ConnMode, String Name,
			String Address) {
		Connection = null;

		if (ConnMode == NXTConnectionModes.USB) {
			Connection = USB.waitForConnection();
		} else if (ConnMode == NXTConnectionModes.Bluetooth) {
			Connection = Bluetooth.waitForConnection();
		}

		if (Connection != null) {
			this.setDataIn(Connection.openDataInputStream());
			this.setDataOut(Connection.openDataOutputStream());
		}

		return (Connection != null);
	}

	@Override
	public void close() {
		this.Connection.close();
	}

	public NXTDataStreamConnection() {
		super();
	}
}
