package org.penemunxt.projects.penemunxtexplorer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.penemunxt.communication.INXTCommunicationData;
import org.penemunxt.communication.NXTCommunicationData;

public class RobotData extends NXTCommunicationData implements
		INXTCommunicationData, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2710524532782779030L;

	/* Read / Write */
	@Override
	public void ReadData(DataInputStream DataIn) throws IOException {
		super.ReadData(DataIn);
		if (this.getDataStatus() == DATA_STATUS_NORMAL) {
			this.setType(DataIn.readInt());
			this.setPosX(DataIn.readInt());
			this.setPosY(DataIn.readInt());
			this.setRobotHeading(DataIn.readInt());
			this.setHeadDistance(DataIn.readInt());
			this.setHeadHeading(DataIn.readInt());
			this.setBatteryLevel(DataIn.readInt());
			this.setCompassValues(DataIn.readInt());
			this.setTargetPosX(DataIn.readInt());
			this.setTargetPosY(DataIn.readInt());
			this.setUssDistance(DataIn.readInt());
		}
	}

	@Override
	public void WriteData(DataOutputStream DataOut) throws IOException {
		super.WriteData(DataOut);
		if (this.getDataStatus() == DATA_STATUS_NORMAL) {
			DataOut.writeInt(this.getType());
			DataOut.writeInt(this.getPosX());
			DataOut.writeInt(this.getPosY());
			DataOut.writeInt(this.getRobotHeading());
			DataOut.writeInt(this.getHeadDistance());
			DataOut.writeInt(this.getHeadHeading());
			DataOut.writeInt(this.getBatteryLevel());
			DataOut.writeInt(this.getCompassValues());
			DataOut.writeInt(this.getTargetPosX());
			DataOut.writeInt(this.getTargetPosY());
			DataOut.writeInt(this.getUssDistance());
		}
	}

	/* Constructors */

	public RobotData(int MainStatus, int DataStatus, boolean IsPrioritated) {
		super(MainStatus, DataStatus, IsPrioritated);
	}

	public RobotData(int type, int posX, int posY, int robotHeading,
			int headDistance, int headHeading, int batteryLevel, int compassValues, int targetPosX, int targetPosY, int ussDistance) {
		super();
		PositionType = type;
		PosX = posX;
		PosY = posY;
		RobotHeading = robotHeading;
		HeadDistance = headDistance;
		HeadHeading = headHeading;
		BatteryLevel = batteryLevel;
		CompassValues = compassValues;
		TargetPosX = targetPosX;
		TargetPosY = targetPosY;
		UssDistance = ussDistance;
	}

	public RobotData(int MainStatus, int DataStatus) {
		super(MainStatus, DataStatus);
	}

	public RobotData() {
		super();
	}

	/* Params */

	public final static int POSITION_TYPE_DRIVE = 100;
	public final static int POSITION_TYPE_BUMP_BUMPER = 200;
	public final static int POSITION_TYPE_BUMP_DISTANCE = 300;
	public final static int POSITION_TYPE_ALIGNED = 400;
	public final static int POSITION_TYPE_RIGHT_CORNER = 500;
	public final static int POSITION_TYPE_NOT_VALID = 600;

	int PositionType;
	int PosX;
	int PosY;
	int RobotHeading;
	int HeadDistance;
	int HeadHeading;
	int TargetPosX;
	int TargetPosY;
	int UssDistance;
	int BatteryLevel;
	int CompassValues;

	public int getPositionType() {
		return PositionType;
	}

	public void setPositionType(int positionType) {
		PositionType = positionType;
	}

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

	public int getUssDistance() {
		return UssDistance;
	}

	public void setUssDistance(int ussDistance) {
		UssDistance = ussDistance;
	}

	public int getBatteryLevel() {
		return BatteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		BatteryLevel = batteryLevel;
	}

	public int getCompassValues() {
		return CompassValues;
	}

	public void setCompassValues(int compassValues) {
		CompassValues = compassValues;
	}

	public int getType() {
		return PositionType;
	}

	public void setType(int type) {
		PositionType = type;
	}

	public int getPosX() {
		return PosX;
	}

	public void setPosX(int posX) {
		PosX = posX;
	}

	public int getPosY() {
		return PosY;
	}

	public void setPosY(int posY) {
		PosY = posY;
	}

	public int getRobotHeading() {
		return RobotHeading;
	}

	public void setRobotHeading(int robotHeading) {
		RobotHeading = robotHeading;
	}

	public int getHeadDistance() {
		return HeadDistance;
	}

	public void setHeadDistance(int headDistance) {
		HeadDistance = headDistance;
	}

	public int getHeadHeading() {
		return HeadHeading;
	}

	public void setHeadHeading(int headHeading) {
		HeadHeading = headHeading;
	}
}
