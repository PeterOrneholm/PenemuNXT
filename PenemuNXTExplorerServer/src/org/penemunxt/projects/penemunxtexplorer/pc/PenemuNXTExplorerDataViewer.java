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

public class PenemuNXTExplorerDataViewer extends Thread implements Runnable {

	ArrayList<RobotData> RobotData;
	String ApplicationName;
	JTable DataTable;

	@Override
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	public PenemuNXTExplorerDataViewer(ArrayList<RobotData> robotData,
			String applicationName) {
		this.RobotData = robotData;
		this.ApplicationName = applicationName;
		refresh();
	}

	private void createAndShowGUI() {
		JFrame mainFrame = new JFrame(ApplicationName);
		mainFrame.setContentPane(getContentPanel());
		mainFrame.setIconImage(Icons.PENEMUNXT_CIRCLE_LOGO_ICON_16_X_16_ICON
				.getImage());

		Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setSize((int) (ScreenSize.width * 0.50),
				(int) (ScreenSize.height * 0.25));

		mainFrame.setBackground(Color.WHITE);
		mainFrame.setVisible(true);
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
			Object[][] tableData = new Object[rawData.size()][6];

			for (int i = 0; i < rawData.size(); i++) {
				RobotData robotData = rawData.get(i);
				tableData[i][0] = String.valueOf(robotData.getType());
				tableData[i][1] = String.valueOf(robotData.getPosX());
				tableData[i][2] = String.valueOf(robotData.getPosY());
				tableData[i][3] = String.valueOf(robotData.getRobotHeading());
				tableData[i][4] = String.valueOf(robotData.getHeadDistance());
				tableData[i][5] = String.valueOf(robotData.getHeadHeading());
			}
			return tableData;
		} else {
			return null;
		}
	}

	private void setupTable(Object[][] tableData) {
		String[] columnNames = { "Type", "PosX", "PosY", "RobotHeading",
				"HeadDistance", "HeadHeading" };
		
		if(tableData==null){
			tableData = new Object[0][5];
		}
		
		DataTable = new JTable(tableData, columnNames);
		DataTable.setFillsViewportHeight(true);
		DataTable.setCellSelectionEnabled(false);
		DataTable.setColumnSelectionAllowed(false);
		DataTable.setRowSelectionAllowed(true);
	}

	public void refresh() {
		setupTable(getDataArray(RobotData));
	}
}