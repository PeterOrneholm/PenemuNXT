import lejos.navigation.*;
import lejos.nxt.*;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.subsumption.*;

public class leJOSnavigation {


	// Motor C needs 3108 to complete a 360 of the sensor,
	//or 8,6 degrees on the motor per 1 degree on the sensor.
	public static void main ( String args[]){
		CompassNavigator comnav = new CompassNavigator
		(SensorPort.S4, 56, 115, Motor.A, Motor.B);
		
		OpticalDistanceSensor ODS = new OpticalDistanceSensor(SensorPort.S2);
		

		Behavior b1 = new Forward(comnav);
	    Behavior b2 = new FollowWall(comnav, ODS);
	    Behavior b3 = new HitWall(comnav, ODS);
	    Behavior [] bArray = {b1, b2, b3};
	    Arbitrator arby = new Arbitrator(bArray);
	    arby.start();
	      
		
		
		while (!Button.ESCAPE.isPressed()){
			
		}

	}
	
	

	
}
	