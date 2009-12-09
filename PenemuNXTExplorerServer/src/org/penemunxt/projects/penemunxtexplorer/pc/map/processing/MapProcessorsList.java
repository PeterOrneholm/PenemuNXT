package org.penemunxt.projects.penemunxtexplorer.pc.map.processing;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.penemunxt.projects.penemunxtexplorer.pc.connection.DataShare;
import org.penemunxt.projects.penemunxtexplorer.pc.map.MapVisulaisation;
import org.penemunxt.windows.pc.*;
import org.penemunxt.windows.tables.pc.ColorEditor;
import org.penemunxt.windows.tables.pc.ColorRenderer;
import org.penemunxt.windows.tables.pc.SliderEditor;

public class MapProcessorsList extends DataTableWindow implements
		ListSelectionListener {

	final static Color DEFAULT_PANEL_BACKGROUND_COLOR = new Color(197, 209, 215);
	final static Color MAP_PANEL_BACKGROUND_COLOR = Color.WHITE;
	final static Color MAIN_PANEL_BACKGROUND_COLOR = DEFAULT_PANEL_BACKGROUND_COLOR;

	final static Color MAP_PANEL_BORDER_COLOR = Color.BLACK;
	final static int MAP_PANEL_BORDER_WIDTH = 2;
	final static int PANEL_MARGIN = 15;

	MapProcessors mapProcessors;
	MapProcessors mapPreviewProcessors;
	private ChangeListener dataChanged;
	MapVisulaisation mapPreview;
	DataShare DS;

	public ChangeListener getDataChanged() {
		return dataChanged;
	}

	public void setDataChanged(ChangeListener dataChanged) {
		this.dataChanged = dataChanged;
	}

	public MapProcessorsList(MapProcessors mapProcessors, DataShare DS,
			String applicationName, Image applicationIcon) {
		super(applicationName, applicationIcon);

		this.mapProcessors = mapProcessors;
		this.DS = DS;

		refresh();
	}

	@Override
	public Panel getContentPanel() {
		Font fntSectionHeader = new Font("Arial", Font.BOLD, 14);

		// Panels
		panelMain = new Panel(new BorderLayout());
		Panel pnlLeft = new Panel(new BorderLayout());
		Panel pnlRight = new Panel(new BorderLayout());

		// Left

		Label lblMapProcessorsHeader = new Label("Map Processors", Label.CENTER);
		lblMapProcessorsHeader.setFont(fntSectionHeader);

		JScrollPane scrollTable = new JScrollPane(getDataTable());

		pnlLeft.add(lblMapProcessorsHeader, BorderLayout.NORTH);
		pnlLeft.add(new ComponentSpacer(scrollTable, PANEL_MARGIN, MAIN_PANEL_BACKGROUND_COLOR,
				MAP_PANEL_BORDER_WIDTH, MAP_PANEL_BORDER_COLOR,
				MAP_PANEL_BACKGROUND_COLOR), BorderLayout.CENTER);

		// Right
		mapPreview = new MapVisulaisation(null);

		Label lblPreviewHeader = new Label("Preview", Label.CENTER);
		lblPreviewHeader.setFont(fntSectionHeader);

		pnlRight.setBackground(MAIN_PANEL_BACKGROUND_COLOR);

		pnlRight.setPreferredSize(new Dimension(400, 200));
		pnlRight.add(new ComponentSpacer(mapPreview, PANEL_MARGIN, MAIN_PANEL_BACKGROUND_COLOR,
				MAP_PANEL_BORDER_WIDTH, MAP_PANEL_BORDER_COLOR,
				MAP_PANEL_BACKGROUND_COLOR), BorderLayout.CENTER);
		pnlRight.add(lblPreviewHeader, BorderLayout.NORTH);

		// Main
		panelMain.setBackground(MAIN_PANEL_BACKGROUND_COLOR);
		panelMain.add(pnlLeft, BorderLayout.CENTER);
		panelMain.add(pnlRight, BorderLayout.EAST);

		return panelMain;
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
		getDataTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		getDataTable().getSelectionModel().addListSelectionListener(this);
	}

	public void refresh() {
		refresh(false);
	}

	@Override
	public void refresh(boolean focus) {
		super.refresh(focus);
		setupTable();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		ArrayList<IMapProcessor> previewProcessors = new ArrayList<IMapProcessor>();
		for (int i = 0; i < getDataTable().getSelectedRows().length; i++) {
			IMapProcessor mp = mapProcessors.list.get(getDataTable()
					.getSelectedRows()[i]);
			previewProcessors.add(mp);
		}

		if (mapPreviewProcessors == null) {
			mapPreviewProcessors = new MapProcessors(previewProcessors);
		} else {
			mapPreviewProcessors.setList(previewProcessors);
		}

		mapPreview.setMapCurrentFrame(MapVisulaisation.MAP_FRAME_LAST);
		mapPreview.setDS(DS);
		mapPreview.setMapProcessors(mapPreviewProcessors);
		mapPreview.refresh();
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

		@Override
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

		@Override
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			if (col == 1 || col == 5 || col == 6) {
				return true;
			} else {
				return false;
			}
		}

		@Override
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