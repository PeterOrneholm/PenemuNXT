package org.penemunxt.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface INXTDataStreamConnection {
	public DataOutputStream getDataOut();

	public void setDataOut(DataOutputStream dataOut);

	public DataInputStream getDataIn();

	public void setDataIn(DataInputStream dataIn);

	public boolean Connect(NXTConnectionModes ConnMode, String Name,
			String Address);

	public void close();
}
