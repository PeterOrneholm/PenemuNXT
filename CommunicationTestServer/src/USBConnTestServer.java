import lejos.pc.comm.*;

import java.applet.Applet;
import java.awt.*;
import java.io.*;

public class USBConnTestServer extends Applet implements Runnable {
	int x, y, lasty, lastx, i;
	public void start(){
		Thread t = new Thread(this);
		t.start();
	}
	
	public void paint(Graphics g){
		//g.fillOval(x, y, 10, 10);
		System.out.println("bu");
		}

	@Override
	public void run() {
		NXTConnector conn = new NXTConnector();

		boolean res = conn.connectTo("usb://");
		System.out.println(res);
		if (!res){
			System.err.println("No NXT find using USB");
			System.exit(1);
		}

		DataInputStream inDat = conn.getDataIn();
		DataOutputStream outDat = conn.getDataOut();
		
		int LastValue = 0;
		int CurrentValue = 0;
		
		while(CurrentValue!=101)
		{
			try {
				CurrentValue = inDat.readInt();
	        } catch (IOException ioe) {
	           System.err.println("IO Exception reading reply");
	        }
	        
	        x++;
	        i++;
	        
	        y= (int) (this.getHeight() - ((CurrentValue/100.0) * this.getHeight()));
	        lasty = (int) (this.getHeight() - ((LastValue/100.0) * this.getHeight()));
	        
	        
	        if(CurrentValue != LastValue){
	        	System.out.println("Value: " + String.valueOf(CurrentValue));
		        Graphics2D g = (Graphics2D) this.getGraphics();
		        g.setStroke(new BasicStroke(3));
		        if(x%this.getWidth()>(lastx)%this.getWidth()){
		        	g.drawLine((lastx)%this.getWidth(), lasty, x%this.getWidth(), y);	
		        }
		        
		        LastValue = CurrentValue;
		        lastx=x;

		   
	        }
	        
	        
	        if(i%this.getWidth()==0)repaint();

	

		}
		
		try {
			inDat.close();
			outDat.close();
			System.out.println("Closed data streams");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}
		
		try {
			conn.close();
			System.out.println("Closed connection");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}

	}
}