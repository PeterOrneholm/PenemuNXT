package org.penemunxt.debug.nxt;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class NXTDebug {
	public static void WriteMessageAndWait(String Message) {
		LCD.clear();
		LCD.drawString(Message, 1, 1);
		LCD.refresh();
		Button.ENTER.waitForPressAndRelease();
	}
}
