import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.OpticalDistanceSensor;;;

public class IRDistance implements Runnable {
	public static void main(String[] args) throws InterruptedException {
		IRDistance m = new IRDistance();
		m.run();
	}

	@Override
	public void run() {
		
		OpticalDistanceSensor sensor = new OpticalDistanceSensor(SensorPort.S1);
		int ClosestAngle = 0;
		int ClosestDist = 0;
		Motor.C.setSpeed(300);
		Motor.C.resetTachoCount();
		while(!Button.ESCAPE.isPressed()) {
			Motor.C.rotateTo(-1450);
			
			Motor.C.rotateTo(1450, true);
			while(Motor.C.getTachoCount() < 1400){
				if(ClosestDist == 0 || sensor.getDistance() < ClosestDist){
					ClosestAngle = Motor.C.getTachoCount();
					ClosestDist = sensor.getDistance();
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
			}
			
			LCD.clear();
			LCD.drawInt(ClosestDist, 1, 1);
			LCD.drawInt(ClosestAngle, 1, 2);
			LCD.refresh();

			Motor.C.rotateTo(ClosestAngle);
			try {
				Button.ESCAPE.waitForPressAndRelease();
			} catch (InterruptedException e) {
			}
		}
		
	}
}
