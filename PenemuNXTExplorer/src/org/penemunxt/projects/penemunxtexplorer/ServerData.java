package org.penemunxt.projects.penemunxtexplorer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.penemunxt.communication.INXTCommunicationData;
import org.penemunxt.communication.NXTCommunicationData;

public class ServerData extends NXTCommunicationData implements
		INXTCommunicationData {

	/* Read / Write */
	@Override
	public void ReadData(DataInputStream DataIn) throws IOException {
		super.ReadData(DataIn);
		if (this.getDataStatus() == DATA_STATUS_NORMAL) {
			this.setTargetPosX(DataIn.readInt());
			this.setTargetPosY(DataIn.readInt());
		}
	}

	@Override
	public void WriteData(DataOutputStream DataOut) throws IOException {
		super.WriteData(DataOut);
		if (this.getDataStatus() == DATA_STATUS_NORMAL) {
			DataOut.writeInt(this.getTargetPosX());
			DataOut.writeInt(this.getTargetPosY());
		}
	}

	/* Constructors */

	public ServerData(int targetPosX, int targetPosY, int command) {
		this();
		TargetPosX = targetPosX;
		TargetPosY = targetPosY;
	}

	public ServerData(int MainStatus, int DataStatus,
			boolean IsPrioritated) {
		super(MainStatus, DataStatus, IsPrioritated);
	}

	public ServerData(int MainStatus, int DataStatus) {
		super(MainStatus, DataStatus);
	}

	public ServerData() {
		super();
	}
	
	/* Params */
	
	int TargetPosX;
	int TargetPosY;
	
	public int getTargetPosX() {
		return TargetPosX;
	}

	public void setTargetPosX(int targetPosX) {
		TargetPosX = targetPosX;
	}

	public int getTargetPosY() {
		return TargetPosY;
	}

	public void setTargetPosY(int targetPosY) {
		TargetPosY = targetPosY;
	}
}