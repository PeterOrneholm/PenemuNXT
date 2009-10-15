package org.penemunxt.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class NXTDataStreamConnection implements INXTDataStreamConnection {
	DataOutputStream DataOut;
	DataInputStream DataIn;

	public DataOutputStream getDataOut() {
		return DataOut;
	}

	public void setDataOut(DataOutputStream dataOut) {
		this.DataOut = dataOut;
	}

	public DataInputStream getDataIn() {
		return DataIn;
	}

	public void setDataIn(DataInputStream dataIn) {
		this.DataIn = dataIn;
	}

	public boolean Connect(NXTConnectionModes ConnMode, String Name,
			String Address) {
		return false;
	}

	public void close() {
	}

	public NXTDataStreamConnection() {
		super();
	}
}
