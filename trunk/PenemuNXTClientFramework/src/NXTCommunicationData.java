import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NXTCommunicationData {
	final static int STATUS_NORMAL = 100;
	final static int STATUS_ERROR = 200;

	final static int DATA_EMPTY_DATA = 0;
	final static int DATA_NORMAL_DATA = 1;

	int MainStatus;
	int DataStatus;

	public Integer getMainStatus() {
		return MainStatus;
	}

	public void setMainStatus(int mainStatus) {
		MainStatus = mainStatus;
	}

	public int getDataStatus() {
		return DataStatus;
	}

	public void setDataStatus(int dataStatus) {
		DataStatus = dataStatus;
	}

	public void ReadData(DataInputStream DataIn) throws IOException {
		this.MainStatus = DataIn.readInt();
		this.DataStatus = DataIn.readInt();
	}

	public void WriteData(DataOutputStream DataOut) throws IOException {
		DataOut.writeInt(this.MainStatus);
		DataOut.writeInt(this.DataStatus);
	}
	
	public NXTCommunicationData(int MainStatus, int DataStatus) {
		this.MainStatus = MainStatus;
		this.DataStatus = DataStatus;
	}

	public NXTCommunicationData() {
		this(STATUS_NORMAL, DATA_EMPTY_DATA);
	}
}