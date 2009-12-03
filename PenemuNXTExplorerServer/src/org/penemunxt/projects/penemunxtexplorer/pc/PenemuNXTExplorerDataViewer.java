package org.penemunxt.projects.penemunxtexplorer.pc;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.beans.*;
import java.io.*;
import java.net.URL;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.penemunxt.communication.*;
import org.penemunxt.graphics.pc.*;
import org.penemunxt.projects.penemunxtexplorer.*;
import org.penemunxt.projects.penemunxtexplorer.pc.connection.*;
import org.penemunxt.projects.penemunxtexplorer.pc.maps.*;

public class PenemuNXTExplorerDataViewer extends Thread implements Runnable,
		WindowListener {

	public final static String POSITION_TYPE_DRIVE_NAME = "Drive path";
	public final static String POSITION_TYPE_BUMP_BUMPER_NAME = "Bumber (Front bumper)";
	public final static String POSITION_TYPE_BUMP_DISTANCE_NAME = "Bumper (Distance)";
	public final static String POSITION_TYPE_ALIGNED_NAME = "Aligned to wall";
	public final static String POSITION_TYPE_RIGHT_CORNER_NAME = "Turn in right corner";
	
	public static String getPositionTypeName(int PositionType) {
		switch (PositionType) {
		case RobotData.POSITION_TYPE_DRIVE:
			return POSITION_TYPE_DRIVE_NAME;
		case RobotData.POSITION_TYPE_BUMP_BUMPER:
			return POSITION_TYPE_BUMP_BUMPER_NAME;
		case RobotData.POSITION_TYPE_BUMP_DISTANCE:
			return POSITION_TYPE_BUMP_DISTANCE_NAME;
		case RobotData.POSITION_TYPE_ALIGNED:
			return POSITION_TYPE_ALIGNED_NAME;
		case RobotData.POSITION_TYPE_RIGHT_CORNER:
			return POSITION_TYPE_RIGHT_CORNER_NAME;
		default:
			return "Unknown type";
		}
	}
	
	public static int WINDOW_STATE_OPEN = 100;
	public static int WINDOW_STATE_CLOSED = 200;

	ArrayList<RobotData> TableData;
	String ApplicationName;
	JTable DataTable;
	JFrame mainFrame;
	int WindowState;

	public int getWindowState() {
		return WindowState;
	}

	public void setWindowState(int windowState) {
		WindowState = windowState;
	}

	@Override
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	public PenemuNXTExplorerDataViewer(ArrayList<RobotData> robotData,
			String applicationName) {
		this.TableData = robotData;
		this.ApplicationName = applicationName;
		this.WindowState = PenemuNXTExplorerDataViewer.WINDOW_STATE_OPEN;
		refresh();
	}

	private void createAndShowGUI() {
		mainFrame = new JFrame(ApplicationName);
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setContentPane(getContentPanel());
		mainFrame.setIconImage(Icons.PENEMUNXT_CIRCLE_LOGO_ICON_16_X_16_ICON
				.getImage());

		Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setSize((int) (ScreenSize.width * 0.50),
				(int) (ScreenSize.height * 0.25));

		mainFrame.setBackground(Color.WHITE);
		mainFrame.setVisible(true);
		mainFrame.addWindowListener(this);
	}

	public Panel getContentPanel() {
		// Panels
		Panel panelMain = new Panel();
		JScrollPane scrollTable = new JScrollPane(DataTable);

		// Main
		panelMain.setLayout(new BorderLayout());
		panelMain.add(scrollTable, BorderLayout.CENTER);

		return panelMain;
	}

	public void open() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private Object[][] getDataArray(ArrayList<RobotData> rawData) {
		if (rawData.size() > 0) {
			Object[][] tableData = new Object[rawData.size()][8];

			for (int i = 0; i < rawData.size(); i++) {
				RobotData robotData = rawData.get(i);
				tableData[i][0] = String.valueOf(i + 1);
				tableData[i][1] = getPositionTypeName(robotData.getType());
				tableData[i][2] = String.valueOf(robotData.getType());
				tableData[i][3] = String.valueOf(robotData.getPosX());
				tableData[i][4] = String.valueOf(robotData.getPosY());
				tableData[i][5] = String.valueOf(robotData.getRobotHeading());
				tableData[i][6] = String.valueOf(robotData.getHeadDistance());
				tableData[i][7] = String.valueOf(robotData.getHeadHeading());
			}
			return tableData;
		} else {
			return null;
		}
	}

	private void setupTable(Object[][] tableData) {
		String[] columnNames = { "Frame", "Type", "Type code", "PosX", "PosY",
				"RobotHeading", "HeadDistance", "HeadHeading" };

		if (tableData == null) {
			tableData = new Object[0][5];
		}

		DataTable = new JTable(tableData, columnNames);
		DataTable.setFillsViewportHeight(true);
		DataTable.setCellSelectionEnabled(false);
		DataTable.setColumnSelectionAllowed(false);
		DataTable.setRowSelectionAllowed(true);
	}

	public void refresh() {
		setupTable(getDataArray(TableData));
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		setWindowState(PenemuNXTExplorerDataViewer.WINDOW_STATE_CLOSED);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

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
}