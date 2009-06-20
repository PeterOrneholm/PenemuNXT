import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;

public class USBConnTC {
	public static void main(String[] args) throws Exception {
		LCD.drawString("Waiting...", 0, 0);
		LCD.refresh();
		NXTConnection conn = USB.waitForConnection();

		
		DataOutputStream dOut = conn.openDataOutputStream();
		DataInputStream dIn = conn.openDataInputStream();

		TouchSensor TS = new TouchSensor(SensorPort.S1);
		SoundSensor SS = new SoundSensor(SensorPort.S2);

		LCD.clear();
		LCD.drawString("Connected!", 0, 0);
		LCD.refresh();

		while (!TS.isPressed()) {
			dOut.writeInt(SS.readValue());
			dOut.flush();
		}

		dOut.writeInt(101);
		dOut.flush();
	}
}
