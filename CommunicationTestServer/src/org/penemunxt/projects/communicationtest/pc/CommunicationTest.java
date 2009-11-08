package org.penemunxt.projects.communicationtest.pc;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.*;
import java.beans.*;
import java.io.*;
import java.net.URL;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.penemunxt.communication.*;
import org.penemunxt.communication.pc.*;
import org.penemunxt.graphics.pc.Icons;
import org.penemunxt.projects.communicationtest.*;

public class CommunicationTest extends Applet implements Runnable,
		ActionListener, WindowListener, ChangeListener, MouseWheelListener,
		MouseListener, MouseMotionListener {

	// Constants
	final static String APPLICATION_NAME = "PenemuNXT";

	final static int OPTICAL_DISTANCE_MIN_LENGTH_MM = 200;
	final static int OPTICAL_DISTANCE_MAX_LENGTH_MM = 1500;

	final static Color MAP_PANEL_BACKGROUND_COLOR = Color.WHITE;
	final static Color LEFT_PANEL_BACKGROUND_COLOR = Color.LIGHT_GRAY;
	final static Color BOTTOM_PANEL_BACKGROUND_COLOR = Color.LIGHT_GRAY;

	final static Color DEFAULT_CIRCLE_COLOR = Color.BLACK;
	final static Color LATEST_POS_CIRCLE_COLOR = Color.GREEN;
	final static Color DRIVING_PATH_CIRCLE_COLOR = Color.BLACK;
	final static Color BUMPING_BUMPER_CIRCLE_COLOR = Color.RED;
	final static Color BUMPING_DISTANCE_CIRCLE_COLOR = Color.BLUE;
	final static Color ALIGNED_TO_WALL_CIRCLE_COLOR = Color.CYAN;
	final static Color HEAD_MAP_CIRCLE_COLOR = Color.ORANGE;

	final static int DEFAULT_CIRCLE_SIZE = 2;
	final static int LATEST_POS_CIRCLE_SIZE = 15;
	final static int DRIVING_PATH_CIRCLE_SIZE = 2;
	final static int BUMPING_BUMPER_CIRCLE_SIZE = 10;
	final static int BUMPING_DISTANCE_CIRCLE_SIZE = 10;
	final static int ALIGNED_TO_WALL_CIRCLE_SIZE = 10;
	final static int HEAD_MAP_CIRCLE_SIZE = 5;
	final static int HOT_SPOTS_MAX_CIRCLE_SIZE = 25;

	final static int HOT_SPOTS_MAX_DISTANCE_SQ_TO_NEXT_POSITON = 700;
	final static int HOT_SPOTS_FIND_CONNECTIONS = 5;

	final static int HOT_SPOTS_DEFAULT_FILTER_CONNECTIONS = 5;
	final static int HOT_SPOTS_MIN_FILTER_CONNECTIONS = 0;
	final static int HOT_SPOTS_MAX_FILTER_CONNECTIONS = 15;

	final static float MAP_DEFAULT_SCALE_FACTOR = 0.004f;
	final static int MAP_MIN_SCALE = 1;
	final static int MAP_MAX_SCALE = 100;
	final static int MAP_DEFAULT_SCALE = 50;

	final static int TIMELINE_PLAY_SPEED_MIN = 1;
	final static int TIMELINE_PLAY_SPEED_MAX = 15;
	final static int TIMELINE_PLAY_SPEED_DEFAULT = 5;
	final static int TIMELINE_PLAY_SPEED_MULTIPLIER = 10;

	final static NXTConnectionModes[] CONNECTION_MODES = {
			NXTConnectionModes.USB, NXTConnectionModes.Bluetooth };
	final static String[] CONNECTION_MODES_NAMES = { "USB", "Bluetooth" };
	final static int CONNECTION_MODES_INIT_SELECTED = 1;

	final static String CONNECT_TO_NAME_DEFAULT = "NXT";
	final static String CONNECT_TO_ADDRESS_DEFAULT = "0016530A9000";

	final static Boolean START_FULLSCREEN = false;

	// Menu
	JMenuItem mnuFileOpenButton;
	JMenuItem mnuFileSaveButton;
	JMenuItem mnuFileExitButton;

	// //In Show menu
	JCheckBoxMenuItem chkShowLatestPos;
	JCheckBoxMenuItem chkShowDrivingPath;
	JCheckBoxMenuItem chkShowBumpingPositions;
	JCheckBoxMenuItem chkShowAlignedToWall;
	JCheckBoxMenuItem chkShowHeadMap;
	JCheckBoxMenuItem chkShowHotspots;

	// Buttons
	Button btnExit;
	Button btnConnectAndStart;
	Button btnDisconnectAndStop;
	Button btnTimelineEnableDisable;
	Button btnTimelinePlayPause;

	// Panels
	Panel controlPanel;
	Panel mapPanel;

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
	JSlider sldHotspotsDistanceFilter;
	JSlider sldTimeline;
	JSlider sldTimelineSpeed;

	// Textboxes
	TextField txtConnectToName;
	TextField txtConnectToAddress;

	// Map
	int curScale = MAP_DEFAULT_SCALE;
	Point mapCenter;
	Point startDrag;
	Point mapCenterBeforeDrag;

	// Timeline
	int timelineMin = 0;
	int timelineMax = 1;

	int timelinePrevMin = 0;
	int timelinePrevMax = 1;

	int timelineDefault = 0;

	int timelinePlaySpeed = TIMELINE_PLAY_SPEED_DEFAULT;

	boolean timelinePlay = false;
	boolean timelineEnabled = false;

	Hashtable<Integer, JLabel> TimelineLabelTable;
	MapTimelinePlayer TimelinePlayer;

	// Misc
	boolean AppActive;
	boolean ConnectionActive;
	NXTCommunication NXTC;
	DataShare DS;
	VolatileImage OSI;

	public static void main(String[] args) {
		CommunicationTest PCCT = new CommunicationTest();
		PCCT.init();

		JFrame mainFrame = new JFrame(APPLICATION_NAME);
		mainFrame.addWindowListener(PCCT);
		mainFrame.add(PCCT);

		mainFrame.setJMenuBar(PCCT.getMenuBar());

		URL iconURL = Icons.class.getResource("PenemuNXT_Logo_Icon_16x16.png");
		mainFrame.setIconImage(new ImageIcon(iconURL).getImage());

		mainFrame.setBackground(Color.WHITE);
		Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setSize((int) (ScreenSize.width * 0.85),
				(int) (ScreenSize.height * 0.85));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
		Thread t = new Thread(this);
		t.start();
	}

	public JMenuBar getMenuBar() {
		// Menu
		JMenuBar mnuMainBar = new JMenuBar();

		// Menus
		JMenu mnuFileMenu = new JMenu("File");
		JMenu mnuShowMenu = new JMenu("Show");

		// File menu items
		mnuFileOpenButton = new JMenuItem("Open File...");
		mnuFileOpenButton.addActionListener(this);
		mnuFileSaveButton = new JMenuItem("Save As...");
		mnuFileSaveButton.addActionListener(this);
		mnuFileExitButton = new JMenuItem("Exit");
		mnuFileExitButton.addActionListener(this);

		// Show menu items
		chkShowLatestPos = new JCheckBoxMenuItem("Latest position", true);
		chkShowLatestPos.setForeground(LATEST_POS_CIRCLE_COLOR);
		
		chkShowDrivingPath = new JCheckBoxMenuItem("Driving path", true);
		chkShowDrivingPath.setForeground(DRIVING_PATH_CIRCLE_COLOR);
		
		chkShowBumpingPositions = new JCheckBoxMenuItem("Bumps", true);
		chkShowBumpingPositions.setForeground(BUMPING_BUMPER_CIRCLE_COLOR);
		
		chkShowAlignedToWall = new JCheckBoxMenuItem("Aligned to wall", true);
		chkShowAlignedToWall.setForeground(ALIGNED_TO_WALL_CIRCLE_COLOR);
		
		chkShowHeadMap = new JCheckBoxMenuItem("Map from head", true);
		chkShowHeadMap.setForeground(HEAD_MAP_CIRCLE_COLOR);
		
		chkShowHotspots = new JCheckBoxMenuItem("Hotspots", true);

		// Add everything
		mnuMainBar.add(mnuFileMenu);
		mnuMainBar.add(mnuShowMenu);

		mnuFileMenu.add(mnuFileOpenButton);
		mnuFileMenu.add(mnuFileSaveButton);
		mnuFileMenu.add(new JSeparator());
		mnuFileMenu.add(mnuFileExitButton);

		mnuShowMenu.add(chkShowLatestPos);
		mnuShowMenu.add(chkShowDrivingPath);
		mnuShowMenu.add(chkShowBumpingPositions);
		mnuShowMenu.add(chkShowAlignedToWall);
		mnuShowMenu.add(chkShowHeadMap);
		mnuShowMenu.add(chkShowHotspots);

		return mnuMainBar;
	}

	@Override
	public void init() {
		// Panels
		Panel leftPanel = new Panel();
		Panel rightPanel = new Panel();
		Panel bottomPanel = new Panel();

		controlPanel = new Panel();
		mapPanel = new Panel();
		Panel timelinePanel = new Panel();
		Panel timelineControlPanel = new Panel();

		// Fonts
		Font fntMainHeader = new Font("Arial", Font.BOLD, 22);
		Font fntSectionHeader = new Font("Arial", Font.BOLD, 14);
		Font fntLabelHeader = new Font("Arial", Font.BOLD, 12);

		// Header
		Label lblHeader = new Label(APPLICATION_NAME, Label.CENTER);
		lblHeader.setFont(fntMainHeader);

		// Exit
		btnExit = new Button("Exit");
		btnExit.addActionListener(this);
		btnExit.setEnabled(true);

		// Connect
		Label lblConnectionHeader = new Label("Connection");
		lblConnectionHeader.setFont(fntSectionHeader);

		cboConnectionTypes = new JComboBox(CONNECTION_MODES_NAMES);
		cboConnectionTypes.setSelectedIndex(CONNECTION_MODES_INIT_SELECTED);

		txtConnectToName = new TextField(CONNECT_TO_NAME_DEFAULT, 15);
		txtConnectToAddress = new TextField(CONNECT_TO_ADDRESS_DEFAULT, 15);

		btnConnectAndStart = new Button("Connect and Start");
		btnConnectAndStart.addActionListener(this);
		btnConnectAndStart.setEnabled(true);

		btnDisconnectAndStop = new Button("Disconnect and Stop");
		btnDisconnectAndStop.addActionListener(this);
		btnDisconnectAndStop.setEnabled(false);

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
		pnlConnection.add(btnConnectAndStart, CBC);
		CBC.gridx = 0;
		CBC.gridy = 4;
		CBC.gridwidth = 2;
		pnlConnection.add(btnDisconnectAndStop, CBC);

		// Map scale
		Label lblMapScalesHeader = new Label("Map scale");
		lblMapScalesHeader.setFont(fntSectionHeader);

		sldMapScale = new JSlider(JSlider.HORIZONTAL, MAP_MIN_SCALE,
				MAP_MAX_SCALE, curScale);
		sldMapScale.setMajorTickSpacing(10);
		sldMapScale.setMinorTickSpacing(5);
		sldMapScale.setPaintTicks(true);
		sldMapScale.setPaintLabels(true);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(MAP_MIN_SCALE), new JLabel(MAP_MIN_SCALE
				+ "%"));
		labelTable.put(new Integer(25), new JLabel("25%"));
		labelTable.put(new Integer(50), new JLabel("50%"));
		labelTable.put(new Integer(75), new JLabel("75%"));
		labelTable.put(new Integer(MAP_MAX_SCALE), new JLabel(MAP_MAX_SCALE
				+ "%"));
		sldMapScale.setLabelTable(labelTable);
		sldMapScale.setBackground(LEFT_PANEL_BACKGROUND_COLOR);
		sldMapScale.addChangeListener(this);

		// Hotspots sensitivity
		Label lblHotspotsDistanceFilterHeader = new Label(
				"Hotspots sensitivity");
		lblHotspotsDistanceFilterHeader.setFont(fntSectionHeader);

		sldHotspotsDistanceFilter = new JSlider(JSlider.HORIZONTAL,
				HOT_SPOTS_MIN_FILTER_CONNECTIONS,
				HOT_SPOTS_MAX_FILTER_CONNECTIONS,
				HOT_SPOTS_DEFAULT_FILTER_CONNECTIONS);
		sldHotspotsDistanceFilter.setMajorTickSpacing(5);
		sldHotspotsDistanceFilter.setMinorTickSpacing(1);
		sldHotspotsDistanceFilter.setPaintTicks(true);
		sldHotspotsDistanceFilter.setPaintLabels(true);

		Hashtable<Integer, JLabel> sensitivityLabelTable = new Hashtable<Integer, JLabel>();
		sensitivityLabelTable.put(
				new Integer(HOT_SPOTS_MIN_FILTER_CONNECTIONS), new JLabel(
						String.valueOf(HOT_SPOTS_MIN_FILTER_CONNECTIONS)));
		sensitivityLabelTable.put(
				new Integer(HOT_SPOTS_MAX_FILTER_CONNECTIONS), new JLabel(
						String.valueOf(HOT_SPOTS_MAX_FILTER_CONNECTIONS)));
		sldHotspotsDistanceFilter.setLabelTable(sensitivityLabelTable);
		sldHotspotsDistanceFilter.setBackground(LEFT_PANEL_BACKGROUND_COLOR);

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

		// Timeline
		Label lblTimelineHeader = new Label("Timeline");
		lblTimelineHeader.setFont(fntSectionHeader);

		sldTimeline = new JSlider(JSlider.HORIZONTAL, timelineMin, timelineMax,
				timelineDefault);
		sldTimeline.setPaintTicks(true);
		sldTimeline.setPaintLabels(true);
		sldTimeline.addChangeListener(this);
		sldTimeline.setBackground(BOTTOM_PANEL_BACKGROUND_COLOR);

		// Timeline speed
		Label lblTimelineSpeedHeader = new Label("Speed");
		lblTimelineSpeedHeader.setFont(fntLabelHeader);

		sldTimelineSpeed = new JSlider(JSlider.HORIZONTAL,
				TIMELINE_PLAY_SPEED_MIN, TIMELINE_PLAY_SPEED_MAX,
				TIMELINE_PLAY_SPEED_DEFAULT);
		sldTimelineSpeed.setMajorTickSpacing(5);
		sldTimelineSpeed.setMinorTickSpacing(1);
		sldTimelineSpeed.setPaintTicks(false);
		sldTimelineSpeed.setPaintLabels(false);

		Hashtable<Integer, JLabel> timelineSpeedLabelTable = new Hashtable<Integer, JLabel>();
		timelineSpeedLabelTable.put(new Integer(TIMELINE_PLAY_SPEED_MIN),
				new JLabel(String.valueOf(TIMELINE_PLAY_SPEED_MIN)));
		timelineSpeedLabelTable.put(new Integer(TIMELINE_PLAY_SPEED_MAX),
				new JLabel(String.valueOf(TIMELINE_PLAY_SPEED_MAX)));
		//sldTimelineSpeed.setLabelTable(timelineSpeedLabelTable);
		sldTimelineSpeed.setBackground(BOTTOM_PANEL_BACKGROUND_COLOR);
		sldTimelineSpeed.addChangeListener(this);

		// Timeline buttons

		btnTimelineEnableDisable = new Button("Enable");
		btnTimelineEnableDisable.addActionListener(this);

		btnTimelinePlayPause = new Button("Play");
		btnTimelinePlayPause.addActionListener(this);

		// Control panel
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.add(lblHeader);

		controlPanel.add(btnExit);

		controlPanel.add(lblConnectionHeader);
		controlPanel.add(pnlConnection);

		controlPanel.add(lblMapScalesHeader);
		controlPanel.add(sldMapScale);

		controlPanel.add(lblHotspotsDistanceFilterHeader);
		controlPanel.add(sldHotspotsDistanceFilter);

		controlPanel.add(lblLatestDataHeader);
		controlPanel.add(pnlLatestData);

		// Timeline controlpanel
		timelineControlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		timelineControlPanel.add(btnTimelineEnableDisable);
		timelineControlPanel.add(btnTimelinePlayPause);
		timelineControlPanel.add(lblTimelineSpeedHeader);
		timelineControlPanel.add(sldTimelineSpeed);

		// Timeline panel
		timelinePanel.setLayout(new BoxLayout(timelinePanel, BoxLayout.Y_AXIS));
		timelinePanel.add(lblTimelineHeader);
		timelinePanel.add(timelineControlPanel);
		timelinePanel.add(sldTimeline);

		// Bottom panel
		bottomPanel.setBackground(BOTTOM_PANEL_BACKGROUND_COLOR);
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(timelinePanel, BorderLayout.CENTER);

		// Left panel
		leftPanel.setBackground(LEFT_PANEL_BACKGROUND_COLOR);
		leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		leftPanel.add(controlPanel);

		// Right panel
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(mapPanel, BorderLayout.CENTER);
		rightPanel.add(bottomPanel, BorderLayout.SOUTH);

		// Add it to the applet
		this.setLayout(new BorderLayout());
		this.add(leftPanel, BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.CENTER);

		// Map panel
		mapPanel.setBackground(MAP_PANEL_BACKGROUND_COLOR);
		mapPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));

		// Map Listeners
		mapPanel.addMouseMotionListener(this);
		mapPanel.addMouseListener(this);
		mapPanel.addMouseWheelListener(this);

		// Set active
		AppActive = true;
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

	@Override
	public void paint(Graphics g) {
		ArrayList<MapPositionPoints> ObjectPositions = new ArrayList<MapPositionPoints>();

		if (DS != null && DS.NXTRobotData != null) {
			for (RobotData RD : DS.NXTRobotData) {
				if (DS.NXTRobotData.indexOf(RD) + 1 > sldTimeline.getValue()) {
					break;
				}
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
				case RobotData.POSITION_TYPE_ALIGNED:
					circleColor = ALIGNED_TO_WALL_CIRCLE_COLOR;
					circleShow = chkShowAlignedToWall.getState();
					circleSize = ALIGNED_TO_WALL_CIRCLE_SIZE;
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

				if (chkShowHeadMap.getState() || chkShowHotspots.getState()) {
					Point HeadMapPos = getHeadingPos(RD.getPosX(),
							RD.getPosY(), (-RD.getHeadHeading() + RD
									.getRobotHeading()), RD.getHeadDistance());
					if (RD.getHeadDistance() > OPTICAL_DISTANCE_MIN_LENGTH_MM
							&& RD.getHeadDistance() < OPTICAL_DISTANCE_MAX_LENGTH_MM) {

						if (chkShowHotspots.getState()) {
							ObjectPositions.add(new MapPositionPoints(0,
									(int) HeadMapPos.getX(), (int) HeadMapPos
											.getY()));
						}

						if (chkShowHeadMap.getState()) {
							paintOval((int) HeadMapPos.getY(), (int) HeadMapPos
									.getX(), HEAD_MAP_CIRCLE_COLOR,
									HEAD_MAP_CIRCLE_SIZE, g);
						}
					}
				}

			}

			if (DS.NXTRobotData.size() > 0) {
				RobotData RD = DS.NXTRobotData.get(Math.max(sldTimeline
						.getValue() - 1, 0));

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

			if (ObjectPositions != null && chkShowHotspots.getState()) {
				MapPositionPoints.GetPositionsPoints(ObjectPositions,
						HOT_SPOTS_MAX_DISTANCE_SQ_TO_NEXT_POSITON,
						HOT_SPOTS_FIND_CONNECTIONS);
				int maxPoints = MapPositionPoints.GetMaxPoints(ObjectPositions);

				for (MapPositionPoints ScanPoint : ObjectPositions) {
					float pointPercentage = (ScanPoint.getPoints() / (float) maxPoints);
					Color c = new Color((int) (pointPercentage * 255),
							255 - (int) (pointPercentage * 255), 0);

					if (ScanPoint.getPoints() > sldHotspotsDistanceFilter
							.getValue()) {
						paintOval(
								ScanPoint.getY(),
								ScanPoint.getX(),
								c,
								(int) (pointPercentage * HOT_SPOTS_MAX_CIRCLE_SIZE),
								g);
					}
				}
			}
		}
	}

	@Override
	public void run() {
		// Object to share data internal
		DS = new DataShare();

		// Map init center
		mapCenter = new Point((mapPanel.getWidth() / 2),
				(mapPanel.getHeight() / 2));

		while (AppActive) {
			repaint();
			sldMapScale.setValue(curScale);

			if (DS != null && DS.NXTRobotData != null
					&& DS.NXTRobotData.size() > 0) {
				btnTimelineEnableDisable.setEnabled(true);

				timelineMin = 0;
				timelineMax = DS.NXTRobotData.size();

				sldTimeline.setMinimum(timelineMin);
				sldTimeline.setMaximum(timelineMax);
				if (!timelineEnabled) {
					sldTimeline.setValue(timelineMax);
				}

				if (sldTimeline.getValue() == sldTimeline.getMaximum()) {
					stopTimelineAutoPlay();
				}

				sldTimeline
						.setMajorTickSpacing((int) ((timelineMax - timelineMin) / 5.0));
				sldTimeline
						.setMinorTickSpacing((int) ((timelineMax - timelineMin) / 10.0));

				if (timelineMin != timelinePrevMin
						|| timelineMax != timelinePrevMin) {
					if (TimelineLabelTable == null) {
						TimelineLabelTable = new Hashtable<Integer, JLabel>();
					}

					TimelineLabelTable.clear();
					TimelineLabelTable.put(new Integer(timelineMin),
							new JLabel(String.valueOf(timelineMin)));

					TimelineLabelTable.put(new Integer(
							(timelineMax - timelineMin) / 2), new JLabel(String
							.valueOf((timelineMax - timelineMin) / 2)));

					TimelineLabelTable.put(new Integer(timelineMax),
							new JLabel(String.valueOf(timelineMax)));
					sldTimeline.setLabelTable(TimelineLabelTable);
				}
			} else {
				btnTimelineEnableDisable.setEnabled(false);
				disableTimeline();

				sldTimeline.setMinimum(0);
				sldTimeline.setMaximum(1);
				sldTimeline.setMajorTickSpacing(2);
				sldTimeline.setMinorTickSpacing(1);
			}

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}

			timelinePrevMin = timelineMin;
			timelinePrevMax = timelineMax;
		}

		System.exit(0);
	}

	public void runConnection() {
		ConnectionActive = true;

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

		while (ConnectionActive) {
			this.ConnectionActive = SDP.Active;

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}

		NXTC.Disconnect();
		NXTC = null;
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnExit || ae.getSource() == mnuFileExitButton) {
			exitApp();
		} else if (ae.getSource() == btnConnectAndStart) {
			connectAndStart();
		} else if (ae.getSource() == btnDisconnectAndStop) {
			disconnectAndStop();
		} else if (ae.getSource() == mnuFileOpenButton) {
			openData();
		} else if (ae.getSource() == mnuFileSaveButton) {
			saveData();
		} else if (ae.getSource() == btnTimelinePlayPause) {
			switchTimelineAutoPlay();
		} else if (ae.getSource() == btnTimelineEnableDisable) {
			switchTimelineEnabled();
		}
	}

	private void exitApp() {
		AppActive = false;
		btnExit.setEnabled(false);
	}

	private void disconnectAndStop() {
		if (NXTC != null) {
			NXTC.sendShutDown();
		}

		btnExit.setEnabled(true);
		btnConnectAndStart.setEnabled(true);
		btnDisconnectAndStop.setEnabled(false);
		cboConnectionTypes.setEnabled(true);
		txtConnectToName.setEnabled(true);
		txtConnectToAddress.setEnabled(true);
	}

	private void connectAndStart() {
		btnExit.setEnabled(false);
		btnConnectAndStart.setEnabled(false);
		btnDisconnectAndStop.setEnabled(true);
		cboConnectionTypes.setEnabled(false);
		txtConnectToName.setEnabled(false);
		txtConnectToAddress.setEnabled(false);

		runConnection();
	}

	private void openData() {
		JFileChooser FC = new JFileChooser();
		String filePath = "";
		FC.addChoosableFileFilter(new PenemuNXTMapFileFilter());
		if (FC.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
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

				if (OpenedRobotData != null) {
					DS.NXTRobotData = OpenedRobotData;
				}
			}
		}
	}

	private void saveData() {
		JFileChooser FC = new JFileChooser();
		String filePath = "";
		FC.addChoosableFileFilter(new PenemuNXTMapFileFilter());
		if (FC.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				filePath = FC.getSelectedFile().getPath();
			} catch (Exception ex) {
				filePath = "";
			}

			if (!filePath
					.endsWith(PenemuNXTMapFileFilter.ALLOWED_FILE_EXTENSION)) {
				filePath += PenemuNXTMapFileFilter.ALLOWED_FILE_EXTENSION;
			}

			System.out.println(filePath);

			FileOutputStream FOS;

			if (filePath.length() > 0 && DS != null && DS.NXTRobotData != null) {
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

	private void paintOval(int x, int y, Color c, int size, Graphics g) {
		g.setColor(c);
		Point pos = getMapPos(x, y);
		g.fillOval(pos.x - (size / 2), pos.y - (size / 2), size, size);
	}

	private void switchTimelineEnabled() {
		if (timelineEnabled) {
			disableTimeline();
		} else {
			enableTimeline();
		}
	}

	private void enableTimeline() {
		timelineEnabled = true;
		stopTimelineAutoPlay();
		btnTimelineEnableDisable.setLabel("Disable");
		btnTimelinePlayPause.setEnabled(true);

		sldTimelineSpeed.setEnabled(true);
		sldTimeline.setEnabled(true);
	}

	private void disableTimeline() {
		timelineEnabled = false;
		btnTimelineEnableDisable.setLabel("Enable");
		btnTimelinePlayPause.setEnabled(false);

		sldTimelineSpeed.setEnabled(false);
		sldTimeline.setEnabled(false);

	}

	private void switchTimelineAutoPlay() {
		if (timelinePlay) {
			stopTimelineAutoPlay();
		} else {
			startTimelineAutoPlay();
		}
	}

	private void startTimelineAutoPlay() {
		stopTimelineAutoPlay();
		timelinePlay = true;
		btnTimelinePlayPause.setLabel("Pause");
		TimelinePlayer = new MapTimelinePlayer(sldTimeline, getTimelineDelayFromSpeed(timelinePlaySpeed));
		TimelinePlayer.start();
	}

	private void stopTimelineAutoPlay() {
		timelinePlay = false;
		btnTimelinePlayPause.setLabel("Play");
		if (TimelinePlayer != null) {
			TimelinePlayer.deactivate();
			TimelinePlayer = null;
		}
	}
	
	private int getTimelineDelayFromSpeed(int speed){
		return ((TIMELINE_PLAY_SPEED_MAX - speed + TIMELINE_PLAY_SPEED_MIN) * TIMELINE_PLAY_SPEED_MULTIPLIER);
	}

	@Override
	public void stateChanged(ChangeEvent ce) {
		if (ce.getSource() == sldMapScale) {
			curScale = sldMapScale.getValue();
		} else if (ce.getSource() == sldTimelineSpeed) {
			timelinePlaySpeed = sldTimelineSpeed.getValue();
			if(TimelinePlayer!=null){
				TimelinePlayer.setDelay(getTimelineDelayFromSpeed(timelinePlaySpeed));
			}
		} else if (ce.getSource() == sldTimeline) {
			// stopTimelineAutoPlay();
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
			curScale = MAP_DEFAULT_SCALE;
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
			disconnectAndStop();
			exitApp();
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