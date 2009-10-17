package org.penemunxt.projects.communicationtest.pc;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;

import org.penemunxt.communication.*;
import org.penemunxt.communication.pc.*;
import org.penemunxt.graphics.pc.Graph;
import org.penemunxt.projects.communicationtest.*;

public class CommunicationTest extends Applet implements Runnable,
		ActionListener, WindowListener {
	boolean Active;
	DataShare DS;
	VolatileImage OSI;
	Button btnExit;

	Checkbox chkShowLatestPos;
	Checkbox chkShowDrivingPath;
	Checkbox chkShowBumpingPositions;
	Checkbox chkShowHeadMap;

	NXTCommunication NXTC;
	Graph SoundGraph;
	Graph IRDistanceGraph;

	public static void main(String[] args) {
		CommunicationTest PCCT = new CommunicationTest();
		PCCT.init();

		JFrame mainFrame = new JFrame();
		mainFrame.addWindowListener(PCCT);
		mainFrame.add(PCCT);

		mainFrame.setBackground(Color.WHITE);
		mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		// mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// mainFrame.setUndecorated(true);
		// mainFrame.pack();
		// mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		mainFrame.setVisible(true);

		PCCT.start();
	}

	@Override
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void init() {
		btnExit = new Button("Exit");
		btnExit.addActionListener(this);
		this.add(btnExit);

		SoundGraph = new Graph();
		IRDistanceGraph = new Graph();
	}

	@Override
	public void update(Graphics g) {
		if (OSI == null || OSI.getWidth() != getWidth()
				|| OSI.getHeight() != getHeight()) {
			OSI = createVolatileImage(getWidth(), getHeight());
		}

		OSI.getGraphics().clearRect(0, 0, OSI.getWidth(), OSI.getHeight());
		paint(OSI.getGraphics());
		g.drawImage(OSI, 0, 0, null);
	}

	@Override
	public void paint(Graphics g) {
		for (RobotData PD : DS.NXTRobotData) {
			int circleSize;
			switch (PD.getType()) {
			case RobotData.POSITION_TYPE_DRIVE:
				g.setColor(Color.BLACK);
				circleSize = 2;
				break;
			case RobotData.POSITION_TYPE_BUMP_BUMPER:
				g.setColor(Color.RED);
				circleSize = 10;
				break;
			case RobotData.POSITION_TYPE_BUMP_DISTANCE:
				g.setColor(Color.BLUE);
				circleSize = 10;
				break;
			default:
				g.setColor(Color.BLACK);
				circleSize = 2;
				break;
			}
			g.fillOval(-PD.getPosY() / 5 + (this.getWidth() / 2), -PD
					.getPosX()/ 5 + (this.getHeight() / 2), circleSize, circleSize);
			
			int x;
			int y;
			
			x = (int) (PD.getHeadDistance() * Math.cos((PD.getHeadHeading()+ PD.getRobotHeading()) * Math.PI / 180)) + PD.getPosX(); 
			y = (int) (PD.getHeadDistance() * Math.sin((PD.getHeadHeading()+ PD.getRobotHeading()) * Math.PI / 180)) + PD.getPosY(); 
			
			g.setColor(Color.ORANGE);
			if (PD.getHeadDistance()> 200 && PD.getHeadDistance()< 1500) {
				g.fillOval(-y / 5 + (this.getWidth() / 2), -x / 5
						+ (this.getHeight() / 2), 5, 5);
			}
			
		}

		RobotData PD = DS.NXTRobotData.get(DS.NXTRobotData.size() - 1);
		g.drawString("X: " + PD.getPosX() + "Y: " + PD.getPosY() + "H: "
				+ PD.getRobotHeading(), 100, 100);
	}

	@Override
	public void run() {
		Active = true;

		// Object to share data internal
		DS = new DataShare();

		// Setup data factories
		NXTCommunicationDataFactories DataFactories = new NXTCommunicationDataFactories(
				new RobotDataFactory(), new ServerMessageDataFactory());

		// Setup and start the communication
		NXTC = new NXTCommunication(false, DataFactories,
				new PCDataStreamConnection());
		NXTC.ConnectAndStartAll(NXTConnectionModes.Bluetooth, "NXT", "");

		// Setup a data processor
		PositionDataProcessor SDP = new PositionDataProcessor(DS, NXTC,
				DataFactories);
		SDP.start();

		while (Active) {
			this.Active = SDP.Active;

			repaint();

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}

		NXTC.Disconnect();
		System.exit(0);
	}

	public void actionPerformed(ActionEvent AE) {
		if (AE.getSource() == btnExit) {
			NXTC.sendShutDown();
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		NXTC.sendShutDown();
	}

	public void windowActivated(WindowEvent arg0) {
	}

	public void windowClosed(WindowEvent arg0) {
	}

	public void windowDeactivated(WindowEvent arg0) {
	}

	public void windowDeiconified(WindowEvent arg0) {
	}

	public void windowIconified(WindowEvent arg0) {
	}

	public void windowOpened(WindowEvent arg0) {
	}
}
