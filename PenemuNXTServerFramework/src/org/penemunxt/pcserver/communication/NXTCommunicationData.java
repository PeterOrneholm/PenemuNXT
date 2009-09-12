package org.penemunxt.pcserver.communication;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NXTCommunicationData implements INXTCommunicationData {	
	public final static int MAIN_STATUS_NORMAL = 100;
	public final static int MAIN_STATUS_SHUT_DOWN = 200;
	public final static int MAIN_STATUS_SHUTTING_DOWN = 210;

	public final static int DATA_STATUS_EMPTY = 0;
	public final static int DATA_STATUS_NORMAL = 100;
	public final static int DATA_STATUS_ONLY_STATUS = 200;

	int MainStatus;
	int DataStatus;
	boolean IsPrioritated;

	public Integer getMainStatus() {
		return MainStatus;
	}

	public void setMainStatus(int mainStatus) {
		MainStatus = mainStatus;
	}

	public boolean isPrioritated() {
		return IsPrioritated;
	}

	public void setPrioritated(boolean isPrioritated) {
		IsPrioritated = isPrioritated;
	}

	public int getDataStatus() {
		return DataStatus;
	}

	public void setDataStatus(int dataStatus) {
		DataStatus = dataStatus;
	}

	public void ReadData(DataInputStream DataIn) throws IOException {
		this.IsPrioritated = DataIn.readBoolean();
		this.MainStatus = DataIn.readInt();
		this.DataStatus = DataIn.readInt();
	}

	public void WriteData(DataOutputStream DataOut) throws IOException {
		DataOut.writeBoolean(this.IsPrioritated);
		DataOut.writeInt(this.MainStatus);
		DataOut.writeInt(this.DataStatus);
	}
	
	public NXTCommunicationData(int MainStatus, int DataStatus, boolean IsPrioritated) {
		super();
		this.IsPrioritated = IsPrioritated;
		this.MainStatus = MainStatus;
		this.DataStatus = DataStatus;
	}
	
	public NXTCommunicationData(int MainStatus, int DataStatus) {
		this(MainStatus, DataStatus, false);
	}

	public NXTCommunicationData() {
		this(MAIN_STATUS_NORMAL, DATA_STATUS_NORMAL);
	}
}