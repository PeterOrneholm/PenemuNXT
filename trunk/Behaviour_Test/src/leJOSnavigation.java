import lejos.robotics.navigation.*;
import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.robotics.subsumption.*;


public class leJOSnavigation {



	public static void main ( String args[]){
		CompassPilot compil = new CompassPilot
		(new CompassSensor(SensorPort.S2), 49, 125, Motor.C, Motor.B);
		TachoPilot tacho = new TachoPilot(49, 125, Motor.C, Motor.B);
		SimpleNavigator simnav = new SimpleNavigator (tacho);
		
		OpticalDistanceSensor ODS = new OpticalDistanceSensor(SensorPort.S2);
		UltrasonicSensor USS = new UltrasonicSensor(SensorPort.S3);
		TouchSensor TS = new TouchSensor(SensorPort.S4);
		
		simnav.setTurnSpeed(50);
		simnav.setMoveSpeed(500);

		Behavior b1 = (Behavior) new Forward(simnav);
	    Behavior b2 = (Behavior) new USSclose(simnav, USS);
	    Behavior b3 = (Behavior) new Bumperclose (simnav, TS);
	    Behavior [] bArray = {b1, b2, b3};
	    Arbitrator arby = new Arbitrator(bArray);
	    arby.start();
	      
		
		
		while (!Button.ESCAPE.isPressed()){
			
		}

	}
	
	

	
}
	