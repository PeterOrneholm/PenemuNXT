import lejos.nxt.Button;
import lejos.nxt.LCD;


public class HelloWorld {
	public static void main(String args[]) throws InterruptedException{
		LCD.drawString("Hello World!", 1, 1);
		Button.ESCAPE.waitForPressAndRelease();
	}
}
