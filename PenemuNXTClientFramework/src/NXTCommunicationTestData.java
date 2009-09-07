import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NXTCommunicationTestData extends NXTCommunicationData {
	public void ReadData(DataInputStream DataIn) throws IOException {
		super.ReadData(DataIn);
		this.Param1 = DataIn.readInt();
		this.Param2 = DataIn.readInt();
	}

	public void WriteData(DataOutputStream DataOut) throws IOException {
		super.WriteData(DataOut);
		NXTDebug.WriteMessageAndWait("Write sub");
		DataOut.writeInt(this.Param1);
		DataOut.writeInt(this.Param2);
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

	public NXTCommunicationTestData(int OverallStatus, int DataStatus,
			int Param1, int Param2) {
		super(OverallStatus, DataStatus);
		this.Param1 = Param1;
		this.Param2 = Param2;
	}

	public NXTCommunicationTestData() {
		super();
	}

	public NXTCommunicationTestData(int MainStatus, int DataStatus) {
		super(MainStatus, DataStatus);
	}
}
