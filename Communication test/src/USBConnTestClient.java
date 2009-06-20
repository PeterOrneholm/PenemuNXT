import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;


public class USBConnTestClient {
	public static void main(String [] args) throws Exception 
	{
		LCD.drawString("waiting", 0, 0);
		USBConnection conn = USB.waitForConnection();
		DataOutputStream dOut = conn.openDataOutputStream();
		DataInputStream dIn = conn.openDataInputStream();
		
		while (true) 
		{
            int b;
            try
            {
                b = dIn.readInt();
            }
            catch (EOFException e) 
            {
                break;
            }         
			dOut.writeInt(-b);
			dOut.flush();
	        LCD.drawInt((int)b,8,0,1);
		}
	}
}
