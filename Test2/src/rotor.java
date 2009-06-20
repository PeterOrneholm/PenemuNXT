import lejos.nxt.Button;
import lejos.nxt.Motor;


public class rotor implements Runnable{
	public static void main (String[] args){
		rotor m = new rotor();
		m.run();
	}

	@Override
	public void run() {
		Motor.A.setSpeed(40);
		while (!Button.ESCAPE.isPressed()){
			Motor.A.rotateTo(-90);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e){}
			Motor.A.rotateTo(90);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
		
			}
			
		}
		
	}

}
