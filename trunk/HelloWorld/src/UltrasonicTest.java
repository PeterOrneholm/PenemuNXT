import lejos.nxt.*;

public class UltrasonicTest implements Runnable {
	public static void main(String[] args) throws InterruptedException {
		UltrasonicTest m = new UltrasonicTest();
		m.run();
	}

	@Override
	public void run() {
		UltrasonicSensor sensor = new UltrasonicSensor(SensorPort.S1);

		while(!Button.ESCAPE.isPressed()) {
			LCD.clear();
			LCD.drawInt((sensor.getDistance()), 1, 1);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			LCD.refresh();
		}
		
	}
}
