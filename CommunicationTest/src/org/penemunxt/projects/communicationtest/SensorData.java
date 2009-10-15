package org.penemunxt.projects.communicationtest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.penemunxt.communication.*;

public class SensorData extends NXTCommunicationData implements
		INXTCommunicationData {

	/* Read / Write */
	@Override
	public void ReadData(DataInputStream DataIn) throws IOException {
		super.ReadData(DataIn);
		if (this.getDataStatus() == DATA_STATUS_NORMAL) {
			this.setIRDistance(DataIn.readInt());
			this.setSoundDB(DataIn.readInt());
			this.setAccX(DataIn.readInt());
			this.setAccY(DataIn.readInt());
			this.setAccZ(DataIn.readInt());
		}
	}

	@Override
	public void WriteData(DataOutputStream DataOut) throws IOException {
		super.WriteData(DataOut);
		if (this.getDataStatus() == DATA_STATUS_NORMAL) {
			DataOut.writeInt(this.getIRDistance());
			DataOut.writeInt(this.getSoundDB());
			DataOut.writeInt(this.getAccX());
			DataOut.writeInt(this.getAccY());
			DataOut.writeInt(this.getAccZ());
		}
	}

	/* Constructors */

	public SensorData(int IRDistance, int SoundDB, int AccX, int AccY, int AccZ) {
		this();
		this.setIRDistance(IRDistance);
		this.setSoundDB(SoundDB);
		this.setAccX(AccX);
		this.setAccY(AccY);
		this.setAccZ(AccZ);
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
	int SoundDB;
	int AccX;
	int AccY;
	int AccZ;

	public int getIRDistance() {
		return IRDistance;
	}

	public void setIRDistance(int distance) {
		IRDistance = distance;
	}

	public int getSoundDB() {
		return SoundDB;
	}

	public void setSoundDB(int soundDB) {
		SoundDB = soundDB;
	}

	public int getAccX() {
		return AccX;
	}

	public void setAccX(int accX) {
		AccX = accX;
	}

	public int getAccY() {
		return AccY;
	}

	public void setAccY(int accY) {
		AccY = accY;
	}

	public int getAccZ() {
		return AccZ;
	}

	public void setAccZ(int accZ) {
		AccZ = accZ;
	}
}
