import lejos.nxt.Button;
import lejos.nxt.LCD;


public class HelloWorld {
	public static void main(String args[]) throws InterruptedException{
		LCD.drawString("Hello World!", 1, 1);
		LCD.drawString("Second line", 1, 2);
		LCD.drawString("Third line", 1, 3);
		LCD.drawString("Furth Lin", 1, 4);
		
		Button.ESCAPE.waitForPressAndRelease();
		
	}
}
