package org.penemunxt.projects.communicationtest.pc;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.beans.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.penemunxt.communication.*;
import org.penemunxt.communication.pc.*;
import org.penemunxt.projects.communicationtest.*;

public class CommunicationTest extends Applet implements Runnable,
		ActionListener, WindowListener, ChangeListener, MouseWheelListener,
		MouseListener, MouseMotionListener {
	// Constants

	final static int OPTICAL_DISTANCE_MIN_LENGTH_MM = 200;
	final static int OPTICAL_DISTANCE_MAX_LENGTH_MM = 1500;

	final static Color DEFAULT_CIRCLE_COLOR = Color.BLACK;
	final static Color LATEST_POS_CIRCLE_COLOR = Color.GREEN;
	final static Color DRIVING_PATH_CIRCLE_COLOR = Color.BLACK;
	final static Color BUMPING_BUMPER_CIRCLE_COLOR = Color.RED;
	final static Color BUMPING_DISTANCE_CIRCLE_COLOR = Color.BLUE;
	final static Color HEAD_MAP_CIRCLE_COLOR = Color.ORANGE;

	final static int DEFAULT_CIRCLE_SIZE = 2;
	final static int LATEST_POS_CIRCLE_SIZE = 15;
	final static int DRIVING_PATH_CIRCLE_SIZE = 2;
	final static int BUMPING_BUMPER_CIRCLE_SIZE = 10;
	final static int BUMPING_DISTANCE_CIRCLE_SIZE = 10;
	final static int HEAD_MAP_CIRCLE_SIZE = 5;

	final static float MAP_DEFAULT_SCALE_FACTOR = 0.004f;
	final static int MAP_MIN_SCALE = 1;
	final static int MAP_MAX_SCALE = 100;
	final static int MAP_INIT_SCALE = 50;

	final static NXTConnectionModes[] CONNECTION_MODES = {
			NXTConnectionModes.USB, NXTConnectionModes.Bluetooth };
	final static String[] CONNECTION_MODES_NAMES = { "USB", "Bluetooth" };
	final static int CONNECTION_MODES_INIT_SELECTED = 1;

	final static String CONNECT_TO_NAME_DEFAULT = "NXT";
	final static String CONNECT_TO_ADDRESS_DEFAULT = "0016530A9000";

	final static Boolean START_FULLSCREEN = true;

	// Buttons
	Button btnExit;
	Button btnStart;
	Button btnConnect;
	Button btnSaveData;
	Button btnOpenData;

	// Panels
	Panel controlPanel;
	Panel mapPanel;

	// Checkboxes
	Checkbox chkShowLatestPos;
	Checkbox chkShowDrivingPath;
	Checkbox chkShowBumpingPositions;
	Checkbox chkShowHeadMap;

	// Labels
	Label lblRDX;
	Label lblRDY;
	Label lblRDRobotHeading;
	Label lblRDHeadDistance;
	Label lblRDHeadHeading;

	// Comboboxes
	JComboBox cboConnectionTypes;

	// Sliders
	JSlider sldMapScale;

	// Textboxes
	TextField txtConnectToName;
	TextField txtConnectToAddress;

	// Map
	int curScale = MAP_INIT_SCALE;
	Point mapCenter;
	Point startDrag;
	Point mapCenterBeforeDrag;

	// Misc
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
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (START_FULLSCREEN) {
			mainFrame.setUndecorated(true);
			mainFrame.pack();
			mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		}
		mainFrame.setVisible(true);

		PCCT.start();
	}

	@Override
	public void start() {

	}

	@Override
	public void init() {
		// Panels
		Panel leftPanel = new Panel();
		controlPanel = new Panel();
		mapPanel = new Panel();

		// Fonts
		Font fntMainHeader = new Font("Arial", Font.BOLD, 22);
		Font fntSectionHeader = new Font("Arial", Font.BOLD, 14);
		Font fntLabelHeader = new Font("Arial", Font.BOLD, 12);

		// Header
		Label lblHeader = new Label("PenemuNXT", Label.CENTER);
		lblHeader.setFont(fntMainHeader);

		// Exit
		btnExit = new Button("Shut down");
		btnExit.addActionListener(this);
		btnExit.setEnabled(false);

		// Start
		btnStart = new Button("Start");
		btnStart.addActionListener(this);

		// Connect
		Label lblConnectionHeader = new Label("Connection");
		lblConnectionHeader.setFont(fntSectionHeader);

		cboConnectionTypes = new JComboBox(CONNECTION_MODES_NAMES);
		cboConnectionTypes.setSelectedIndex(CONNECTION_MODES_INIT_SELECTED);

		txtConnectToName = new TextField(CONNECT_TO_NAME_DEFAULT, 15);
		txtConnectToAddress = new TextField(CONNECT_TO_ADDRESS_DEFAULT, 15);
		btnConnect = new Button("Connect");
		btnConnect.addActionListener(this);
		btnConnect.setEnabled(true);

		Panel pnlConnection = new Panel(new GridBagLayout());

		GridBagConstraints CBC = new GridBagConstraints();
		CBC.fill = GridBagConstraints.HORIZONTAL;
		CBC.weightx = 0.0;
		CBC.weighty = 0.0;

		CBC.gridx = 0;
		CBC.gridy = 0;
		pnlConnection.add(new Label("Mode:"), CBC);
		CBC.gridx = 1;
		CBC.gridy = 0;
		pnlConnection.add(cboConnectionTypes, CBC);
		CBC.gridx = 0;
		CBC.gridy = 1;
		pnlConnection.add(new Label("NXT name:"), CBC);
		CBC.gridx = 1;
		CBC.gridy = 1;
		pnlConnection.add(txtConnectToName, CBC);
		CBC.gridx = 0;
		CBC.gridy = 2;
		pnlConnection.add(new Label("NXT address:"), CBC);
		CBC.gridx = 1;
		CBC.gridy = 2;
		pnlConnection.add(txtConnectToAddress, CBC);
		CBC.gridx = 0;
		CBC.gridy = 3;
		CBC.gridwidth = 2;
		pnlConnection.add(btnConnect, CBC);

		// Show
		Label lblShowHeader = new Label("Show");
		lblShowHeader.setFont(fntSectionHeader);

		chkShowLatestPos = new Checkbox("Latest position", true);
		chkShowDrivingPath = new Checkbox("Driving path", true);
		chkShowBumpingPositions = new Checkbox("Bumps", true);
		chkShowHeadMap = new Checkbox("Map from head", true);

		// Map scale
		Label lblMapScalesHeader = new Label("Map scale");
		lblMapScalesHeader.setFont(fntSectionHeader);

		sldMapScale = new JSlider(JSlider.HORIZONTAL, MAP_MIN_SCALE,
				MAP_MAX_SCALE, curScale);
		sldMapScale.setMajorTickSpacing(10);
		sldMapScale.setMinorTickSpacing(5);
		sldMapScale.setPaintTicks(true);
		sldMapScale.setPaintLabels(true);

		Hashtable labelTable = new Hashtable();
		labelTable.put(new Integer(MAP_MIN_SCALE), new JLabel(MAP_MIN_SCALE
				+ "%"));
		labelTable.put(new Integer(25), new JLabel("25%"));
		labelTable.put(new Integer(50), new JLabel("50%"));
		labelTable.put(new Integer(75), new JLabel("75%"));
		labelTable.put(new Integer(MAP_MAX_SCALE), new JLabel(MAP_MAX_SCALE
				+ "%"));
		sldMapScale.setLabelTable(labelTable);
		sldMapScale.setBackground(Color.LIGHT_GRAY);
		sldMapScale.addChangeListener(this);

		// Latest data
		Label lblLatestDataHeader = new Label("Latest data");
		lblLatestDataHeader.setFont(fntSectionHeader);
		Panel pnlLatestData = new Panel(new GridLayout(0, 2));

		lblRDX = new Label();
		lblRDY = new Label();
		lblRDRobotHeading = new Label();
		lblRDHeadDistance = new Label();
		lblRDHeadHeading = new Label();

		Label lblRDXHeader = new Label("X:");
		Label lblRDYHeader = new Label("Y:");
		Label lblRDRobotHeadingHeader = new Label("Robot heading:");
		Label lblRDHeadDistanceHeader = new Label("Head distance:");
		Label lblRDHeadHeadingHeader = new Label("Head heading:");

		lblRDXHeader.setFont(fntLabelHeader);
		lblRDYHeader.setFont(fntLabelHeader);
		lblRDRobotHeadingHeader.setFont(fntLabelHeader);
		lblRDHeadDistanceHeader.setFont(fntLabelHeader);
		lblRDHeadHeadingHeader.setFont(fntLabelHeader);

		pnlLatestData.add(lblRDXHeader);
		pnlLatestData.add(lblRDX);
		pnlLatestData.add(lblRDYHeader);
		pnlLatestData.add(lblRDY);
		pnlLatestData.add(lblRDRobotHeadingHeader);
		pnlLatestData.add(lblRDRobotHeading);
		pnlLatestData.add(lblRDHeadDistanceHeader);
		pnlLatestData.add(lblRDHeadDistance);
		pnlLatestData.add(lblRDHeadHeadingHeader);
		pnlLatestData.add(lblRDHeadHeading);

		// Map data
		Label lblSaveDataHeader = new Label("Map data", Label.LEFT);
		lblSaveDataHeader.setFont(fntSectionHeader);

		// Open
		btnOpenData = new Button("Open");
		btnOpenData.addActionListener(this);

		// Save
		btnSaveData = new Button("Save");
		btnSaveData.addActionListener(this);

		// Control panel
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.add(lblHeader);

		controlPanel.add(btnStart);
		controlPanel.add(btnExit);

		controlPanel.add(lblConnectionHeader);
		controlPanel.add(pnlConnection);

		controlPanel.add(lblShowHeader);
		controlPanel.add(chkShowLatestPos);
		controlPanel.add(chkShowDrivingPath);
		controlPanel.add(chkShowBumpingPositions);
		controlPanel.add(chkShowHeadMap);
		controlPanel.add(chkShowHeadMap);

		controlPanel.add(lblMapScalesHeader);
		controlPanel.add(sldMapScale);

		controlPanel.add(lblLatestDataHeader);
		controlPanel.add(pnlLatestData);

		controlPanel.add(lblSaveDataHeader);
		controlPanel.add(btnOpenData);
		controlPanel.add(btnSaveData);

		// Left panel
		leftPanel.setBackground(Color.LIGHT_GRAY);
		leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		leftPanel.add(controlPanel);

		// Add it to the applet
		this.setLayout(new BorderLayout());
		this.add(leftPanel, BorderLayout.WEST);
		this.add(mapPanel, BorderLayout.CENTER);

		// Map cursors
		mapPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));

		// Map Listeners
		mapPanel.addMouseMotionListener(this);
		mapPanel.addMouseListener(this);
		mapPanel.addMouseWheelListener(this);
	}

	@Override
	public void update(Graphics g) {
		if (OSI == null || OSI.getWidth() != getWidth()
				|| OSI.getHeight() != getHeight()) {
			OSI = createVolatileImage(mapPanel.getWidth(), mapPanel.getHeight());
		}

		OSI.getGraphics().clearRect(0, 0, OSI.getWidth(), OSI.getHeight());
		paint(OSI.getGraphics());
		mapPanel.getGraphics().drawImage(OSI, 0, 0, null);
	}

	private void paintArrow(Graphics g, int x0, int y0, int x1, int y1) {
		int deltaX = x1 - x0;
		int deltaY = y1 - y0;
		double frac = 0.2;

		g.drawLine(x0, y0, x1, y1);
		g.drawLine(x0 + (int) ((1 - frac) * deltaX + frac * deltaY), y0
				+ (int) ((1 - frac) * deltaY - frac * deltaX), x1, y1);
		g.drawLine(x0 + (int) ((1 - frac) * deltaX - frac * deltaY), y0
				+ (int) ((1 - frac) * deltaY + frac * deltaX), x1, y1);

	}

	@Override
	public void paint(Graphics g) {
		if (DS != null && DS.NXTRobotData != null) {
			for (RobotData RD : DS.NXTRobotData) {
				int circleSize;
				Color circleColor;
				boolean circleShow;

				switch (RD.getType()) {
				case RobotData.POSITION_TYPE_DRIVE:
					circleColor = DRIVING_PATH_CIRCLE_COLOR;
					circleShow = chkShowDrivingPath.getState();
					circleSize = DRIVING_PATH_CIRCLE_SIZE;
					break;
				case RobotData.POSITION_TYPE_BUMP_BUMPER:
					circleColor = BUMPING_BUMPER_CIRCLE_COLOR;
					circleShow = chkShowBumpingPositions.getState();
					circleSize = BUMPING_BUMPER_CIRCLE_SIZE;
					break;
				case RobotData.POSITION_TYPE_BUMP_DISTANCE:
					circleColor = BUMPING_DISTANCE_CIRCLE_COLOR;
					circleShow = chkShowBumpingPositions.getState();
					circleSize = BUMPING_DISTANCE_CIRCLE_SIZE;
					break;
				default:
					circleColor = DEFAULT_CIRCLE_COLOR;
					circleShow = false;
					circleSize = 2;
					break;
				}

				if (circleShow) {
					paintOval(RD.getPosY(), RD.getPosX(), circleColor,
							circleSize, g);
				}

				if (chkShowHeadMap.getState()) {
					Point HeadMapPos = getHeadingPos(RD.getPosX(),
							RD.getPosY(), (-RD.getHeadHeading() + RD
									.getRobotHeading()), RD.getHeadDistance());
					if (RD.getHeadDistance() > OPTICAL_DISTANCE_MIN_LENGTH_MM
							&& RD.getHeadDistance() < OPTICAL_DISTANCE_MAX_LENGTH_MM) {
						paintOval((int) HeadMapPos.getY(), (int) HeadMapPos
								.getX(), HEAD_MAP_CIRCLE_COLOR,
								HEAD_MAP_CIRCLE_SIZE, g);
					}
				}

			}

			if (DS.NXTRobotData.size() > 0) {
				RobotData RD = DS.NXTRobotData.get(DS.NXTRobotData.size() - 1);
				if (chkShowLatestPos.getState()) {
					Color circleColor;
					if (DS.NXTRobotData.size() % 2 == 0) {
						circleColor = LATEST_POS_CIRCLE_COLOR;
					} else {
						circleColor = Color.WHITE;
					}
					paintOval(RD.getPosY(), RD.getPosX(), circleColor,
							LATEST_POS_CIRCLE_SIZE, g);
				}

				// Update texts
				lblRDX.setText(String.valueOf(RD.getPosX()));
				lblRDY.setText(String.valueOf(RD.getPosY()));
				lblRDRobotHeading.setText(String.valueOf(RD.getRobotHeading()));
				lblRDHeadDistance.setText(String.valueOf(RD.getHeadDistance()));
				lblRDHeadHeading.setText(String.valueOf(RD.getHeadHeading()));
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
		PCDataStreamConnection CPDSC = new PCDataStreamConnection();
		NXTC = new NXTCommunication(false, DataFactories, CPDSC);
		NXTC.ConnectAndStartAll(CONNECTION_MODES[cboConnectionTypes
				.getSelectedIndex()], txtConnectToName.getText(),
				txtConnectToAddress.getText());

		// Setup a data processor
		PositionDataProcessor SDP = new PositionDataProcessor(DS, NXTC,
				DataFactories);
		SDP.start();

		// System.out.println(CPDSC.getConnection().getNXTInfo().deviceAddress);

		// Map init center
		mapCenter = new Point((mapPanel.getWidth() / 2),
				(mapPanel.getHeight() / 2));

		while (Active) {
			this.Active = SDP.Active;

			repaint();
			sldMapScale.setValue(curScale);

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
			btnExit.setEnabled(false);
		} else if (AE.getSource() == btnStart) {
			btnStart.setEnabled(false);
			btnExit.setEnabled(true);
			cboConnectionTypes.setEnabled(false);
			txtConnectToName.setEnabled(false);
			txtConnectToAddress.setEnabled(false);

			Thread t = new Thread(this);
			t.start();
		} else if (AE.getSource() == btnOpenData) {
			JFileChooser FC = new JFileChooser();
			String filePath = "";
			FC.showOpenDialog(this);
			try {
				filePath = FC.getSelectedFile().getPath();
			} catch (Exception ex) {
				filePath = "";
			}

			FileInputStream FIS;

			if (filePath.length() > 0) {
				XMLDecoder xdec;
				ArrayList<RobotData> OpenedRobotData = null;

				try {
					FIS = new FileInputStream(filePath);
					xdec = new XMLDecoder(FIS);
					OpenedRobotData = (ArrayList<RobotData>) xdec.readObject();
					xdec.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					OpenedRobotData = null;
				}
				
				if (OpenedRobotData!=null){
					DS.NXTRobotData = OpenedRobotData;
				}
			}
		} else if (AE.getSource() == btnSaveData) {
			JFileChooser FC = new JFileChooser();
			String filePath = "";
			FC.showSaveDialog(this);
			try {
				filePath = FC.getSelectedFile().getPath();
			} catch (Exception ex) {
				filePath = "";
			}

			FileOutputStream FOS;

			if (filePath.length() > 0 && NXTC != null
					&& NXTC.getDataRetrievedQueue() != null) {
				try {
					FOS = new FileOutputStream(filePath);
					XMLEncoder xenc = new XMLEncoder(FOS);
					xenc.writeObject(DS.NXTRobotData);
					xenc.close();
					FOS.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Point getMapPos(int x, int y) {
		return getMapPos(x, y, (float) (curScale * MAP_DEFAULT_SCALE_FACTOR),
				mapCenter.x, mapCenter.y);
	}

	private Point getMapPos(int x, int y, float scale, int centerX, int centerY) {
		return new Point((int) (-y * scale + centerX),
				(int) (x * scale + centerY));
	}

	private Point getHeadingPos(int x, int y, int heading, int distance) {
		int nx;
		int ny;

		nx = (int) (distance * Math.cos(heading * Math.PI / 180)) + x;
		ny = (int) (distance * Math.sin(heading * Math.PI / 180)) + y;

		return new Point(nx, ny);
	}

	private void paintOval(int x, int y, Color c, int size, Graphics g) {
		g.setColor(c);
		Point pos = getMapPos(x, y);
		g.fillOval(pos.x - (size / 2), pos.y - (size / 2), size, size);
	}

	@Override
	public void stateChanged(ChangeEvent ce) {
		if (ce.getSource() == sldMapScale) {
			curScale = sldMapScale.getValue();
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		curScale += -e.getWheelRotation();
		curScale = Math.max(curScale, MAP_MIN_SCALE);
		curScale = Math.min(curScale, MAP_MAX_SCALE);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (mapCenter != null) {
			mapCenterBeforeDrag = (Point) mapCenter.clone();
			startDrag = new Point(e.getX(), e.getY());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			mapCenter = new Point((mapPanel.getWidth() / 2), (mapPanel
					.getHeight() / 2));
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mapCenterBeforeDrag != null && startDrag != null) {
			int x = (int) (mapCenterBeforeDrag.getX() + (e.getX() - startDrag
					.getX()));
			int y = (int) (mapCenterBeforeDrag.getY() + (e.getY() - startDrag
					.getY()));
			mapCenter.setLocation(x, y);
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if (NXTC != null) {
			NXTC.sendShutDown();
		} else {
			System.exit(0);
		}
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

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}