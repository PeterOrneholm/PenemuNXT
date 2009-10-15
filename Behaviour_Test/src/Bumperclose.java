import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;


public class Bumperclose implements Behavior {
	
	SimpleNavigator simnav;
	TouchSensor TS;

	public Bumperclose(SimpleNavigator simnav, TouchSensor TS) {
		this.simnav = simnav;
		this.TS = TS;
	}
	@Override
	public void action() {
		simnav.backward();
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {}
		simnav.stop();
		simnav.rotate(20);

	}

	@Override
	public void suppress() {
		simnav.stop();
	}

	@Override
	public boolean takeControl() {
		return TS.isPressed();
	}

}
