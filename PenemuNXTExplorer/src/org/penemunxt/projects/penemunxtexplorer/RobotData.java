package org.penemunxt.projects.penemunxtexplorer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.penemunxt.communication.INXTCommunicationData;
import org.penemunxt.communication.NXTCommunicationData;

public class RobotData extends NXTCommunicationData implements
		INXTCommunicationData {

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
		}
	}

	/* Constructors */

	public RobotData(int MainStatus, int DataStatus, boolean IsPrioritated) {
		super(MainStatus, DataStatus, IsPrioritated);
	}

	public RobotData(int type, int posX, int posY, int robotHeading,
			int headDistance, int headHeading) {
		super();
		Type = type;
		PosX = posX;
		PosY = posY;
		RobotHeading = robotHeading;
		HeadDistance = headDistance;
		HeadHeading = headHeading;
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

	int Type;
	int PosX;
	int PosY;
	int RobotHeading;
	int HeadDistance;
	int HeadHeading;

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
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
