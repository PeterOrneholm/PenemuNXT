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
	Button btnExit;
	Checkbox chkShowLatestPos;
	Checkbox chkShowDrivingPath;
	Checkbox chkShowBumpingPositions;
	Checkbox chkShowHeadMap;
	JLabel lblLatestPosition;

	boolean Active;
	NXTCommunication NXTC;
	DataShare DS;
	VolatileImage OSI;

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
		Panel controlPanel = new Panel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

		btnExit = new Button("Exit");
		btnExit.addActionListener(this);

		chkShowLatestPos = new Checkbox("Latest position", true);
		chkShowDrivingPath = new Checkbox("Driving path", true);
		chkShowBumpingPositions = new Checkbox("Bumps", true);
		chkShowHeadMap = new Checkbox("Map from head", true);

		lblLatestPosition = new JLabel("");

		Label lblHeader = new Label("PenemuNXT");
		Label lblLatestPositionHeader = new Label("Latest position:");
		
		controlPanel.add(lblHeader);
		controlPanel.add(btnExit);
		controlPanel.add(chkShowLatestPos);
		controlPanel.add(chkShowDrivingPath);
		controlPanel.add(chkShowBumpingPositions);
		controlPanel.add(chkShowHeadMap);
		controlPanel.add(lblLatestPositionHeader);
		controlPanel.add(lblLatestPosition);
		

		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(controlPanel, BorderLayout.WEST);
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
			boolean show;

			switch (PD.getType()) {
			case RobotData.POSITION_TYPE_DRIVE:
				g.setColor(Color.BLACK);
				show = chkShowDrivingPath.getState();
				circleSize = 2;
				break;
			case RobotData.POSITION_TYPE_BUMP_BUMPER:
				g.setColor(Color.RED);
				show = chkShowBumpingPositions.getState();
				circleSize = 10;
				break;
			case RobotData.POSITION_TYPE_BUMP_DISTANCE:
				g.setColor(Color.BLUE);
				show = chkShowBumpingPositions.getState();
				circleSize = 10;
				break;
			default:
				g.setColor(Color.BLACK);
				show = true;
				circleSize = 2;
				break;
			}

			if (show) {
				g.fillOval(-PD.getPosY() / 5 + (this.getWidth() / 2), -PD
						.getPosX()
						/ 5 + (this.getHeight() / 2), circleSize, circleSize);
			}

			if (chkShowHeadMap.getState()) {
				int x;
				int y;

				x = (int) (PD.getHeadDistance() * Math
						.cos((PD.getHeadHeading() + PD.getRobotHeading())
								* Math.PI / 180))
						+ PD.getPosX();
				y = (int) (PD.getHeadDistance() * Math
						.sin((PD.getHeadHeading() + PD.getRobotHeading())
								* Math.PI / 180))
						+ PD.getPosY();

				g.setColor(Color.ORANGE);
				if (PD.getHeadDistance() > 200 && PD.getHeadDistance() < 1500) {
					g.fillOval(-y / 5 + (this.getWidth() / 2), -x / 5
							+ (this.getHeight() / 2), 5, 5);
				}
			}

		}

		if (DS.NXTRobotData.size() > 0) {
			RobotData PD = DS.NXTRobotData.get(DS.NXTRobotData.size() - 1);
			lblLatestPosition.setText("X: " + PD.getPosX() + "\nY: "
					+ PD.getPosY() + "\nH: " + PD.getRobotHeading());
			if (chkShowLatestPos.getState()) {
				g.setColor(Color.PINK);
				g.fillOval(-PD.getPosY() / 5 + (this.getWidth() / 2), -PD
						.getPosX()
						/ 5 + (this.getHeight() / 2), 15, 15);
			}

		}
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