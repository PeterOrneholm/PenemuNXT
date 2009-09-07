package Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Communication.INXTCommunicationData;
import Communication.NXTCommunicationData;
import Debug.NXTDebug;

public class ProcessedData extends NXTCommunicationData implements
		INXTCommunicationData {
	public void ReadData(DataInputStream DataIn) throws IOException {
		super.ReadData(DataIn);
		if (this.getDataStatus() != DATA_STATUS_EMPTY) {
			this.Sum = DataIn.readInt();
		}
	}

	public void WriteData(DataOutputStream DataOut) throws IOException {
		super.WriteData(DataOut);
		if (this.getDataStatus() != DATA_STATUS_EMPTY) {
			DataOut.writeInt(this.Sum);
		}
	}

	int Sum;

	public Integer getSum() {
		return Sum;
	}

	public void setSum(int sum) {
		Sum = sum;
	}

	public ProcessedData(int OverallStatus, int DataStatus, int Sum) {
		super(OverallStatus, DataStatus);
		this.Sum = Sum;
	}

	public ProcessedData() {
		super();
	}

	public ProcessedData(int MainStatus, int DataStatus) {
		super(MainStatus, DataStatus);
	}
	
	public ProcessedData(int MainStatus, int DataStatus, boolean IsPrioritated) {
		super(MainStatus, DataStatus, IsPrioritated);
	}
}
