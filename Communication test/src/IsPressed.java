import lejos.nxt.*;


public class IsPressed {
	public static void main (String[] args){
		Touchcheck TC = new Touchcheck();
		TC.run();
	}


}
class Touchcheck implements Runnable{
	
	public void run() {
		TouchSensor TS = new TouchSensor(SensorPort.S1);
		while (true){
			if(TS.isPressed()){
				LCD.drawString("OMGOMGOMG SOMEONE IS TOUCHING ME!!! OMGOMGOMG", 1, 3);
			} else {
				LCD.drawString("Come on, touch me!", 1, 3);
			}
		}
		
	}
	



	
}