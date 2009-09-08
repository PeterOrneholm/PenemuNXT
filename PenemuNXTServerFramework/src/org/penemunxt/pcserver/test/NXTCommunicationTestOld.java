package org.penemunxt.pcserver.test;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;

public class NXTCommunicationTestOld {
	final static int ConnectionMode = NXTCommFactory.USB;
	static boolean Active = true;

	public static void main(String args[]) {
		NXTConnector conn = new NXTConnector();

		boolean connected = conn.connectTo("PeterF", "0016530A3D1C", NXTCommFactory.USB);
		
		if(connected){
			System.out.println("Connected to:" + conn.getNXTInfo().name);
		}else{
			System.err.println("Not connected");
			System.exit(1);
		}

		DataInputStream inDat = conn.getDataIn();
		DataOutputStream outDat = conn.getDataOut();

		int V1 = 0;
		int V2 = 0;
		int V3 = 0;
		int V4 = 0;
		boolean P = false;

		while (Active) {
			try {
				P = inDat.readBoolean();
				V1 = inDat.readInt();
				V2 = inDat.readInt();
				if (V2 != 0) {
					V3 = inDat.readInt();
					V4 = inDat.readInt();
				}
			} catch (IOException ioe) {
				System.err.println("IO Exception reading reply");
			}
			
			if(V1==200){
				V1 = 210;
				P = true;
				Active = false;
			}else{
				P = false;
			}

			if (V2 != 0) {
				System.out.println(V1 + "," + V2 + "," + V3 + "," + V4 + ";"
						+ (V3 + V4));
			} else {
				System.out.println(V1 + "," + V2);
			}

			try {
				outDat.writeBoolean(P);
				outDat.writeInt(V1);
				outDat.writeInt(V2);
				if (V2 != 0) {
					outDat.writeInt(V4 + V3);
				}
				outDat.flush();
			} catch (IOException ioe) {
				System.err.println("IO Exception writing reply");
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
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
