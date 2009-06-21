import lejos.nxt.*;


public class IsPressed {
	public static void main (String[] args){
		Touchcheck TC = new Touchcheck();
		TC.run();
	}


}
class Touchcheck implements Runnable{
	
	public void run() {
		SoundSensor SS = new SoundSensor(SensorPort.S2);
		TouchSensor TS = new TouchSensor(SensorPort.S1);
		int motorspeed = 0;
		while (true){
			if(TS.isPressed()){
				LCD.drawString("OMGOMGOMG SOMEONE IS TOUCHING ME!!! OMGOMGOMG", 1, 3);
			} else {
				LCD.drawString("Come on, touch me!", 1, 3);

			}
			
			
			LCD.drawInt(SS.readValue(), 1, 4);
			LCD.drawInt(motorspeed, 1, 5);
			
			motorspeed = motorspeed + ((SS.readValue()- motorspeed)/50);
			Motor.C.setSpeed(motorspeed);
			Motor.C.forward();
			
		}
		
	}
	



	
}