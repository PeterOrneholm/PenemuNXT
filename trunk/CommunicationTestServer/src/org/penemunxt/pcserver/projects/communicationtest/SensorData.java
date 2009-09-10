package org.penemunxt.pcserver.projects.communicationtest;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.penemunxt.pcserver.communication.INXTCommunicationData;
import org.penemunxt.pcserver.communication.NXTCommunicationData;

public class SensorData extends NXTCommunicationData implements
		INXTCommunicationData {
	
	/* Read / Write */
	public void ReadData(DataInputStream DataIn) throws IOException {
		super.ReadData(DataIn);
		if (this.getDataStatus() != DATA_STATUS_EMPTY) {
			this.setIRDistance(DataIn.readInt());
			this.setUSDistance(DataIn.readInt());
			this.setSoundDB(DataIn.readInt());
		}
	}

	public void WriteData(DataOutputStream DataOut) throws IOException {
		super.WriteData(DataOut);
		if (this.getDataStatus() != DATA_STATUS_EMPTY) {
			DataOut.writeInt(this.getIRDistance());
			DataOut.writeInt(this.getUSDistance());
			DataOut.writeInt(this.getSoundDB());
		}
	}
	
	/* Constructors */

	public SensorData(int IRDistance, int USDistance, int SoundDB) {
		this();
		this.setIRDistance(IRDistance);
		this.setUSDistance(USDistance);
		this.setSoundDB(SoundDB);
	}

	public SensorData(int MainStatus, int DataStatus, boolean IsPrioritated) {
		super(MainStatus, DataStatus, IsPrioritated);
	}
	
	public SensorData(int MainStatus, int DataStatus) {
		super(MainStatus, DataStatus);
	}

	public SensorData() {
		super();
	}
	
	/* Params */
	
	int IRDistance;
	int USDistance;
	int SoundDB;

	public int getIRDistance() {
		return IRDistance;
	}

	public void setIRDistance(int distance) {
		IRDistance = distance;
	}

	public int getUSDistance() {
		return USDistance;
	}

	public void setUSDistance(int distance) {
		USDistance = distance;
	}

	public int getSoundDB() {
		return SoundDB;
	}

	public void setSoundDB(int soundDB) {
		SoundDB = soundDB;
	}	
}
