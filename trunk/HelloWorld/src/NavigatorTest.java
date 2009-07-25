import lejos.navigation.CompassNavigator;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassSensor;

public class NavigatorTest {
	
	public static void main(String args[]) throws InterruptedException{
		DataExchange nDE = new DataExchange();
		
		WriteDebug WD = new WriteDebug(nDE);
		Mover M = new Mover(nDE);
		
		WD.start();
		M.start();
	}
}

class DataExchange{
	public CompassNavigator CN;
}

 class Mover extends Thread {
	
	private DataExchange DE;
	
	public Mover(DataExchange DE){
		this.DE = DE;
	}
	
	@Override
	public void run()  {		
		DE.CN = new CompassNavigator(SensorPort.S4, 56f, 110f, Motor.B, Motor.A, false);
		DE.CN.setMoveSpeed(100);
		DE.CN.setTurnSpeed(400);
		
		try {
			Button.ENTER.waitForPressAndRelease();
		} catch (InterruptedException e) {
		}
		
		
		while(!Button.ESCAPE.isPressed()){
			if(DE.CN.getAngle() > 105.0 || DE.CN.getAngle() < 95.0){
				DE.CN.rotateTo(100);
			}
			DE.CN.updatePosition();
		}
		
		try {
			Button.ESCAPE.waitForPressAndRelease();
		} catch (InterruptedException e) {
		}
	}
}

class WriteDebug extends Thread{
	private DataExchange DE;
	
	public WriteDebug(DataExchange DE){
		this.DE = DE;
	}

	@Override
	public void run(){
		CompassSensor C = new CompassSensor(SensorPort.S4);
		while(!Button.ESCAPE.isPressed()){
			LCD.clear();
			if (DE!=null && DE.CN !=null){
				//DE.CN.updatePosition();
				LCD.drawString("A: " + String.valueOf(DE.CN.getAngle()), 1, 1);
				LCD.drawString("X: " + String.valueOf(DE.CN.getX()), 1, 2);
				LCD.drawString("Y: " + String.valueOf(DE.CN.getY()), 1, 3);
				LCD.drawString("C: " + String.valueOf(C.getDegrees()), 1, 4);
			}
			LCD.refresh();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {;
			}
		}
	}
}
