import lejos.nxt.Button;
import lejos.nxt.LCD;


public class Main {
	public static void main(String[] args) throws InterruptedException{
		LCD.drawString("Hej", 1, 1);
		Button.ESCAPE.waitForPressAndRelease();
	}
}
