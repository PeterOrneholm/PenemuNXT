import java.util.ArrayList;

import lejos.localization.Point;
import lejos.nxt.*;

import javax.microedition.lcdui.*;

public class UltrasonicRotator {
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
		Button.ENTER.addButtonListener(this);

		HR.start();
		USDC.start();
	}

	@Override
	public void buttonPressed(Button arg0) {
	}

	@Override
	public void buttonReleased(Button btn) {
		if(btn==Button.ESCAPE){
			Sound.beep();

			USDC.interrupt();
			HR.interrupt();
			//Motor.A.stop();
			Motor.A.rotateTo(0);
			while (Motor.A.getTachoCount() != 0) {
			}
			
			System.exit(0);
		}else if(btn==Button.ENTER){
			USDC.clearPoints();
			LCD.refresh();
		}
	}
}

class UltrasonicDataCollector extends Thread {
	// ArrayList<UltraSonicData> USDAL = new ArrayList<UltraSonicData>();
	// USDAL.add(new UltraSonicData(Motor.A.getTachoCount(),
	// USS.getDistance()));

	boolean doClearNext = false;
	
	public void clearPoints() {
		doClearNext = true;
	}
	
	UltrasonicSensor USS = new UltrasonicSensor(SensorPort.S1);
	ArrayList<Point> Points = new ArrayList<Point>();
	
	private Point getScreenPos(int Distance, int Angle){
		int x, y;
		int distx, disty;
		
		distx = (int) (((Math.min(200.0, Distance) / 200.0) * (LCD.SCREEN_WIDTH / 2)));
		disty = (int) ((Math.min(200.0, Distance) / 200.0) * LCD.SCREEN_HEIGHT);

		x = (int) ((distx * Math.cos((Angle) * Math.PI / 180)) + (LCD.SCREEN_WIDTH / 2));
		x = LCD.SCREEN_WIDTH + (x*-1);
		y = (int) (-1 * (disty * Math.sin(Angle * Math.PI / 180)) + LCD.SCREEN_HEIGHT);
		
		return new Point(x, y);
	}
	
	@Override
	public void run() {
		Point curPosition;
		Point curScanPosition;
		Graphics GFX = new Graphics();

		while (!this.isInterrupted()) {
			LCD.clear();
			if(doClearNext){
				Points.clear();
				doClearNext = false;
			}
			
			curPosition = this.getScreenPos(
								USS.getDistance(),
								Motor.A.getTachoCount() + 90);

			curScanPosition = this.getScreenPos(
					200,
					Motor.A.getTachoCount() + 90);
			
			Points.add(curPosition);
			GFX.drawLine(
					(LCD.SCREEN_WIDTH/2),
					LCD.SCREEN_HEIGHT,
					(int) curScanPosition.x,
					(int) curScanPosition.y);
			
			for(Point p: Points){
				LCD.setPixel(1, (int) p.x, (int) p.y);	
			}

			Thread.yield();
		}
	}
}

class HeadRotator extends Thread {
	@Override
	public void run() {
		Motor.A.setSpeed(40);
		while (!this.isInterrupted()) {
			Motor.A.rotateTo(-100, true);
			Sound.beep();
			while(Motor.A.getTachoCount() >=-90 && !this.isInterrupted()){
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			Sound.beep();
			Motor.A.rotateTo(100, true);
			while(Motor.A.getTachoCount() <= 90 && !this.isInterrupted()){
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {

			}
			Sound.beep();
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