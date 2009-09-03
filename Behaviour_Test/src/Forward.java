import lejos.navigation.CompassNavigator;
import lejos.subsumption.Behavior;


public class Forward implements Behavior {

	CompassNavigator comnav;


	public Forward(CompassNavigator comnav) {
		this.comnav = comnav;

	}

	@Override
	public void action() {
		comnav.forward();

	}

	@Override
	public void suppress() {
		comnav.stop();

	}

	@Override
	public boolean takeControl() {
		return true;
	}

}
