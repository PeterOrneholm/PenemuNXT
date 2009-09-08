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
			this.setMessage(DataIn.readUTF());
		}
	}

	public void WriteData(DataOutputStream DataOut) throws IOException {
		super.WriteData(DataOut);
		if (this.getDataStatus() != DATA_STATUS_EMPTY) {
			DataOut.writeUTF(this.getMessage());
		}
	}
	
	/* Constructors */

	public ServerMessageData(String message) {
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
	
	String Message;

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
}
