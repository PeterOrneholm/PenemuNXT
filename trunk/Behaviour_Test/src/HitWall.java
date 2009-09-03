import lejos.navigation.CompassNavigator;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.subsumption.Behavior;


public class HitWall implements Behavior {
	UltrasonicSensor USS = new UltrasonicSensor(SensorPort.S1);
	CompassNavigator comnav;
	OpticalDistanceSensor ODS;

	public HitWall(CompassNavigator comnav, OpticalDistanceSensor ODS) {
		this.comnav = comnav;
		this.ODS = ODS;
	}

	@Override
	public void action() {
		comnav.backward();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		comnav.stop();
		comnav.rotate(90);

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean takeControl() {
		return (USS.getDistance()< 25);
	}

}
