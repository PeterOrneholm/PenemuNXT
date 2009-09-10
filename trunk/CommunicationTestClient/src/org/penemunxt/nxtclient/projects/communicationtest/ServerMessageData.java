package org.penemunxt.nxtclient.projects.communicationtest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.penemunxt.nxtclient.communication.INXTCommunicationData;
import org.penemunxt.nxtclient.communication.NXTCommunicationData;

public class ServerMessageData extends NXTCommunicationData implements
		INXTCommunicationData {
	
	/* Read / Write */
	public void ReadData(DataInputStream DataIn) throws IOException {
		super.ReadData(DataIn);
		if (this.getDataStatus() != DATA_STATUS_EMPTY) {
			this.setMessage(DataIn.readFloat());
		}
	}

	public void WriteData(DataOutputStream DataOut) throws IOException {
		super.WriteData(DataOut);
		if (this.getDataStatus() != DATA_STATUS_EMPTY) {
			DataOut.writeFloat(this.getMessage());
		}
	}
	
	/* Constructors */

	public ServerMessageData(float message) {
		this();
		this.setMessage(message);
	}

	public ServerMessageData(int MainStatus, int DataStatus, boolean IsPrioritated) {
		super(MainStatus, DataStatus, IsPrioritated);
	}
	
	public ServerMessageData(int MainStatus, int DataStatus) {
		super(MainStatus, DataStatus);
	}

	public ServerMessageData() {
		super();
	}
	
	/* Params */
	
	float Message;

	public float getMessage() {
		return Message;
	}

	public void setMessage(float message) {
		Message = message;
	}
}
