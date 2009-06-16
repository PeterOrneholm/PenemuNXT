import lejos.nxt.*;
import lejos.util.DebugMessages;

public class SonicDataCollector {
	static UltrasonicSensor sonic;
	static DebugMessages dm;

	public static void main(String[] args) throws Exception {
		sonic = new UltrasonicSensor(SensorPort.S4);
		dm = new DebugMessages(3);
		dm.setLCDLines(6);
		Button.ENTER.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				int d = sonic.getDistance();
				if (d == 255) {
					dm.echo("N/A");
				} else {
					dm.echo(d + "cm");
				}
			}

			public void buttonReleased(Button b) {
			}
		});
		while (!Button.ESCAPE.isPressed()) {
			LCD.drawString("                 ", 0, 0);
			LCD.drawString("----------------", 0, 1);
			LCD.drawString("Cur. value: " + sonic.getDistance(), 0, 0);

			Thread.sleep(500);
		}
	}
}
