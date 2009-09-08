package org.penemunxt.nxtclient.communication;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface INXTCommunicationData {
	public Integer getMainStatus();
	public void setMainStatus(int mainStatus);

	public int getDataStatus();
	public void setDataStatus(int dataStatus);

	public boolean isPrioritated();
	public void setPrioritated(boolean isPrioritated);
	
	public void ReadData(DataInputStream DataIn) throws IOException;
	public void WriteData(DataOutputStream DataOut) throws IOException;
}
