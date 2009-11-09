import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;



public class Forward implements Behavior {

	SimpleNavigator comnav;


	public Forward(SimpleNavigator comnav) {
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
