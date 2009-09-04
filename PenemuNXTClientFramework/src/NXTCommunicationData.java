import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class NXTCommunicationData {
	final static int STATUS_NORMAL = 100;
	final static int STATUS_ERROR = 200;

	final static int DATA_EMPTY_DATA = 0;
	final static int DATA_NORMAL_DATA = 1;

	public void ReadData(DataInputStream DataIn) throws IOException {
		// Implement this in a subclass
	}

	public void WriteData(DataOutputStream DataOut) throws IOException {
		// Implement this in a subclass
	}
}
