package org.penemunxt.nxtclient.projects.communicationtest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.penemunxt.nxtclient.communication.INXTCommunicationData;
import org.penemunxt.nxtclient.communication.NXTCommunicationData;
import org.penemunxt.nxtclient.projects.communicationtest.ServerMessageData;

public class ServerMessageData extends NXTCommunicationData implements
		INXTCommunicationData {
	
	final static int SOUND_TOO_HIGH = 100;
	final static int SOUND_MEDIUM = 200;
	final static int SOUND_TOO_LOW = 300;
	
	/* Read / Write */
	public void ReadData(DataInputStream DataIn) throws IOException {
		super.ReadData(DataIn);
		if (this.getDataStatus() == DATA_STATUS_NORMAL) {
			this.setMessage(DataIn.readInt());
		}
	}

	public void WriteData(DataOutputStream DataOut) throws IOException {
		super.WriteData(DataOut);
		if (this.getDataStatus() == DATA_STATUS_NORMAL) {
			DataOut.writeInt(this.getMessage());
		}
	}
	
	/* Constructors */

	public ServerMessageData(int message) {
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
	
	int Message;

	public int getMessage() {
		return Message;
	}

	public void setMessage(int message) {
		Message = message;
	}
	
	/* Help functions */
	public String getMessageDescription() {
		return ServerMessageData.getMessageDescription(this.Message);
	}
	
	public static String getMessageDescription(int message) {
		switch (message) {
		case ServerMessageData.SOUND_TOO_HIGH:
			return "Too high!";
		case ServerMessageData.SOUND_MEDIUM:
			return "Medium :)";
		case ServerMessageData.SOUND_TOO_LOW:
			return "Too low..";
		default:
			return "Unknown";
		}
	}
}
