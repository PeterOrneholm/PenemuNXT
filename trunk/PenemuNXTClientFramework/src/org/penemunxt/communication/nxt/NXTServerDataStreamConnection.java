package org.penemunxt.communication.nxt;

import lejos.nxt.comm.*;
import org.penemunxt.communication.*;

public class NXTServerDataStreamConnection extends NXTDataStreamConnection {
	NXTConnection Connection;

	@Override
	public boolean Connect(NXTConnectionModes ConnMode, String Name,
			String Address) {
		Connection = null;
		NXTConnection NewConn = null;
		if (ConnMode == NXTConnectionModes.USB) {
			NewConn = USB.waitForConnection();
		} else if (ConnMode == NXTConnectionModes.Bluetooth) {
			NewConn = Bluetooth.waitForConnection();
		}

		if (NewConn != null) {
			this.setDataIn(Connection.openDataInputStream());
			this.setDataOut(Connection.openDataOutputStream());
			Connection = NewConn;
		}

		return (Connection != null);
	}

	@Override
	public void close() {
		this.Connection.close();
	}

	public NXTServerDataStreamConnection() {
		super();
	}
}
