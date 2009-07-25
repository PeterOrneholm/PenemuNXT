import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassSensor;

public class CompassTest implements Runnable {
	public static void main(String[] args) throws InterruptedException {
		CompassTest m = new CompassTest();
		m.run();
	}

	@Override
	public void run() {
		int Tolerance = 5;
		int HeadFor;
		CompassSensor sensor = new CompassSensor(SensorPort.S1);
		Motor.A.setSpeed(5);
		sensor.resetCartesianZero();
		
		HeadFor = (int) sensor.getDegreesCartesian();

		while(!Button.ESCAPE.isPressed()) {
			LCD.drawInt((int) HeadFor, 1, 1);
			LCD.drawInt((int) Motor.A.getTachoCount(), 1, 2);
			LCD.drawInt((int) sensor.getDegreesCartesian(), 1, 3);

			if(sensor.getDegreesCartesian() > (HeadFor + Tolerance)){
				LCD.drawInt(1, 1, 4);
				Motor.A.backward();
			}else if(sensor.getDegreesCartesian() < (HeadFor - Tolerance)){
				LCD.drawInt(2, 1, 4);
				Motor.A.forward();
			}else{
				Motor.A.stop();
				LCD.drawInt(0, 1, 4);
			}
			
			LCD.refresh();
			
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
			}
			
			LCD.clear();
		}
		
	}
}
