import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;




public class USSclose implements Behavior {
	SimpleNavigator simnav;
	UltrasonicSensor USS;

	public USSclose(SimpleNavigator simnav, UltrasonicSensor USS) {
		this.simnav = simnav;
		this.USS = USS;
	}


	@Override
	public void action() {
			simnav.rotate(20);

		

	}

	@Override
	public void suppress() {
		simnav.stop();


	}

	@Override
	public boolean takeControl() {
		return USS.getDistance()< 20;
	}

}
