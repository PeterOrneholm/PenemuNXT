package org.penemunxt.pcserver.projects.communicationtest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;

import org.penemunxt.pcserver.communication.*;

public class CommunicationTest extends Applet implements Runnable,
		ActionListener, WindowListener {
	boolean Active;
	DataShare DS;
	VolatileImage OSI;
	Button btnExit;
	NXTCommunication NXTComm;

	public static void main(String[] args) {
		CommunicationTest PCCT = new CommunicationTest();
		PCCT.init();

		JFrame mainFrame = new JFrame();
		mainFrame.addWindowListener(PCCT);

		mainFrame.add(PCCT);

		mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		// mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// mainFrame.setUndecorated(true);
		// mainFrame.pack();
		// mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		mainFrame.setVisible(true);

		PCCT.start();
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	public void init() {
		btnExit = new Button("Exit");
		btnExit.addActionListener(this);
		this.add(btnExit);
	}

	public void update(Graphics g) {
		if (OSI == null || OSI.getWidth() != getWidth()
				|| OSI.getHeight() != getHeight()) {
			OSI = createVolatileImage(getWidth(), getHeight());
		}

		OSI.getGraphics().clearRect(0, 0, OSI.getWidth(), OSI.getHeight());
		paint(OSI.getGraphics());
		g.drawImage(OSI, 0, 0, null);
	}

	public void paint(Graphics g) {
		int C = (int) ((DS.SoundDB / 100.0) * 255);
		g.setColor(new Color(C, C, 255 - C));

		g
				.fillRect(0, 0, (int) ((DS.SoundDB / 100.0) * getWidth()),
						getHeight());

		g.setFont(new Font("Arial", Font.PLAIN, 36));
		g.setColor(new Color(0, 0, 0));
		g.drawString(DS.SoundDB + "%",
				(int) ((DS.SoundDB / 100.0) * getWidth()), (int) (this
						.getHeight() / 2.0));
	}

	@Override
	public void run() {
		Active = true;

		// Object to share data internal
		DS = new DataShare();

		// Setup data factories
		NXTCommunicationDataFactories DataFactories = new NXTCommunicationDataFactories(
				new SensorDataFactory(), new ServerMessageDataFactory());

		// Setup and start the communication
		NXTComm = new NXTCommunication(false, DataFactories);
		NXTComm.ConnectAndStartAll(NXTConnectionModes.USB, "PeterF",
				"0016530A3D1C");

		// Setup a data processor
		SensorDataProcessor SDP = new SensorDataProcessor(DS, NXTComm,
				DataFactories);
		SDP.start();

		while (Active) {
			this.Active = SDP.Active;

			repaint();

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		NXTComm.Disconnect();
		System.exit(0);
	}

	public void actionPerformed(ActionEvent AE) {
		if (AE.getSource() == btnExit) {
			NXTComm.sendShutDown();
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		NXTComm.sendShutDown();
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
