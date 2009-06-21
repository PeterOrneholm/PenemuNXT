import java.io.*;

import lejos.nxt.*;
import lejos.nxt.comm.*;

public class USBConnTC {
	public static void main(String[] args) throws Exception {
		LCD.drawString("Waiting...", 0, 0);
		LCD.refresh();
		BTConnection conn = Bluetooth.waitForConnection();
		
		DataOutputStream outDat = conn.openDataOutputStream();
		DataInputStream inDat = conn.openDataInputStream();

		TouchSensor TS = new TouchSensor(SensorPort.S1);
		SoundSensor SS = new SoundSensor(SensorPort.S2);
		UltrasonicSensor USS = new UltrasonicSensor(SensorPort.S3);

		int status = 0;
		
		LCD.clear();
		LCD.drawString("Connected!", 0, 0);
		LCD.refresh();

		while (!TS.isPressed() && status!=1) {
			try {
				outDat.writeInt(SS.readValue());
				outDat.writeInt(USS.getDistance());

				outDat.flush();
			} catch (IOException ioe) {
				status = 1;
				System.err.println("IO Exception Closing connection");
			}

			try {
				status = inDat.readInt();
			} catch (IOException ioe) {
				status = 1;
				System.err.println("IO Exception Closing connection");
			}
		}

		try {
			outDat.writeInt(1001);
			outDat.flush();
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}

		try {
			inDat.close();
			outDat.close();
			System.out.println("Closed data streams");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}

		conn.close();

	}
}
