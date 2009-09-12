package org.penemunxt.pcserver.projects.communicationtest;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.VolatileImage;
import javax.sound.*;
import javax.sound.midi.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Port;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.penemunxt.pcserver.communication.NXTCommunication;
import org.penemunxt.pcserver.communication.NXTCommunicationData;
import org.penemunxt.pcserver.communication.NXTConnectionModes;
import org.penemunxt.pcserver.communication.NXTDataProcessor;

public class CommunicationTest extends Applet implements Runnable,
		ActionListener, WindowListener {
	boolean Active;
	DataShare DS;
	VolatileImage OSI;
	Button btnExit;
	NXTCommunication<SensorData, ServerMessageData> NXTComm;

	private void sendNormalData(
			NXTCommunication<SensorData, ServerMessageData> NXTComm, int Message) {
		NXTComm.getDataSendQueue().add(new ServerMessageData(Message));
	}

	private void ShutDown(
			NXTCommunication<SensorData, ServerMessageData> NXTComm) {
		NXTComm.getDataSendQueue().add(
				new ServerMessageData(
						NXTCommunicationData.MAIN_STATUS_SHUT_DOWN,
						NXTCommunicationData.DATA_STATUS_ONLY_STATUS, true));
	}

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
		g.drawString(DS.SoundDB + "%", (int) (this.getWidth() / 2.0),
				(int) (this.getHeight() / 2.0));
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

	public void actionPerformed(ActionEvent AE) {
		if (AE.getSource() == btnExit) {
			this.ShutDown(NXTComm);
		}
	}

	@Override
	public void run() {
		Active = true;
		DS = new DataShare();

		// Start up the communication
		NXTComm = new NXTCommunication<SensorData, ServerMessageData>(false,
				new SensorDataFactory(), new ServerMessageDataFactory());
		NXTComm.ConnectAndStartAll(NXTConnectionModes.Bluetooth, "PeterF",
				"0016530A3D1C");

		// Setup a data processor
		NXTDataProcessor<SensorData, ServerMessageData> DP = new NXTDataProcessor<SensorData, ServerMessageData>(
				NXTComm,
				new SensorDataProcessor<SensorData, ServerMessageData>(DS));

		// Add some data to send
		this.sendNormalData(NXTComm, ServerMessageData.SOUND_MEDIUM);

		// Handle retrieved data
		DP.start();

		while (Active) {
			this.Active = DP.Active;

			repaint();
			this.setSystemVolume(DS.SoundDB);

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		NXTComm.Disconnect();
		System.exit(0);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		this.ShutDown(NXTComm);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void setSystemVolume(int volumePercentage) {
		Port lineO;
		try {
			lineO = (Port) AudioSystem.getLine(Port.Info.SPEAKER);
			lineO.open();

			FloatControl controlF = (FloatControl) lineO
					.getControl(FloatControl.Type.VOLUME);
			float volume = 100 * (controlF.getValue() / controlF
					.getMaximum());
			controlF.setValue((float) volumePercentage / 100);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
