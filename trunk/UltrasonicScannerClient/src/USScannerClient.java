import java.io.*;

import javax.microedition.io.Connection;

import lejos.nxt.*;
import lejos.nxt.comm.*;

public class USScannerClient{
	public static void main(String[] args) throws Exception {
		LCD.drawString("Waiting...", 0, 0);
		LCD.refresh();
		USBConnection conn = USB.waitForConnection();

		DataOutputStream outDat = conn.openDataOutputStream();
		DataInputStream inDat = conn.openDataInputStream();

		UltrasonicSensor USS = new UltrasonicSensor(SensorPort.S1);

		int ClientStatus = 0;
		int ServerStatus = 0;
		
		LCD.clear();
		LCD.drawString("Connected!", 0, 0);
		LCD.refresh();
		
		HeadRotator HR = new HeadRotator();
		HR.start();
		while (true) {
			if ( Button.ESCAPE.isPressed()){
				ClientStatus = 1;
			}
			if ( Button.ENTER.isPressed()){
				ClientStatus = 2;
			}
			try {
				outDat.writeInt(ClientStatus);
				outDat.writeInt(Motor.A.getTachoCount());
				outDat.writeInt(USS.getDistance());

				outDat.flush();
			} catch (IOException ioe) {
				ServerStatus = 1;
				System.err.println("IO Exception Closing connection");
			}

			try {
				ServerStatus = inDat.readInt();
			} catch (IOException ioe) {
				ServerStatus = 1;
				System.err.println("IO Exception Closing connection");
			}
			
			if(ClientStatus==1 || ServerStatus == 1){
				break;
			}
			ClientStatus = 0;
		}

		try {
			inDat.close();
			outDat.close();
			System.out.println("Closed data streams");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}
		HR.interrupt();
		System.out.println("interrupted");
		Motor.A.stop();
		conn.close();
		Motor.A.rotateTo(0);
		System.exit(0);

	}
}

class HeadRotator extends Thread {
	@Override
	public void run() {
		Motor.A.setSpeed(40);
		while (!isInterrupted()) {
			Motor.A.rotateTo(-100, true);
			while(Motor.A.getTachoCount() >=-90 && !this.isInterrupted()){
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			Motor.A.rotateTo(100, true);
			while(Motor.A.getTachoCount() <= 90 && !this.isInterrupted()){
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {

			}
		}
	}
}
