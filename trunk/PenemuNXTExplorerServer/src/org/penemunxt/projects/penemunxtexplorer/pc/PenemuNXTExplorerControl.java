package org.penemunxt.projects.penemunxtexplorer.pc;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.VolatileImage;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import org.penemunxt.communication.*;
import org.penemunxt.graphics.pc.Icons;
import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.connection.*;
import org.penemunxt.projects.penemunxtexplorer.pc.map.*;
import org.penemunxt.projects.penemunxtexplorer.pc.map.file.MapFileUtilities;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.*;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors.*;
import org.penemunxt.windows.pc.ComponentSpacer;
import org.penemunxt.windows.pc.DataTableWindow;

public class PenemuNXTExplorerControl extends JPanel implements Runnable,
		ActionListener, WindowListener, ComponentListener, ChangeListener,
		MouseListener, MouseMotionListener {

	// Constants

	// // Application
	final static String APPLICATION_NAME = "PenemuNXT - Explorer control";
	final static ImageIcon APPLICATION_ICON = Icons.PENEMUNXT_CIRCLE_LOGO_ICON_16_X_16_ICON;
	final static ImageIcon APPLICATION_LOGO = Icons.PENEMUNXT_LOGO_LANDSCAPE_ICON;
	final static Boolean APPLICATION_START_FULLSCREEN = false;

	// // Maps

	// None
	// final static String DEFAULT_FOLDER_PATH = "";
	// final static String PRELOAD_PENEMUNXT_MAP_PATH = "";

	// PeterF-01
	final static String DEFAULT_FOLDER_PATH = "C:\\Users\\Peter\\Desktop\\PMaps\\";
	final static String PRELOAD_PENEMUNXT_MAP_PATH = "C:\\Users\\Peter\\Desktop\\PMaps\\Berzan\\8.penemunxtmap";

	// PeterF-04
	// final static String DEFAULT_FOLDER_PATH =
	// "C:\\Documents and Settings\\Peter\\Mina dokument\\Projects\\PenemuNXT\\Data\\Maps\\";
	// final static String PRELOAD_PENEMUNXT_MAP_PATH =
	// "C:\\Documents and Settings\\Peter\\Mina dokument\\Projects\\PenemuNXT\\Data\\Maps\\NXT5.penemunxtmap";

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

	final static int DEFAULT_CIRCLE_SIZE = 10;
	final static int LATEST_POS_CIRCLE_SIZE = 75;
	final static int DRIVING_PATH_CIRCLE_SIZE = 10;
	final static int BUMPING_BUMPER_CIRCLE_SIZE = 50;
	final static int BUMPING_DISTANCE_CIRCLE_SIZE = 50;
	final static int ALIGNED_TO_WALL_CIRCLE_SIZE = 50;
	final static int HEAD_MAP_CIRCLE_SIZE = 25;
	final static int HOT_SPOTS_MAX_CIRCLE_SIZE = 100;

	// //// Scale
	final static int MAP_INIT_SCALE = 35;

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
	Button btnConnectAndStart;
	Button btnDisconnectAndStop;
	Button btnTimelineEnableDisable;
	Button btnTimelinePlayPause;
	Button btnTimelineRewind;

	// // Panels
	Panel controlPanel;

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
	MapVisulaisation mapVisulaisation;
	MapVisulaisation mapVisulaisationThumbnail;
	MapThumbnail mapThumbnail;

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

	public PenemuNXTExplorerControl() {
		AppActive = true;
		this.setLayout(new BorderLayout());
		this.add(getContentPanel(), BorderLayout.CENTER);

		mapThumbnail = new MapThumbnail();

		Thread t = new Thread(this);
		t.start();
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

		setupMapProcessorsMenu();

		return mnuMainBar;
	}

	public Panel getContentPanel() {
		// Panels
		Panel mainPanel = new Panel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		JPanel viewPanel = new JPanel();

		controlPanel = new Panel();
		Panel timelinePanel = new Panel();
		Panel timelineControlPanel = new Panel();

		// Fonts
		Font fntSectionHeader = new Font("Arial", Font.BOLD, 14);
		Font fntLabelHeader = new Font("Arial", Font.BOLD, 12);

		// Map
		setupMapProcessors();
		mapVisulaisation = new MapVisulaisation(MAP_INIT_SCALE, mapProcessors,
				true);
		mapVisulaisation.mapCenterChanged = this;
		mapVisulaisation.mapScaleChanged = this;

		mapVisulaisationThumbnail = new MapVisulaisation(MAP_INIT_SCALE,
				mapProcessors, false);

		// Image logo
		JPanel logoPanel = new JPanel(new BorderLayout());
		JLabel lblLogo = new JLabel("", APPLICATION_LOGO, SwingConstants.CENTER);

		logoPanel.setBackground(LEFT_PANEL_BACKGROUND_COLOR);
		logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0,
				LOGO_MARGIN_BOTTOM, 0));
		logoPanel.add(lblLogo, BorderLayout.CENTER);

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

		sldMapScale = new JSlider(SwingConstants.HORIZONTAL,
				MapVisulaisation.MAP_MIN_SCALE, MapVisulaisation.MAP_MAX_SCALE,
				MAP_INIT_SCALE);
		sldMapScale.setMajorTickSpacing(10);
		sldMapScale.setMinorTickSpacing(5);
		sldMapScale.setPaintTicks(true);
		sldMapScale.setPaintLabels(true);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(MapVisulaisation.MAP_MIN_SCALE), new JLabel(
				MapVisulaisation.MAP_MIN_SCALE + "%"));
		labelTable.put(new Integer(25), new JLabel("25%"));
		labelTable.put(new Integer(50), new JLabel("50%"));
		labelTable.put(new Integer(75), new JLabel("75%"));
		labelTable.put(new Integer(MapVisulaisation.MAP_MAX_SCALE), new JLabel(
				MapVisulaisation.MAP_MAX_SCALE + "%"));
		sldMapScale.setLabelTable(labelTable);
		sldMapScale.setBackground(LEFT_PANEL_BACKGROUND_COLOR);
		sldMapScale.addChangeListener(this);

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

		sldTimeline = new JSlider(SwingConstants.HORIZONTAL, timelineMin,
				timelineMax, timelineDefault);
		sldTimeline.setPaintTicks(true);
		sldTimeline.setPaintLabels(true);
		sldTimeline.addChangeListener(this);
		sldTimeline.setBackground(BOTTOM_PANEL_BACKGROUND_COLOR);

		sldTimeline.addMouseListener(this);
		sldTimeline.addMouseMotionListener(this);

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
		controlPanel.add(logoPanel);

		controlPanel.add(lblConnectionHeader);
		controlPanel.add(pnlConnection);

		controlPanel.add(lblMapScalesHeader);
		controlPanel.add(sldMapScale);

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
		viewPanel.add(new ComponentSpacer(mapVisulaisation, PANEL_MARGIN,
				VIEW_PANEL_BACKGROUND_COLOR, MAP_PANEL_BORDER_WIDTH,
				MAP_PANEL_BORDER_COLOR, MAP_PANEL_BACKGROUND_COLOR),
				BorderLayout.CENTER);

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
					menuItem.setForeground(mapProcessor.getColor());
					menuItem.setToolTipText(mapProcessor.getDescription());

					menuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							mapProcessor.setEnabled(menuItem.getState());
							if (MapProcessorsListView != null) {
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
		if (mapVisulaisation != null) {
			mapVisulaisation.refresh();
			mapVisulaisationThumbnail.refresh();
		}
	}

	@Override
	public void run() {
		// Object to share data internal
		DS = new DataShare();

		mapVisulaisation.setDS(DS);
		mapVisulaisationThumbnail.setDS(DS);

		disableTimeline();

		// Auto open
		if (PRELOAD_PENEMUNXT_MAP_PATH.length() > 0) {
			MapFileUtilities.openData(PRELOAD_PENEMUNXT_MAP_PATH, DS);
		}

		while (AppActive) {
			if (mapVisulaisation != null) {
				sldMapScale.setValue(mapVisulaisation.getMapScale());
			}

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

				if (mapVisulaisation != null) {
					mapVisulaisation.setMapCurrentFrame(sldTimeline.getValue());
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
		if (ae.getSource() == mnuFileExitButton) {
			exitApp();
		} else if (ae.getSource() == btnConnectAndStart) {
			connectAndStart();
		} else if (ae.getSource() == btnDisconnectAndStop) {
			disconnectAndStop();
		} else if (ae.getSource() == mnuFileOpenButton) {
			MapFileUtilities.openData(DEFAULT_FOLDER_PATH, this, DS,
					mapProcessors);
		} else if (ae.getSource() == mnuFileSaveButton) {
			MapFileUtilities.saveData(DEFAULT_FOLDER_PATH, this, DS);
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
			if (mapVisulaisation != null) {
				mapVisulaisation.setMapScale(sldMapScale.getValue(), false);
			}
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
		} else if (ce.getSource() == mapVisulaisation) {
			refreshMap();
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
			MapProcessorsListView = new MapProcessorsList(mapProcessors, DS,
					APPLICATION_NAME + " - Map Processors", APPLICATION_ICON
							.getImage());
			MapProcessorsListView.setDataChanged(this);
			MapProcessorsListView.open();
		}
	}

	private void exitApp() {
		AppActive = false;
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

		btnConnectAndStart.setEnabled(true);
		btnDisconnectAndStop.setEnabled(false);
		cboConnectionTypes.setEnabled(true);
		txtConnectToName.setEnabled(true);
		txtConnectToAddress.setEnabled(true);
	}

	private void connectAndStart() {
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

	@Override
	public void mouseExited(MouseEvent arg0) {
		mapThumbnail.hide();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		refreshMap();
		if (!mapThumbnail.isOpen()) {
			mapThumbnail.open(mapVisulaisationThumbnail, 250, 250);
			mapThumbnail.setScale(25);
		}

		int x = e.getXOnScreen();
		int y = e.getYOnScreen();

		float percentage = (e.getX() / (float) sldTimeline.getWidth());
		int frame = (int) (percentage * sldTimeline.getMaximum());

		x -= (mapThumbnail.getWidth() / 2);

		int finalX;
		finalX = Math.min(x,
				((int) sldTimeline.getLocationOnScreen().getX()
						+ sldTimeline.getWidth() - mapVisulaisationThumbnail
						.getWidth()));
		finalX = Math.max(finalX, (int) sldTimeline.getLocationOnScreen()
				.getX());
		int finalY = (int) sldTimeline.getLocationOnScreen().getY()
				- mapThumbnail.mainWindow.getHeight() - 100;

		mapThumbnail.setFrame(frame);
		mapThumbnail.move(finalX, finalY);
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
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
}