package org.penemunxt.projects.communicationtest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.penemunxt.projects.communicationtest.ServerMessageData;
import org.penemunxt.communication.*;

public class ServerMessageData extends NXTCommunicationData implements
		INXTCommunicationData {

	/* Read / Write */
	@Override
	public void ReadData(DataInputStream DataIn) throws IOException {
		super.ReadData(DataIn);
		if (this.getDataStatus() == DATA_STATUS_NORMAL) {
			DataIn.readInt();
		}
	}

	@Override
	public void WriteData(DataOutputStream DataOut) throws IOException {
		super.WriteData(DataOut);
		if (this.getDataStatus() == DATA_STATUS_NORMAL) {
			DataOut.writeInt(100);
		}
	}

	/* Constructors */

	public ServerMessageData(int message) {
		this();
	}

	public ServerMessageData(int MainStatus, int DataStatus,
			boolean IsPrioritated) {
		super(MainStatus, DataStatus, IsPrioritated);
	}

	public ServerMessageData(int MainStatus, int DataStatus) {
		super(MainStatus, DataStatus);
	}

	public ServerMessageData() {
		super();
	}
}