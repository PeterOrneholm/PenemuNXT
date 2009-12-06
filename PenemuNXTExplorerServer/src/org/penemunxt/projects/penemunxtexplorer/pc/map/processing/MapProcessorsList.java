package org.penemunxt.projects.penemunxtexplorer.pc.map.processing;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.*;

import org.penemunt.windows.pc.DataTableWindow;

public class MapProcessorsList extends DataTableWindow {

	MapProcessors mapProcessors;

	public MapProcessorsList(MapProcessors mapProcessors,
			String applicationName, Image applicationIcon) {
		super(applicationName, applicationIcon);
		this.mapProcessors = mapProcessors;
		refresh();
	}

	private Object[][] getDataArray(ArrayList<IMapProcessor> rawData) {
		if (rawData.size() > 0) {
			Object[][] tableData = new Object[rawData.size()][6];

			for (int i = 0; i < rawData.size(); i++) {
				IMapProcessor mapProcessor = rawData.get(i);
				tableData[i][0] = String.valueOf(i + 1); // No.
				tableData[i][1] = String.valueOf(mapProcessor.isEnabled()); // Enabled
				tableData[i][2] = mapProcessor.getName(); // Name
				tableData[i][3] = mapProcessor.getDescription(); // Description
				if (mapProcessor.getColor() != null) {
					tableData[i][4] = mapProcessor.getColor().toString(); // Color
				} else {
					tableData[i][4] = "";
				}
				tableData[i][5] = String.valueOf(mapProcessor.getSize()); // Size
			}
			return tableData;
		} else {
			return null;
		}
	}

	private void setupTable(Object[][] tableData) {
		String[] columnNames = { "Layer", "Enabled", "Name", "Description",
				"Color", "Size" };

		if (tableData == null) {
			tableData = new Object[0][6];
		}

		setDataTable(new JTable(tableData, columnNames));
		getDataTable().setFillsViewportHeight(true);
		getDataTable().setCellSelectionEnabled(false);
		getDataTable().setColumnSelectionAllowed(false);
		getDataTable().setRowSelectionAllowed(true);
		getDataTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public void refresh() {
		refresh(false);
	}
	
	@Override
	public void refresh(boolean focus) {
		super.refresh(focus);
		setupTable(getDataArray(mapProcessors.getList()));
	}
}