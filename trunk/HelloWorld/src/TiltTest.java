import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.TiltSensor;

public class TiltTest {
	public void run() {
		TiltSensor TS = new TiltSensor(SensorPort.S3);
		while (true){
			LCD.drawInt(TS.getXAccel(), 2, 1);
			LCD.drawInt(TS.getYAccel(), 2, 2);
			LCD.drawInt(TS.getZAccel(), 2, 3);
			LCD.drawInt(TS.getZTilt(), 2, 4);
			LCD.drawInt(TS.getZTilt(), 2, 5);
			LCD.drawInt(TS.getZTilt(), 2, 6);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}
}
