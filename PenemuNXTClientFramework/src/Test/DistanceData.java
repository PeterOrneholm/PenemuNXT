package Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Communication.INXTCommunicationData;
import Communication.NXTCommunicationData;
import Debug.NXTDebug;

public class DistanceData extends NXTCommunicationData implements
		INXTCommunicationData {
	public void ReadData(DataInputStream DataIn) throws IOException {
		super.ReadData(DataIn);
		if (this.getDataStatus() != DATA_STATUS_EMPTY) {
			this.Param1 = DataIn.readInt();
			this.Param2 = DataIn.readInt();
		}
	}

	public void WriteData(DataOutputStream DataOut) throws IOException {
		super.WriteData(DataOut);
		if (this.getDataStatus() != DATA_STATUS_EMPTY) {
			DataOut.writeInt(this.Param1);
			DataOut.writeInt(this.Param2);
		}
	}

	int Param1;
	int Param2;

	public Integer getParam1() {
		return Param1;
	}

	public void setParam1(int param1) {
		Param1 = param1;
	}

	public Integer getParam2() {
		return Param2;
	}

	public void setParam2(int param2) {
		Param2 = param2;
	}

	public DistanceData(int OverallStatus, int DataStatus, int Param1,
			int Param2) {
		super(OverallStatus, DataStatus);
		this.Param1 = Param1;
		this.Param2 = Param2;
	}

	public DistanceData() {
		super();
	}

	public DistanceData(int MainStatus, int DataStatus) {
		super(MainStatus, DataStatus);
	}

	public DistanceData(int MainStatus, int DataStatus, boolean IsPrioritated) {
		super(MainStatus, DataStatus, IsPrioritated);
	}
}
