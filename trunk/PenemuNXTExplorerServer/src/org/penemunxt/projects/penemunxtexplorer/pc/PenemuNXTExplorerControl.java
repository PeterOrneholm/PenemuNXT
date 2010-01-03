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
import org.penemunxt.projects.penemunxtexplorer.pc.map.timeline.MapTimeline;
import org.penemunxt.windows.pc.ComponentSpacer;
import org.penemunxt.windows.pc.DataTableWindow;

public class PenemuNXTExplorerControl extends JPanel implements Runnable,
		ActionListener, WindowListener, ComponentListener, ChangeListener
		{

	// Constants
	private static final long serialVersionUID = 1L;

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
	final static String DEFAULT_FOLDER_PATH = "C:\\Users\\Peter\\Desktop\\Maps\\";
	final static String PRELOAD_PENEMUNXT_MAP_PATH = "C:\\Users\\Peter\\Desktop\\Maps\\Sample_4.penemunxtmap";

	// PeterF-04
	// final static String DEFAULT_FOLDER_PATH =
	// "C:\\Documents and Settings\\Peter\\Mina dokument\\Projects\\PenemuNXT\\Data\\Maps\\";
	// final static String PRELOAD_PENEMUNXT_MAP_PATH =
	// "C:\\Documents and Settings\\Peter\\Mina dokument\\Projects\\PenemuNXT\\Data\\Maps\\NXT5.penemunxtmap";

	// //Events and Algorithms basics

	// //// Scale
	final static int MAP_INIT_SCALE = 50;

	// // UI
	final static Color DEFAULT_PANEL_BACKGROUND_COLOR = new Color(197, 209, 215);
	final static Color VIEW_PANEL_BACKGROUND_COLOR = DEFAULT_PANEL_BACKGROUND_COLOR;
	final static Color LEFT_PANEL_BACKGROUND_COLOR = DEFAULT_PANEL_BACKGROUND_COLOR;
	final static Color BOTTOM_PANEL_BACKGROUND_COLOR = DEFAULT_PANEL_BACKGROUND_COLOR;

	final static Color MAP_PANEL_BACKGROUND_COLOR = Color.GRAY;
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

	// Variables

	// // Menu
	JMenuBar mnuMainBar;
	
	JMenuItem mnuFileOpenDataViewButton;
	JMenuItem mnuFileOpenMapProcessorsButton;
	JMenuItem mnuFileOpenButton;
	JMenuItem mnuFileSaveButton;
	JMenuItem mnuFileExportMapAsImageButton;
	JMenuItem mnuFileExitButton;

	// // Map menu
	JMenuItem mnuMapClearButton;
	JMenu mnuMapProcessors;

	// // Buttons
	JButton btnConnectAndStart;
	JButton btnDisconnectAndStop;

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
	JSlider sldMapRotate;
	JSlider sldAlgorithmsSensitivityFilter;

	// // Textboxes
	JTextField txtConnectToName;
	JTextField txtConnectToAddress;

	// // Map
	MapVisulaisation mapVisulaisation;
	

	// // Processors
	MapProcessors mapProcessors;

	// // Misc
	boolean AppActive;
	NXTCommunication NXTC;
	DataShare DS;
	RobotConnection RC;
	VolatileImage OSI;
	PenemuNXTExplorerDataViewer DataView;
	MapProcessorsList MapProcessorsListView;
	MapTimeline mapTimeline;

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

		//Timeline
		mapTimeline = new MapTimeline();
		mapTimeline.addFrameChangeListeners(this);
		mapTimeline.setBgColor(BOTTOM_PANEL_BACKGROUND_COLOR);

		this.setLayout(new BorderLayout());
		this.add(getContentPanel(), BorderLayout.CENTER);
		
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
		mnuFileOpenButton = new JMenuItem("Open Map...");
		mnuFileOpenButton.addActionListener(this);
		mnuFileSaveButton = new JMenuItem("Save map As...");
		mnuFileSaveButton.addActionListener(this);
		mnuFileExportMapAsImageButton = new JMenuItem("Export rendered map...");
		mnuFileExportMapAsImageButton.addActionListener(this);		
		mnuFileExitButton = new JMenuItem("Exit");
		mnuFileExitButton.addActionListener(this);

		// Add everything
		mnuMainBar.add(mnuFileMenu);
		mnuMainBar.add(getMapMenu());

		mnuFileMenu.add(mnuFileOpenButton);
		mnuFileMenu.add(mnuFileSaveButton);
		mnuFileMenu.add(mnuFileExportMapAsImageButton);
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

		// Fonts
		Font fntSectionHeader = new Font("Arial", Font.BOLD, 14);
		Font fntLabelHeader = new Font("Arial", Font.BOLD, 12);

		// Map
		setupMapProcessors();
		mapVisulaisation = new MapVisulaisation(MAP_INIT_SCALE, mapProcessors,
				true);
		mapVisulaisation.mapCenterChanged = this;
		mapVisulaisation.mapScaleChanged = this;

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

		txtConnectToName = new JTextField(CONNECT_TO_NAME_DEFAULT, 15);
		txtConnectToAddress = new JTextField(CONNECT_TO_ADDRESS_DEFAULT, 15);

		btnConnectAndStart = new JButton("Connect and Start");
		btnConnectAndStart.addActionListener(this);
		btnConnectAndStart.setEnabled(true);

		btnDisconnectAndStop = new JButton("Disconnect and Stop");
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

		Hashtable<Integer, JLabel> mapScaleLabelTable = new Hashtable<Integer, JLabel>();
		mapScaleLabelTable.put(new Integer(MapVisulaisation.MAP_MIN_SCALE), new JLabel(
				MapVisulaisation.MAP_MIN_SCALE + "%"));
		
		mapScaleLabelTable.put(new Integer((int) (MapVisulaisation.MAP_MAX_SCALE - MapVisulaisation.MAP_MAX_SCALE / 2)), new JLabel(
				(int) (MapVisulaisation.MAP_MAX_SCALE - MapVisulaisation.MAP_MAX_SCALE / 2) + "%"));
		
		
		mapScaleLabelTable.put(new Integer(MapVisulaisation.MAP_MAX_SCALE), new JLabel(
				MapVisulaisation.MAP_MAX_SCALE + "%"));
		sldMapScale.setLabelTable(mapScaleLabelTable);
		sldMapScale.setBackground(LEFT_PANEL_BACKGROUND_COLOR);
		sldMapScale.addChangeListener(this);

		// Map rotate
		Label lblMapRotateHeader = new Label("Map orientation");
		lblMapRotateHeader.setFont(fntSectionHeader);

		sldMapRotate = new JSlider(SwingConstants.HORIZONTAL, 0, 360,
				0);
		sldMapRotate.setMajorTickSpacing(36);
		sldMapRotate.setMinorTickSpacing(18);
		sldMapRotate.setPaintTicks(true);
		sldMapRotate.setPaintLabels(true);

		Hashtable<Integer, JLabel> mapRotateLabelTable = new Hashtable<Integer, JLabel>();
		mapRotateLabelTable.put(new Integer(0), new JLabel("0°"));
		mapRotateLabelTable.put(new Integer(180), new JLabel("180°"));
		mapRotateLabelTable.put(new Integer(360), new JLabel("360°"));

		sldMapRotate.setLabelTable(mapRotateLabelTable);
		sldMapRotate.setBackground(LEFT_PANEL_BACKGROUND_COLOR);
		sldMapRotate.addChangeListener(this);

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

		// Control panel
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.add(logoPanel);

		controlPanel.add(lblConnectionHeader);
		controlPanel.add(pnlConnection);

		controlPanel.add(lblMapScalesHeader);
		controlPanel.add(sldMapScale);

		controlPanel.add(lblMapRotateHeader);
		controlPanel.add(sldMapRotate);

		controlPanel.add(lblCurrentDataHeader);
		controlPanel.add(pnlCurrentData);

		// Bottom panel
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, PANEL_MARGIN,
				PANEL_MARGIN, PANEL_MARGIN));
		bottomPanel.setBackground(BOTTOM_PANEL_BACKGROUND_COLOR);
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(mapTimeline, BorderLayout.CENTER);

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

		//Menu
		mnuMainBar = getMenuBar();
		
		// Main panel
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(mnuMainBar, BorderLayout.NORTH);
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
		mapProcessors = new MapProcessors(PenemuNXTDefaultMapProcessors.getDefaultProcessors());
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
					mapTimeline.getCurrentFrame() - 1);
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
			lblTimelineCurrentFrame.setText(String.valueOf(mapTimeline
					.getCurrentFrame()));
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
			mapVisulaisation.setMapCurrentFrame(mapTimeline.getCurrentFrame());
			mapVisulaisation.refresh();
		}
	}

	@Override
	public void run() {
		// Object to share data internal
		DS = new DataShare();

		mapVisulaisation.setDS(DS);
		mapTimeline.setDS(DS);
		mapTimeline.setEnabled(false);
		mapTimeline.setMapThumbnail(MAP_INIT_SCALE, mapProcessors, DS);

		// Auto open
		if (PRELOAD_PENEMUNXT_MAP_PATH.length() > 0) {
			MapFileUtilities.openData(PRELOAD_PENEMUNXT_MAP_PATH, DS);
		}

		while (AppActive) {
			if (mapVisulaisation != null) {
				sldMapScale.setValue(mapVisulaisation.getMapScale());
			}
			
			mapTimeline.refresh();

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
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
		} else if (ae.getSource() == mnuFileExportMapAsImageButton) {
			if(mapVisulaisation!=null){
				MapFileUtilities.saveRenderedMap(DEFAULT_FOLDER_PATH, this, mapVisulaisation);
			}
		} else if (ae.getSource() == mnuFileOpenDataViewButton) {
			openDataView();
		} else if (ae.getSource() == mnuFileOpenMapProcessorsButton) {
			openMapProcessorsListView();
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
		} else if (ce.getSource() == sldMapRotate) {
			if (mapVisulaisation != null) {
				mapVisulaisation.setMapRotate(Math
						.toRadians((double) sldMapRotate.getValue()));
			}
			refreshMap();
		} else if (ce.getSource() == MapProcessorsListView) {
			refreshMap();
			setupMapProcessorsMenu();
		} else if (ce.getSource() == mapVisulaisation) {
			refreshMap();
		} else if (ce.getSource() == DataView) {
			if (DataView != null && mapTimeline.isEnabled()
					&& DataView.getSelectedFrame() >= 0) {
				mapTimeline.setCurrentFrame(DataView.getSelectedFrame());
			}
		} else if (ce.getSource() == mapTimeline) {
			timelineChanged();
		}
	}

	private void timelineChanged() {
		refreshMap();
		refreshLatestData();

		if (DataView != null
				&& DataView.getWindowState() == DataTableWindow.WINDOW_STATE_OPEN) {
			DataView.selectFrame(mapTimeline.getCurrentFrame());
			DataView.focus();
		}
	}
	
	private void openDataView() {
		if (DataView != null
				&& DataView.getWindowState() == DataTableWindow.WINDOW_STATE_OPEN) {
			DataView.refresh(mapTimeline.getCurrentFrame(), true);
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
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}

}