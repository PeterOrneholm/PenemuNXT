package org.penemunxt.projects.penemunxtexplorer.pc;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.penemunt.windows.pc.DataTableWindow;
import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.communication.NXTConnectionModes;
import org.penemunxt.graphics.pc.Icons;
import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.connection.DataShare;
import org.penemunxt.projects.penemunxtexplorer.pc.connection.RobotConnection;
import org.penemunxt.projects.penemunxtexplorer.pc.map.MapFileUtilities;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.IMapProcessor;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapProcessors;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapProcessorsList;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapUtilities;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors.MapAligned;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors.MapBumpBumper;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors.MapBumpDistance;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors.MapCurrentPos;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors.MapDrivingPath;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors.MapFindLines;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors.MapHeadObjects;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors.MapHotspots;

public class PenemuNXTExplorerControl extends Applet implements Runnable,
		ActionListener, WindowListener, ComponentListener, ChangeListener, MouseWheelListener,
		MouseListener, MouseMotionListener {

	// Constants

	// // Application
	final static String APPLICATION_NAME = "PenemuNXT - Explorer control";
	final static ImageIcon APPLICATION_ICON = Icons.PENEMUNXT_CIRCLE_LOGO_ICON_16_X_16_ICON;
	final static ImageIcon APPLICATION_LOGO = Icons.PENEMUNXT_LOGO_LANDSCAPE_ICON;
	final static Boolean APPLICATION_START_FULLSCREEN = false;

	// // Maps
	final static String DEFAULT_FOLDER_PATH = "C:\\Documents and Settings\\Peter\\Mina dokument\\Projects\\PenemuNXT\\Data\\Maps\\";
	final static String PRELOAD_PENEMUNXT_MAP_PATH = "C:\\Documents and Settings\\Peter\\Mina dokument\\Projects\\PenemuNXT\\Data\\Maps\\NXT5.penemunxtmap";
	// final static String PRELOAD_PENEMUNXT_MAP_PATH = "";

	// //Events and Algorithms basics
	final static boolean LATEST_POS_SHOW_DEFAULT = true;
	final static boolean DRIVING_PATH_SHOW_DEFAULT = true;
	final static boolean BUMPING_BUMPER_SHOW_DEFAULT = true;
	final static boolean BUMPING_DISTANCE_SHOW_DEFAULT = true;
	final static boolean ALIGNED_TO_WALL_SHOW_DEFAULT = true;
	final static boolean HEAD_MAP_SHOW_DEFAULT = true;
	final static boolean HOT_SPOTS_SHOW_DEFAULT = false;
	final static boolean FIND_WALLS_SHOW_DEFAULT = true;

	// //// Drawings
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
	final static int HOT_SPOTS_MAX_CIRCLE_SIZE = 20;

	// //// Scale
	final static float MAP_DEFAULT_SCALE_FACTOR = 0.004f;
	final static int MAP_MIN_SCALE = 1;
	final static int MAP_MAX_SCALE = 100;
	final static int MAP_DEFAULT_SCALE = 35;

	// // UI
	final static Color DEFAULT_PANEL_BACKGROUND_COLOR = new Color(197, 209, 215);
	final static Color MAP_PANEL_BACKGROUND_COLOR = Color.WHITE;
	final static Color VIEW_PANEL_BACKGROUND_COLOR = DEFAULT_PANEL_BACKGROUND_COLOR;
	final static Color LEFT_PANEL_BACKGROUND_COLOR = DEFAULT_PANEL_BACKGROUND_COLOR;
	final static Color BOTTOM_PANEL_BACKGROUND_COLOR = DEFAULT_PANEL_BACKGROUND_COLOR;

	final static Color MAP_PANEL_BORDER_COLOR = Color.BLACK;
	final static int MAP_PANEL_BORDER_WIDTH = 2;

	final static int PANEL_MARGIN = 15;
	final static int LOGO_MARGIN_BOTTOM = 15;

	// // Connection
	final static NXTConnectionModes[] CONNECTION_MODES = {
			NXTConnectionModes.USB, NXTConnectionModes.Bluetooth };
	final static String[] CONNECTION_MODES_NAMES = { "USB", "Bluetooth" };
	final static int CONNECTION_MODES_INIT_SELECTED = 1;

	final static String CONNECT_TO_NAME_DEFAULT = "NXT";
	final static String CONNECT_TO_ADDRESS_DEFAULT = "0016530A9000";

	final static int ALGORITHMS_SENSITIVITY_DEFAULT = 5;
	final static int ALGORITHMS_SENSITIVITY_MIN = 0;
	final static int ALGORITHMS_SENSITIVITY_MAX = 15;

	// // Timeline
	final static int TIMELINE_PLAY_SPEED_MIN = 1;
	final static int TIMELINE_PLAY_SPEED_MAX = 15;
	final static int TIMELINE_PLAY_SPEED_DEFAULT = 5;
	final static int TIMELINE_PLAY_SPEED_MULTIPLIER = 10;

	// Variables

	// // Menu
	JMenuItem mnuFileOpenDataViewButton;
	JMenuItem mnuFileOpenMapProcessorsButton;
	JMenuItem mnuFileOpenButton;
	JMenuItem mnuFileSaveButton;
	JMenuItem mnuFileExitButton;

	// // Map menu
	JMenuItem mnuMapClearButton;
	JMenu mnuMapProcessors;

	// // Buttons
	Button btnExit;
	Button btnConnectAndStart;
	Button btnDisconnectAndStop;
	Button btnTimelineEnableDisable;
	Button btnTimelinePlayPause;
	Button btnTimelineRewind;

	// // Panels
	Panel controlPanel;
	JPanel mapPanel;

	// // Labels
	Label lblRDX;
	Label lblRDY;
	Label lblRDRobotHeading;
	Label lblRDHeadDistance;
	Label lblRDHeadHeading;
	Label lblTimelineCurrentFrame;

	// // Comboboxes
	JComboBox cboConnectionTypes;

	// // Sliders
	JSlider sldMapScale;
	JSlider sldAlgorithmsSensitivityFilter;
	JSlider sldTimeline;
	JSlider sldTimelineSpeed;

	// // Textboxes
	TextField txtConnectToName;
	TextField txtConnectToAddress;

	// // Map
	int curScale = MAP_DEFAULT_SCALE;
	Point mapCenter;
	Point startDrag;
	Point mapCenterBeforeDrag;

	// // Processors
	MapProcessors mapProcessors;
	MapAligned mapProcessorAligned;
	MapBumpBumper mapProcessorBumpBumper;
	MapCurrentPos mapProcessorCurrentPos;
	MapBumpDistance mapProcessorBumpDistance;
	MapDrivingPath mapProcessorDrivingPath;
	MapFindLines mapProcessorFindLines;
	MapHeadObjects mapProcessorHeadObjects;
	MapHotspots mapProcessorHotspots;

	// // Timeline
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

	// // Misc
	boolean AppActive;
	NXTCommunication NXTC;
	DataShare DS;
	RobotConnection RC;
	VolatileImage OSI;
	PenemuNXTExplorerDataViewer DataView;
	MapProcessorsList MapProcessorsListView;

	// Functions

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	@Override
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	public PenemuNXTExplorerControl() {
		AppActive = true;
		this.setLayout(new BorderLayout());
		this.add(getContentPanel(), BorderLayout.CENTER);
	}

	private static void createAndShowGUI() {
		PenemuNXTExplorerControl PCCT = new PenemuNXTExplorerControl();

		JFrame mainFrame = new JFrame(APPLICATION_NAME);
		mainFrame.addWindowListener(PCCT);
		mainFrame.add(PCCT);

		mainFrame.setIconImage(APPLICATION_ICON.getImage());

		mainFrame.setBackground(Color.WHITE);
		Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setSize((int) (ScreenSize.width * 0.85),
				(int) (ScreenSize.height * 0.85));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		if (APPLICATION_START_FULLSCREEN) {
			mainFrame.setUndecorated(true);
			mainFrame.pack();
			mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		}
		mainFrame.setVisible(true);
		PCCT.start();
	}

	public JMenu getMapMenu() {
		JMenu mnuMapMenu = new JMenu("Map");
		mnuMapProcessors = new JMenu("Processors");

		// Map menu items
		mnuMapClearButton = new JMenuItem("Clear");
		mnuMapClearButton.addActionListener(this);

		mnuMapMenu.add(mnuMapClearButton);
		mnuMapMenu.add(new JSeparator());
		mnuMapMenu.add(mnuMapProcessors);

		return mnuMapMenu;
	}

	public JMenuBar getMenuBar() {
		// Menu bar
		JMenuBar mnuMainBar = new JMenuBar();

		// Menus
		JMenu mnuFileMenu = new JMenu("File");

		// File menu
		mnuFileMenu.setIcon(APPLICATION_ICON);

		mnuFileOpenDataViewButton = new JMenuItem("Show data...");
		mnuFileOpenDataViewButton.addActionListener(this);
		mnuFileOpenMapProcessorsButton = new JMenuItem("Show map processors...");
		mnuFileOpenMapProcessorsButton.addActionListener(this);
		mnuFileOpenButton = new JMenuItem("Open File...");
		mnuFileOpenButton.addActionListener(this);
		mnuFileSaveButton = new JMenuItem("Save As...");
		mnuFileSaveButton.addActionListener(this);
		mnuFileExitButton = new JMenuItem("Exit");
		mnuFileExitButton.addActionListener(this);

		// Add everything
		mnuMainBar.add(mnuFileMenu);
		mnuMainBar.add(getMapMenu());

		mnuFileMenu.add(mnuFileOpenButton);
		mnuFileMenu.add(mnuFileSaveButton);
		mnuFileMenu.add(new JSeparator());
		mnuFileMenu.add(mnuFileOpenDataViewButton);
		mnuFileMenu.add(mnuFileOpenMapProcessorsButton);
		mnuFileMenu.add(new JSeparator());
		mnuFileMenu.add(mnuFileExitButton);

		return mnuMainBar;
	}

	public Panel getContentPanel() {
		// Panels
		Panel mainPanel = new Panel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		JPanel viewPanel = new JPanel();
		JPanel mapPanelWrapper = new JPanel();

		controlPanel = new Panel();
		mapPanel = new JPanel();
		Panel timelinePanel = new Panel();
		Panel timelineControlPanel = new Panel();

		// Fonts
		Font fntMainHeader = new Font("Arial", Font.BOLD, 22);
		Font fntSectionHeader = new Font("Arial", Font.BOLD, 14);
		Font fntLabelHeader = new Font("Arial", Font.BOLD, 12);

		// Header
		Label lblHeader = new Label(APPLICATION_NAME, Label.CENTER);
		lblHeader.setFont(fntMainHeader);

		// Image logo
		JPanel logoPanel = new JPanel(new BorderLayout());
		JLabel lblLogo = new JLabel("", APPLICATION_LOGO, SwingConstants.CENTER);

		logoPanel.setBackground(LEFT_PANEL_BACKGROUND_COLOR);
		logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0,
				LOGO_MARGIN_BOTTOM, 0));
		logoPanel.add(lblLogo, BorderLayout.CENTER);

		// Exit
		btnExit = new Button("Exit");
		btnExit.addActionListener(this);
		btnExit.setEnabled(true);

		// Connect
		Label lblConnectionHeader = new Label("Connection");
		lblConnectionHeader.setFont(fntSectionHeader);

		Label lblConnectionMode = new Label("Mode:");
		lblConnectionMode.setFont(fntLabelHeader);

		Label lblConnectionNXTName = new Label("NXT name:");
		lblConnectionNXTName.setFont(fntLabelHeader);

		Label lblConnectionNXTAddress = new Label("NXT address:");
		lblConnectionNXTAddress.setFont(fntLabelHeader);

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
		pnlConnection.add(lblConnectionMode, CBC);
		CBC.gridx = 1;
		CBC.gridy = 0;
		pnlConnection.add(cboConnectionTypes, CBC);
		CBC.gridx = 0;
		CBC.gridy = 1;
		pnlConnection.add(lblConnectionNXTName, CBC);
		CBC.gridx = 1;
		CBC.gridy = 1;
		pnlConnection.add(txtConnectToName, CBC);
		CBC.gridx = 0;
		CBC.gridy = 2;
		pnlConnection.add(lblConnectionNXTAddress, CBC);
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

		sldMapScale = new JSlider(SwingConstants.HORIZONTAL, MAP_MIN_SCALE,
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

		// Algorithms sensitivity
		Label lblAlgorithmsSensitivityFilterHeader = new Label(
				"Algorithms sensitivity");
		lblAlgorithmsSensitivityFilterHeader.setFont(fntSectionHeader);

		sldAlgorithmsSensitivityFilter = new JSlider(SwingConstants.HORIZONTAL,
				ALGORITHMS_SENSITIVITY_MIN, ALGORITHMS_SENSITIVITY_MAX,
				ALGORITHMS_SENSITIVITY_DEFAULT);
		sldAlgorithmsSensitivityFilter.setMajorTickSpacing(5);
		sldAlgorithmsSensitivityFilter.setMinorTickSpacing(1);
		sldAlgorithmsSensitivityFilter
				.setBackground(LEFT_PANEL_BACKGROUND_COLOR);

		// Current data
		Label lblCurrentDataHeader = new Label("Current");
		lblCurrentDataHeader.setFont(fntSectionHeader);
		Panel pnlCurrentData = new Panel(new GridLayout(0, 2));

		lblRDX = new Label();
		lblRDY = new Label();
		lblRDRobotHeading = new Label();
		lblRDHeadDistance = new Label();
		lblRDHeadHeading = new Label();
		lblTimelineCurrentFrame = new Label();

		Label lblRDXHeader = new Label("X:");
		Label lblRDYHeader = new Label("Y:");
		Label lblRDRobotHeadingHeader = new Label("Robot heading:");
		Label lblRDHeadDistanceHeader = new Label("Head distance:");
		Label lblRDHeadHeadingHeader = new Label("Head heading:");
		Label lblTimelineCurrentFrameHeader = new Label("Frame:");

		lblRDXHeader.setFont(fntLabelHeader);
		lblRDYHeader.setFont(fntLabelHeader);
		lblRDRobotHeadingHeader.setFont(fntLabelHeader);
		lblRDHeadDistanceHeader.setFont(fntLabelHeader);
		lblRDHeadHeadingHeader.setFont(fntLabelHeader);
		lblTimelineCurrentFrameHeader.setFont(fntLabelHeader);

		pnlCurrentData.add(lblRDXHeader);
		pnlCurrentData.add(lblRDX);
		pnlCurrentData.add(lblRDYHeader);
		pnlCurrentData.add(lblRDY);
		pnlCurrentData.add(lblRDRobotHeadingHeader);
		pnlCurrentData.add(lblRDRobotHeading);
		pnlCurrentData.add(lblRDHeadDistanceHeader);
		pnlCurrentData.add(lblRDHeadDistance);
		pnlCurrentData.add(lblRDHeadHeadingHeader);
		pnlCurrentData.add(lblRDHeadHeading);
		pnlCurrentData.add(lblTimelineCurrentFrameHeader);
		pnlCurrentData.add(lblTimelineCurrentFrame);

		// Timeline
		Label lblTimelineHeader = new Label("Timeline");
		lblTimelineHeader.setFont(fntSectionHeader);
		JPanel timelineWrapperPanel = new JPanel();

		sldTimeline = new JSlider(SwingConstants.HORIZONTAL, timelineMin,
				timelineMax, timelineDefault);
		sldTimeline.setPaintTicks(true);
		sldTimeline.setPaintLabels(true);
		sldTimeline.addChangeListener(this);
		sldTimeline.setBackground(BOTTOM_PANEL_BACKGROUND_COLOR);

		// Timeline speed
		Label lblTimelineSpeedHeader = new Label("Speed");
		lblTimelineSpeedHeader.setFont(fntLabelHeader);

		sldTimelineSpeed = new JSlider(SwingConstants.HORIZONTAL,
				TIMELINE_PLAY_SPEED_MIN, TIMELINE_PLAY_SPEED_MAX,
				TIMELINE_PLAY_SPEED_DEFAULT);
		sldTimelineSpeed.setPaintTicks(false);
		sldTimelineSpeed.setPaintLabels(false);
		sldTimelineSpeed.setBackground(BOTTOM_PANEL_BACKGROUND_COLOR);
		sldTimelineSpeed.addChangeListener(this);

		// Timeline buttons

		btnTimelineEnableDisable = new Button("Enable");
		btnTimelineEnableDisable.addActionListener(this);

		btnTimelinePlayPause = new Button("Play");
		btnTimelinePlayPause.addActionListener(this);

		btnTimelineRewind = new Button("Rewind");
		btnTimelineRewind.addActionListener(this);

		// Control panel
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		// controlPanel.add(lblHeader);
		controlPanel.add(logoPanel);
		// controlPanel.add(btnExit);

		controlPanel.add(lblConnectionHeader);
		controlPanel.add(pnlConnection);

		controlPanel.add(lblMapScalesHeader);
		controlPanel.add(sldMapScale);

		//controlPanel.add(lblAlgorithmsSensitivityFilterHeader);
		//controlPanel.add(sldAlgorithmsSensitivityFilter);

		controlPanel.add(lblCurrentDataHeader);
		controlPanel.add(pnlCurrentData);

		// Timeline controlpanel
		timelineControlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		timelineControlPanel.add(btnTimelineEnableDisable);
		timelineControlPanel.add(btnTimelineRewind);
		timelineControlPanel.add(btnTimelinePlayPause);
		timelineControlPanel.add(lblTimelineSpeedHeader);
		timelineControlPanel.add(sldTimelineSpeed);

		// Timeline panel
		timelinePanel.setLayout(new BoxLayout(timelinePanel, BoxLayout.Y_AXIS));
		timelinePanel.add(lblTimelineHeader);
		timelinePanel.add(timelineControlPanel);
		timelinePanel.add(sldTimeline);

		// Bottom panel
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, PANEL_MARGIN,
				PANEL_MARGIN, PANEL_MARGIN));
		bottomPanel.setBackground(BOTTOM_PANEL_BACKGROUND_COLOR);
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(timelinePanel, BorderLayout.CENTER);

		// Left panel
		leftPanel.setBorder(BorderFactory.createEmptyBorder(PANEL_MARGIN,
				PANEL_MARGIN, PANEL_MARGIN, 0));
		leftPanel.setBackground(LEFT_PANEL_BACKGROUND_COLOR);
		leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		leftPanel.add(controlPanel);

		// Right panel
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(viewPanel, BorderLayout.CENTER);
		rightPanel.add(bottomPanel, BorderLayout.SOUTH);

		// Main panel
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(getMenuBar(), BorderLayout.NORTH);
		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(rightPanel, BorderLayout.CENTER);

		// View panel
		viewPanel.setLayout(new BorderLayout());
		viewPanel.setBackground(VIEW_PANEL_BACKGROUND_COLOR);
		viewPanel.setBorder(BorderFactory.createEmptyBorder(PANEL_MARGIN,
				PANEL_MARGIN, PANEL_MARGIN, PANEL_MARGIN));
		viewPanel.add(mapPanelWrapper, BorderLayout.CENTER);

		// Map panel wrapper
		mapPanelWrapper.setLayout(new BorderLayout());
		mapPanelWrapper.add(mapPanel, BorderLayout.CENTER);
		mapPanelWrapper.setBorder(BorderFactory.createLineBorder(
				MAP_PANEL_BORDER_COLOR, MAP_PANEL_BORDER_WIDTH));

		// Map panel
		mapPanel.setBackground(MAP_PANEL_BACKGROUND_COLOR);
		mapPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));

		// Map Listeners
		mapPanel.addMouseMotionListener(this);
		mapPanel.addMouseListener(this);
		mapPanel.addMouseWheelListener(this);

		return mainPanel;
	}

	private void setupMapProcessors() {
		ArrayList<IMapProcessor> defaultProcessors = new ArrayList<IMapProcessor>();

		mapProcessorAligned = new MapAligned(ALIGNED_TO_WALL_CIRCLE_COLOR,
				ALIGNED_TO_WALL_CIRCLE_SIZE, ALIGNED_TO_WALL_SHOW_DEFAULT);
		mapProcessorBumpBumper = new MapBumpBumper(BUMPING_BUMPER_CIRCLE_COLOR,
				BUMPING_BUMPER_CIRCLE_SIZE, BUMPING_BUMPER_SHOW_DEFAULT);
		mapProcessorBumpDistance = new MapBumpDistance(
				BUMPING_DISTANCE_CIRCLE_COLOR, BUMPING_DISTANCE_CIRCLE_SIZE,
				BUMPING_DISTANCE_SHOW_DEFAULT);
		mapProcessorCurrentPos = new MapCurrentPos(LATEST_POS_CIRCLE_COLOR,
				LATEST_POS_CIRCLE_SIZE, LATEST_POS_SHOW_DEFAULT);
		mapProcessorDrivingPath = new MapDrivingPath(DRIVING_PATH_CIRCLE_COLOR,
				DRIVING_PATH_CIRCLE_SIZE, DRIVING_PATH_SHOW_DEFAULT);
		mapProcessorHeadObjects = new MapHeadObjects(HEAD_MAP_CIRCLE_COLOR,
				HEAD_MAP_CIRCLE_SIZE, HEAD_MAP_SHOW_DEFAULT);

		mapProcessorHotspots = new MapHotspots(HOT_SPOTS_MAX_CIRCLE_SIZE, false);
		mapProcessorFindLines = new MapFindLines(true);

		defaultProcessors.add(mapProcessorCurrentPos);
		defaultProcessors.add(mapProcessorDrivingPath);
		defaultProcessors.add(mapProcessorHeadObjects);

		defaultProcessors.add(mapProcessorBumpBumper);
		defaultProcessors.add(mapProcessorBumpDistance);
		defaultProcessors.add(mapProcessorAligned);

		defaultProcessors.add(mapProcessorHotspots);
		defaultProcessors.add(mapProcessorFindLines);

		mapProcessors = new MapProcessors(defaultProcessors);

		setupMapProcessorsMenu();
	}

	private void setupMapProcessorsMenu() {
		mnuMapProcessors.removeAll();
		for (IMapProcessor.MapProcessorType processorType : IMapProcessor.MapProcessorType
				.values()) {
			JMenu mnuCategory = new JMenu(processorType.name());
			for (final IMapProcessor mapProcessor : mapProcessors.getList()) {
				if (mapProcessor.getType() == processorType) {
					final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(
							mapProcessor.getName(), mapProcessor.isEnabled());
					// menuItem.setBackground(mapProcessor.getColor());
					menuItem.setForeground(mapProcessor.getColor());
					menuItem.setToolTipText(mapProcessor.getDescription());

					menuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							mapProcessor.setEnabled(menuItem.getState());
							if(MapProcessorsListView!=null){
								MapProcessorsListView.refresh();
							}
							refreshMap();
						}
					});
					mnuCategory.add(menuItem);
				}
			}
			mnuMapProcessors.add(mnuCategory);
		}
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
		if (mapProcessors != null && DS != null && DS.NXTRobotData != null) {
			mapProcessors.processData(DS.NXTRobotData, sldTimeline.getValue(),
					(curScale * MAP_DEFAULT_SCALE_FACTOR), (int) mapCenter
							.getX(), (int) mapCenter.getY(), g);
		}
	}

	public void refreshLatestData() {
		RobotData LatestData = null;
		if (DS != null && DS.NXTRobotData != null) {
			LatestData = MapUtilities.getLatestData(DS.NXTRobotData,
					sldTimeline.getValue() - 1);
		}

		if (DS != null && DS.NXTRobotData != null && LatestData != null) {
			lblRDX.setText(String.valueOf(LatestData.getPosX()));
			lblRDY.setText(String.valueOf(LatestData.getPosY()));
			lblRDRobotHeading.setText(String.valueOf(LatestData
					.getRobotHeading()));
			lblRDHeadDistance.setText(String.valueOf(LatestData
					.getHeadDistance()));
			lblRDHeadHeading.setText(String
					.valueOf(LatestData.getHeadHeading()));
			lblTimelineCurrentFrame.setText(String.valueOf(sldTimeline
					.getValue()));
		} else {
			lblRDHeadDistance.setText("");
			lblRDHeadHeading.setText("");
			lblRDRobotHeading.setText("");
			lblRDX.setText("");
			lblRDY.setText("");
			lblTimelineCurrentFrame.setText("");
		}
	}

	public void refreshMap() {
		repaint();
	}

	@Override
	public void run() {
		// Object to share data internal
		DS = new DataShare();

		// Map init center
		mapCenter = new Point((mapPanel.getWidth() / 2),
				(mapPanel.getHeight() / 2));

		// Setup the map data processors
		setupMapProcessors();

		// Auto open
		if (PRELOAD_PENEMUNXT_MAP_PATH.length() > 0) {
			disableTimeline();
			MapFileUtilities.openData(PRELOAD_PENEMUNXT_MAP_PATH, DS, this,
					DEFAULT_FOLDER_PATH);
		}

		while (AppActive) {
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

	private void connectRobot() {
		if (RC != null) {
			RC.disconnect();
		}
		RC = new RobotConnection(NXTC, DS, CONNECTION_MODES[cboConnectionTypes
				.getSelectedIndex()], txtConnectToName.getText(),
				txtConnectToAddress.getText());
		RC.start();
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnExit || ae.getSource() == mnuFileExitButton) {
			exitApp();
		} else if (ae.getSource() == btnConnectAndStart) {
			connectAndStart();
		} else if (ae.getSource() == btnDisconnectAndStop) {
			disconnectAndStop();
		} else if (ae.getSource() == mnuFileOpenButton) {
			MapFileUtilities.openData(null, DS, this, DEFAULT_FOLDER_PATH);
		} else if (ae.getSource() == mnuFileSaveButton) {
			MapFileUtilities.saveData(null, DS, this, DEFAULT_FOLDER_PATH);
		} else if (ae.getSource() == mnuFileOpenDataViewButton) {
			openDataView();
		} else if (ae.getSource() == mnuFileOpenMapProcessorsButton) {
			openMapProcessorsListView();
		} else if (ae.getSource() == btnTimelinePlayPause) {
			switchTimelineAutoPlay();
		} else if (ae.getSource() == btnTimelineEnableDisable) {
			switchTimelineEnabled();
		} else if (ae.getSource() == btnTimelineRewind) {
			rewindTimeline();
		} else if (ae.getSource() == mnuMapClearButton) {
			clearMap();
		}
	}

	@Override
	public void stateChanged(ChangeEvent ce) {
		if (ce.getSource() == sldMapScale) {
			curScale = sldMapScale.getValue();
			refreshMap();
		} else if (ce.getSource() == sldTimelineSpeed) {
			timelinePlaySpeed = sldTimelineSpeed.getValue();
			if (TimelinePlayer != null) {
				TimelinePlayer
						.setDelay(getTimelineDelayFromSpeed(timelinePlaySpeed));
			}
		} else if (ce.getSource() == sldTimeline) {
			timelineChanged();
		} else if (ce.getSource() == DataView) {
			if (DataView != null && timelineEnabled
					&& DataView.getSelectedFrame() >= 0) {
				sldTimeline.setValue(DataView.getSelectedFrame());
			}
		} else if (ce.getSource() == MapProcessorsListView) {
			refreshMap();
			setupMapProcessorsMenu();
		}
	}

	private void openDataView() {
		if (DataView != null
				&& DataView.getWindowState() == DataTableWindow.WINDOW_STATE_OPEN) {
			DataView.refresh(sldTimeline.getValue(), true);
		} else {
			DataView = new PenemuNXTExplorerDataViewer(DS.NXTRobotData,
					APPLICATION_NAME + " - Data view", APPLICATION_ICON
							.getImage());
			DataView.setSelectedFrameChanged(this);
			DataView.open();
		}
	}

	private void openMapProcessorsListView() {
		if (MapProcessorsListView != null
				&& MapProcessorsListView.getWindowState() == DataTableWindow.WINDOW_STATE_OPEN) {
			MapProcessorsListView.refresh(true);
		} else {
			MapProcessorsListView = new MapProcessorsList(mapProcessors,
					APPLICATION_NAME + " - Map Processors", APPLICATION_ICON
							.getImage());
			MapProcessorsListView.setDataChanged(this);
			MapProcessorsListView.open();
		}
	}

	private void exitApp() {
		AppActive = false;
		btnExit.setEnabled(false);
	}

	private void clearMap() {
		if (DS != null) {
			DS.NXTRobotData.clear();
		}

		refreshLatestData();
		refreshMap();
	}

	private void disconnectAndStop() {
		if (RC != null) {
			RC.disconnect();
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

		connectRobot();
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
		btnTimelineRewind.setEnabled(true);

		sldTimelineSpeed.setEnabled(true);
		sldTimeline.setEnabled(true);
		sldTimeline.setVisible(true);
	}

	private void disableTimeline() {
		timelineEnabled = false;
		btnTimelineEnableDisable.setLabel("Enable");
		btnTimelinePlayPause.setEnabled(false);
		btnTimelineRewind.setEnabled(false);

		sldTimelineSpeed.setEnabled(false);
		sldTimeline.setEnabled(false);
		sldTimeline.setVisible(false);
	}

	private void rewindTimeline() {
		sldTimeline.setValue(sldTimeline.getMinimum());
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

		if (sldTimeline.getValue() == sldTimeline.getMaximum()) {
			sldTimeline.setValue(sldTimeline.getMinimum());
		}

		timelinePlay = true;
		btnTimelinePlayPause.setLabel("Pause");
		TimelinePlayer = new MapTimelinePlayer(sldTimeline,
				getTimelineDelayFromSpeed(timelinePlaySpeed));
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

	private void timelineChanged() {
		refreshMap();
		refreshLatestData();

		if (DataView != null
				&& DataView.getWindowState() == DataTableWindow.WINDOW_STATE_OPEN) {
			DataView.selectFrame(sldTimeline.getValue());
			DataView.focus();
		}
	}

	private int getTimelineDelayFromSpeed(int speed) {
		return ((TIMELINE_PLAY_SPEED_MAX - speed + TIMELINE_PLAY_SPEED_MIN) * TIMELINE_PLAY_SPEED_MULTIPLIER);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		curScale += -e.getWheelRotation() * 2;
		curScale = Math.max(curScale, MAP_MIN_SCALE);
		curScale = Math.min(curScale, MAP_MAX_SCALE);

		refreshMap();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (mapCenter != null) {
			mapCenterBeforeDrag = (Point) mapCenter.clone();
			startDrag = new Point(e.getX(), e.getY());
		}

		refreshMap();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			curScale = MAP_DEFAULT_SCALE;
			mapCenter = new Point((mapPanel.getWidth() / 2), (mapPanel
					.getHeight() / 2));
		}

		refreshMap();
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

		refreshMap();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if (RC != null && RC.isConnectionActive()) {
			disconnectAndStop();
			exitApp();
		} else {
			System.exit(0);
		}
	}

	public void windowActivated(WindowEvent arg0) {
		refreshMap();
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		refreshMap();
	}
	
	public void windowOpened(WindowEvent arg0) {
		refreshMap();
	}
	
	public void windowClosed(WindowEvent arg0) {
	}

	public void windowDeactivated(WindowEvent arg0) {
	}

	public void windowDeiconified(WindowEvent arg0) {
	}

	public void windowIconified(WindowEvent arg0) {
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

	@Override
	public void componentHidden(ComponentEvent arg0) {		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {		
	}
}