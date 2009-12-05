package org.penemunxt.projects.penemunxtexplorer.pc;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import org.penemunt.windows.pc.DataTableWindow;
import org.penemunt.windows.pc.WindowUtilities;
import org.penemunxt.projects.penemunxtexplorer.*;

public class PenemuNXTExplorerDataViewer extends DataTableWindow {

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

	ArrayList<RobotData> TableData;

	public PenemuNXTExplorerDataViewer(ArrayList<RobotData> robotData,
			String applicationName, Image applicationIcon) {
		super(applicationName, applicationIcon);
		this.TableData = robotData;
		refresh();
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

		setDataTable(new JTable(tableData, columnNames));
		getDataTable().setFillsViewportHeight(true);
		getDataTable().setCellSelectionEnabled(false);
		getDataTable().setColumnSelectionAllowed(false);
		getDataTable().setRowSelectionAllowed(true);
	}

	public void selectFrame(int frame) {
		if (getDataTable() != null) {
			ListSelectionModel selectionModel = getDataTable()
					.getSelectionModel();
			WindowUtilities.scrollToVisible(getDataTable(), frame - 1, 0);
			selectionModel.setSelectionInterval(frame - 1, frame - 1);
		}
	}

	public void refresh() {
		refresh(false);
	}

	@Override
	public void refresh(boolean focus) {
		refresh(-1, false);
	}

	public void refresh(int frame, boolean focus) {
		super.refresh(focus);
		setupTable(getDataArray(TableData));
		if (frame >= 0) {
			selectFrame(frame);
		}
	}
}