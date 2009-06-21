import lejos.pc.comm.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.JFrame;

public class USScannerServer extends Applet implements Runnable, ActionListener {
	final int ULTRASONIC_SENSOR_MAX_DISTANCE = 200;

	SensorValues USSV;
	SensorValues MAV;
	int USHeadAngle;
	int ServerStatus;
	int ClientStatus;
	ArrayList<Point> Points = new ArrayList<Point>();
	VolatileImage osi;
	boolean ClearPoints = false;

	Button btnExit;
	Button btnClear;

	final int ConnectionMode = NXTCommFactory.USB;

	public static void main(String[] args) {

		Applet NXTGraph = new USScannerServer();
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
		Graphics2D g2 = (Graphics2D) g;
		g2.drawOval(0, 0, this.getWidth(), this.getHeight() * 2);

		for (int i = 0; i < Points.size(); i++) {
			if (i != 0) {
				g.drawLine(Points.get(i - 1).x, Points.get(i - 1).y, Points
						.get(i).x, Points.get(i).y);
			}
			// g.fillOval(p.x, p.y, 5, 5);
		}

		Point HeadPos = getScreenPos(ULTRASONIC_SENSOR_MAX_DISTANCE,
				USHeadAngle);
		g2.drawLine((int) (this.getWidth() / 2), this.getHeight(), HeadPos.x,
				HeadPos.y);
	}

	public void update(Graphics g) {
		if (osi == null || osi.getWidth() != getWidth()) {
			osi = createVolatileImage(getWidth(), getHeight());
		}
		osi.getGraphics().clearRect(0, 0, getWidth(), getHeight());
		paint(osi.getGraphics());
		g.drawImage(osi, 0, 0, null);
	}

	public void init() {
		btnExit = new Button("Exit");
		btnExit.addActionListener(this);
		this.add(btnExit);

		btnClear = new Button("Clear");
		btnClear.addActionListener(this);
		this.add(btnClear);
	}

	@Override
	public void run() {
		NXTConnector conn = new NXTConnector();

		switch (ConnectionMode) {
		case NXTCommFactory.BLUETOOTH:
			if (!conn.connectTo("btspp://")) {
				System.err.println("No NXT found by Bluetooth");
				System.exit(1);
			}
			break;
		case NXTCommFactory.USB:
			if (!conn.connectTo("usb://")) {
				System.err.println("No NXT found by USB");
				System.exit(1);
			}
			break;
		}

		DataInputStream inDat = conn.getDataIn();
		DataOutputStream outDat = conn.getDataOut();

		USSV = new SensorValues();
		MAV = new SensorValues();

		ServerStatus = 0;

		while (ServerStatus != 1) {
			try {
				ClientStatus = inDat.readInt();
				MAV.CurrentValue = inDat.readInt();
				USSV.CurrentValue = inDat.readInt();
			} catch (IOException ioe) {
				System.err.println("IO Exception reading reply");
			}

			if (ClientStatus == 2) {
				ClearPoints = true;
			}

			USHeadAngle = MAV.CurrentValue;

			if (ClearPoints) {
				Points.clear();
				ClearPoints = false;
			}

			if (USSV.CurrentValue != 255) {
				Point ScreenPos = getScreenPos(USSV.CurrentValue,
						MAV.CurrentValue);
				Points.add(ScreenPos);
			}
			MAV.LastY = MAV.CurrentY;

			try {
				outDat.writeInt(ServerStatus);
				outDat.flush();
			} catch (IOException ioe) {
				System.err.println("IO Exception reading reply");
			}

			if (ClientStatus == 1) {
				ServerStatus = 1;
			}

			repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
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

	private Point getScreenPos(int Distance, int Angle) {
		Angle += 90;
		int x, y;
		int distx, disty;

		distx = (int) (((Math.min(ULTRASONIC_SENSOR_MAX_DISTANCE, Distance) / (double) ULTRASONIC_SENSOR_MAX_DISTANCE) * (getWidth() / 2)));
		disty = (int) ((Math.min(ULTRASONIC_SENSOR_MAX_DISTANCE, Distance) / (double) ULTRASONIC_SENSOR_MAX_DISTANCE) * getHeight());

		x = (int) ((distx * Math.cos((Angle) * Math.PI / 180)) + (getWidth() / 2));
		x = getWidth() + (x * -1);
		y = (int) (-1 * (disty * Math.sin(Angle * Math.PI / 180)) + getHeight());

		return new Point(x, y);
	}

	public void actionPerformed(ActionEvent AE) {
		if (AE.getSource() == btnExit) {
			ServerStatus = 1;
		} else if (AE.getSource() == btnClear) {
			ClearPoints = true;
		}
	}
}

class SensorValues {
	public int LastX;
	public int LastY;
	public int CurrentY;
	public int CurrentValue;
}