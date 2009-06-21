import lejos.nxt.*;

public class Main implements Runnable {
	public static void main(String[] args) throws InterruptedException {
		Main m = new Main();
		m.run();
	}

	@Override
	public void run() {
		UltrasonicSensor sensor = new UltrasonicSensor(SensorPort.S1);

		while(!Button.ESCAPE.isPressed()) {
			LCD.clear();
			LCD.drawInt((sensor.getDistance()), 1, 1);
			System.out.println(sensor.getDistance());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			//LCD.refresh();
		}
		
	}
}
