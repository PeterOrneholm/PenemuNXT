import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NXTCommunicationTestData extends NXTCommunicationData {
	public void ReadData(DataInputStream DataIn) {
		// Implement this in a subclass
	}

	public void WriteData(DataOutputStream DataOut) throws IOException {
		DataOut.writeInt(this.OverallStatus);
		DataOut.writeInt(this.DataStatus);
		DataOut.writeInt(this.Param1);
		DataOut.writeInt(this.Param2);
	}

	int OverallStatus;
	int DataStatus;
	int Param1;
	int Param2;

	public Integer getOverallStatus() {
		return OverallStatus;
	}

	public void setOverallStatus(int overallStatus) {
		OverallStatus = overallStatus;
	}

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

	public NXTCommunicationTestData(int OverallStatus, int DataStatus,
			int Param1, int Param2) {
		super();
		this.OverallStatus = OverallStatus;
		this.DataStatus = DataStatus;
		this.Param1 = Param1;
		this.Param2 = Param2;
	}

	public NXTCommunicationTestData(int OverallStatus, int DataStatus) {
		this(OverallStatus, DataStatus, 0, 0);
	}

	public NXTCommunicationTestData() {
		this(STATUS_NORMAL, DATA_EMPTY_DATA);
	}
}
