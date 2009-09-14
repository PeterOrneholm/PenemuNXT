package org.penemunxt.projects.communicationtest.pc;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
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
		//mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//mainFrame.setUndecorated(true);
		//mainFrame.pack();
		//mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
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
		Rectangle SoundBox = new Rectangle(5, 5,
				(getWidth() / 2) - 10, getHeight() - 10);
		Rectangle IRDistanceBox = new Rectangle(
				(getWidth() / 2) + 5, 5, (getWidth() / 2) - 10, getHeight() - 10);

		SoundGraph.drawGraph(DS.Sound,0, 100, "%", Color.GREEN, SoundBox, true, g);
		IRDistanceGraph.drawGraph(DS.IRDistance,200, 1500, "MM", Color.RED, IRDistanceBox, true, g);
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
		NXTC = new NXTCommunication(false, DataFactories,
				new PCDataStreamConnection());
		NXTC.ConnectAndStartAll(NXTConnectionModes.USB, "PeterF",
				"0016530A3D1C");

		// Setup a data processor
		SensorDataProcessor SDP = new SensorDataProcessor(DS, NXTC,
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
