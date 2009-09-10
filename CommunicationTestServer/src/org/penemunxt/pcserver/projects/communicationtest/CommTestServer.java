package org.penemunxt.pcserver.projects.communicationtest;
import lejos.pc.comm.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.JFrame;

public class CommTestServer extends Applet implements Runnable,
		ActionListener {
	int x, i;
	SensorValues SSV;
	SensorValues USSV;
	int status;

	public static void main(String[] args) {
		Applet NXTGraph = new CommTestServer();
		NXTGraph.init();

		JFrame mainFrame = new JFrame();
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		mainFrame.add(NXTGraph);
		mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setUndecorated(true);
		mainFrame.pack();
		mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		mainFrame.setVisible(true);

		NXTGraph.start();
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	public void paint(Graphics g) {
	}

	public void init() {
		Button btnExit = new Button("Exit");
		btnExit.addActionListener(this);
		this.add(btnExit);
	}

	@Override
	public void run() {
		NXTConnector conn = new NXTConnector();
		if (!conn.connectTo("usb://")) {
			System.err.println("No NXT find using USB");
			System.exit(1);
		}

		DataInputStream inDat = conn.getDataIn();
		DataOutputStream outDat = conn.getDataOut();

		SSV = new SensorValues();
		USSV = new SensorValues();

		status = 0;

		while (status != 1) {
			try {
				SSV.CurrentValue = inDat.readInt();
				USSV.CurrentValue = inDat.readInt();
			} catch (IOException ioe) {
				System.err.println("IO Exception reading reply");
			}

			i++;
			
			x++;
			x++;
			x++;

			SSV.CurrentY = (int) (this.getHeight() - ((SSV.CurrentValue / 100.0) * this
					.getHeight()));
			USSV.CurrentY = (int) (this.getHeight() - ((Math.min(
					USSV.CurrentValue, 200) / 200.0) * this.getHeight()));

			Graphics2D g = (Graphics2D) this.getGraphics();

			if (SSV.CurrentY != SSV.LastY) {
				g.setColor(new Color(0, 255, 0));
				g.setStroke(new BasicStroke(3));

				if ((x % this.getWidth()) > (SSV.LastX % this.getWidth())) {
					g.drawLine((SSV.LastX % this.getWidth()), SSV.LastY,
							(x % this.getWidth()), SSV.CurrentY);
				}

				SSV.LastY = SSV.CurrentY;
				SSV.LastX = x;
			}

			if (USSV.CurrentValue == 255) {
				USSV.CurrentY = USSV.LastY;
			}

			if (USSV.CurrentY != USSV.LastY) {
				g.setColor(new Color(255, 0, 0));
				g.setStroke(new BasicStroke(3));

				if ((x % this.getWidth()) > (USSV.LastX % this.getWidth())) {
					g.drawLine((USSV.LastX % this.getWidth()), USSV.LastY,
							(x % this.getWidth()), USSV.CurrentY);
				}

				USSV.LastY = USSV.CurrentY;
				USSV.LastX = x;
			}

			try {
				outDat.writeInt(status);
				outDat.flush();
			} catch (IOException ioe) {
				System.err.println("IO Exception reading reply");
			}

			if (((x-1) % this.getWidth()) == 0 || (x % this.getWidth()) == 0 || ((x + 1) % this.getWidth()) == 0) {
				repaint();
			}

			if (SSV.CurrentValue == 1001 || USSV.CurrentValue == 1001) {
				status = 1;
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

		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		status = 1;

	}
}

class SensorValues {
	public int LastX;
	public int LastY;
	public int CurrentY;
	public int CurrentValue;
}