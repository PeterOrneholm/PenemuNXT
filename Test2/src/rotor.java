import java.util.ArrayList;
import lejos.nxt.*;

public class rotor {
	public static void main(String[] args) {
		RotorApp RA = new RotorApp();

		RA.run();
	}
}

class RotorApp implements ButtonListener, Runnable {
	HeadRotator HR = new HeadRotator();
	UltrasonicDataCollector USDC = new UltrasonicDataCollector();

	public void run() {
		Button.ESCAPE.addButtonListener(this);

		HR.start();
		USDC.start();
	}

	@Override
	public void buttonPressed(Button arg0) {
	}

	@Override
	public void buttonReleased(Button arg0) {
		Sound.beep();

		USDC.interrupt();
		HR.interrupt();
		Motor.A.stop();
		Motor.A.rotateTo(0);
		while (Motor.A.getTachoCount() != 0){}

		System.exit(0);
	}
}

class UltrasonicDataCollector extends Thread {
	// ArrayList<UltraSonicData> USDAL = new ArrayList<UltraSonicData>();
	UltrasonicSensor USS = new UltrasonicSensor(SensorPort.S1);

	@Override
	public void run() {
		// LCD.clear();
		Sound.beep();
		int x;
		int y;
		int dist, distx, disty;
		int angle;
		
		while (!this.isInterrupted()) {
			dist = USS.getDistance();
			angle = Motor.A.getTachoCount();
			// USDAL.add(new UltraSonicData(Motor.A.getTachoCount(),
			// USS.getDistance()));
			distx = (int) (((Math.min(200.0, dist) / 200.0) * (LCD.SCREEN_WIDTH / 2)));
			disty = (int) ((Math.min(200.0, dist) / 200.0) * LCD.SCREEN_HEIGHT);

			x = (int) (distx * Math.cos(angle) + (LCD.SCREEN_WIDTH / 2));
			y = (int) (-1*(disty * Math.sin(angle))+LCD.SCREEN_HEIGHT);
			LCD.setPixel(1, x, y);
			LCD.drawString("    ", 1, 1);
			LCD.drawString("    ", 1, 2);
			LCD.drawInt(x, 1, 1);
			LCD.drawInt(y, 1, 2);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}
}

class HeadRotator extends Thread {
	@Override
	public void run() {
		Motor.A.setSpeed(40);
		while (!this.isInterrupted()) {
			Motor.A.rotateTo(-90);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			Motor.A.rotateTo(90);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {

			}
		}

	}
}

class UltraSonicData {
	public int Angle;
	public int Distance;

	public UltraSonicData(int Angle, int Distance) {
		this.Angle = Angle;
		this.Distance = Distance;
	}
}