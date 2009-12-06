package org.penemunxt.projects.penemunxtexplorer.pc.map.processing;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.penemunt.windows.pc.*;
import org.penemunt.windows.tables.pc.ColorEditor;
import org.penemunt.windows.tables.pc.ColorRenderer;
import org.penemunt.windows.tables.pc.SliderEditor;

public class MapProcessorsList extends DataTableWindow {

	MapProcessors mapProcessors;
	private ChangeListener dataChanged;

	public ChangeListener getDataChanged() {
		return dataChanged;
	}

	public void setDataChanged(ChangeListener dataChanged) {
		this.dataChanged = dataChanged;
	}

	public MapProcessorsList(MapProcessors mapProcessors,
			String applicationName, Image applicationIcon) {
		super(applicationName, applicationIcon);
		this.mapProcessors = mapProcessors;
		refresh();
	}

	private void setupTable() {
		setDataTable(new JTable(new MapProcessorsTableModel(mapProcessors
				.getList(), this)));

		getDataTable().setAutoCreateRowSorter(true);

		TableColumn colSize = getDataTable().getColumnModel().getColumn(6);
		colSize.setCellEditor(new SliderEditor(1, 40));

		getDataTable().setDefaultRenderer(Color.class, new ColorRenderer());
		getDataTable().setDefaultEditor(Color.class, new ColorEditor());

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
		setupTable();
	}

	private class MapProcessorsTableModel extends AbstractTableModel {
		String[] columnNames = { "Layer", "Enabled", "Type", "Name",
				"Description", "Color", "Size" };
		private ArrayList<IMapProcessor> rawData;
		private MapProcessorsList dataChangedSource;

		public MapProcessorsTableModel(ArrayList<IMapProcessor> rawData,
				MapProcessorsList dataChangedSource) {
			super();
			this.rawData = rawData;
			this.dataChangedSource = dataChangedSource;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return rawData.size();
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			switch (col) {
			case 0:
				return row + 1;
			case 1:
				return rawData.get(row).isEnabled();
			case 2:
				return rawData.get(row).getType();
			case 3:
				return rawData.get(row).getName();
			case 4:
				return rawData.get(row).getDescription();
			case 5:
				return rawData.get(row).getColor();
			case 6:
				return rawData.get(row).getSize();
			}
			return "";
		}

		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			if (col == 1 || col == 5 || col == 6) {
				return true;
			} else {
				return false;
			}
		}

		public void setValueAt(Object value, int row, int col) {
			switch (col) {
			case 1:
				rawData.get(row).setEnabled((Boolean) value);
				break;
			case 5:
				rawData.get(row).setColor((Color) value);
				break;
			case 6:
				rawData.get(row).setSize((Integer) value);
				break;
			}

			if (getDataChanged() != null) {
				getDataChanged().stateChanged(
						new ChangeEvent(dataChangedSource));
			}

			this.fireTableCellUpdated(row, col);
			return;
		}
	}
}