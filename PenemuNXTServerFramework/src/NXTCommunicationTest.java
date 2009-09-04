import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;

public class NXTCommunicationTest {
	final static int ConnectionMode = NXTCommFactory.USB;
	static boolean Active = true;
	public static void main(String args[]) {
		NXTConnector conn = new NXTConnector();

		switch (ConnectionMode) {
		case NXTCommFactory.BLUETOOTH:
			if (!conn.connectTo("btspp://")) {
				System.err.println("No NXT found by Bluetooth");
				System.exit(1);
			}
			break;
		case NXTCommFactory.USB:
			if (!conn.connectTo("usb://")) {
				System.err.println("No NXT found by USB");
				System.exit(1);
			}
			break;
		}

		DataInputStream inDat = conn.getDataIn();
		DataOutputStream outDat = conn.getDataOut();

		int V1 = 0;
		int V2 = 0;
		int V3 = 0;
		int V4 = 0;

		while (Active) {
			try {
				V1 = inDat.readInt();
				V2 = inDat.readInt();
				V3 = inDat.readInt();
				V4 = inDat.readInt();
			} catch (IOException ioe) {
				System.err.println("IO Exception reading reply");
			}

			System.err.println(V1 + "," + V2 + "," + V3 + "," + V4);
			
			try {
				outDat.writeInt(-V1);
				outDat.writeInt(-V2);
				outDat.writeInt(-V3);
				outDat.writeInt(-V4);
				outDat.flush();
			} catch (IOException ioe) {
				System.err.println("IO Exception writing reply");
			}
			Thread.yield();
		}

		try {
			inDat.close();
			outDat.close();
			System.out.println("Closed data streams");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}

		try {
			conn.close();
			System.out.println("Closed connection");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}
	}
}
