import lejos.nxt.*;

public class TouchAndSoundTest {
	public static void main (String[] args){
		TouchSoundCheck TC = new TouchSoundCheck();
		TC.run();
	}
}
class TouchSoundCheck implements Runnable{
	
	public void run() {
		SoundSensor SS = new SoundSensor(SensorPort.S2);
		TouchSensor TS = new TouchSensor(SensorPort.S1);
		int motorspeed = 0;
		while (true){
			if(TS.isPressed()){
				LCD.drawString("I'm so touched!", 1, 3);
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