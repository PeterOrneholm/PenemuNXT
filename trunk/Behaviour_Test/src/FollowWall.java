import lejos.navigation.CompassNavigator;
import lejos.nxt.Motor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.subsumption.Behavior;


public class FollowWall implements Behavior {
	CompassNavigator comnav;
	OpticalDistanceSensor ODS;

	public FollowWall(CompassNavigator comnav, OpticalDistanceSensor ODS) {
		this.comnav = comnav;
		this.ODS = ODS;
	}


	@Override
	public void action() {
		comnav.rotate((int)(90-(Motor.C.getTachoCount()/8.6)), true);
		Motor.C.rotateTo(777);
		comnav.forward();
		if (ODS.getDistance() < 350){
			comnav.turn(200, 5);
		} else if ( ODS.getDistance() > 370){
			comnav.turn(-200, 5);
		}

		

	}

	@Override
	public void suppress() {
		comnav.stop();
		Motor.C.rotateTo(0);

	}

	@Override
	public boolean takeControl() {
		return ODS.getDistance()< 400;
	}

}
