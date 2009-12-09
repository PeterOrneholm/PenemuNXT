package org.penemunxt.projects.penemunxtexplorer.pc.map.file;

import java.awt.*;
import java.beans.*;
import java.io.*;

import javax.swing.*;

import org.penemunxt.projects.penemunxtexplorer.pc.connection.DataShare;
import org.penemunxt.projects.penemunxtexplorer.pc.map.MapVisulaisation;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapProcessors;
import org.penemunxt.windows.pc.ComponentSpacer;

public class MapFilePreview extends JPanel implements PropertyChangeListener {
	final static Color MAP_PANEL_BACKGROUND_COLOR = Color.WHITE;
	final static Color MAP_PANEL_BORDER_COLOR = Color.BLACK;
	final static int MAP_PANEL_BORDER_WIDTH = 1;
	final static int MAP_PANEL_MARGIN = 0;
	
	File selectedFile = null;
	MapProcessors mapProcessors;
	MapVisulaisation mapVisulaisation;

	public MapFilePreview(JFileChooser fc, MapProcessors mapProcessors) {
		super(new BorderLayout());
		setPreferredSize(new Dimension(200, 200));
		fc.addPropertyChangeListener(this);

		this.mapProcessors = mapProcessors;
		this.mapVisulaisation = new MapVisulaisation(mapProcessors);

		createGUI();
	}

	private void createGUI() {
		this.add(new ComponentSpacer(mapVisulaisation, MAP_PANEL_MARGIN, MAP_PANEL_BORDER_WIDTH, MAP_PANEL_BORDER_COLOR, MAP_PANEL_BACKGROUND_COLOR), BorderLayout.CENTER);
	}

	private void resetMap() {
		mapVisulaisation.setDS(null);
		mapVisulaisation.refresh();
	}

	public void loadMap() {
		if (selectedFile == null) {
			return;
		} else {
			DataShare DS = new DataShare();

			try {
				MapFileUtilities.openData(selectedFile.getPath(), DS);

				mapVisulaisation.setDS(DS);
				mapVisulaisation.setMapCurrentFrame(MapVisulaisation.MAP_FRAME_LAST);
				mapVisulaisation.refresh();
			} catch (Exception e) {
			}

		}
	}

	public void propertyChange(PropertyChangeEvent e) {
		boolean doUpdate = false;
		String propName = e.getPropertyName();

		resetMap();

		if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(propName)) {
			selectedFile = null;
			doUpdate = true;
		} else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(propName)) {
			selectedFile = (File) e.getNewValue();
			doUpdate = true;
		}

		if (doUpdate) {
			if (isShowing()) {
				loadMap();
			}
		}
	}
}