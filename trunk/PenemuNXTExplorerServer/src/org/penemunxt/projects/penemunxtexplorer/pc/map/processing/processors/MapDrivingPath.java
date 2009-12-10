package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.IMapProcessor;

public class MapDrivingPath extends MapSimplePosition implements IMapProcessor {
	public MapDrivingPath() {
		super();
	}
	
	public MapDrivingPath(Color c, int size, boolean enabled) {
		super(c, size, enabled);
	}

	@Override
	public void processData(ArrayList<RobotData> data, Graphics g) {
		super.processPositionData(data, RobotData.POSITION_TYPE_DRIVE, g);
	}

	@Override
	public String getName() {
		return "Driving path";
	}
	
	@Override
	public String getDescription() {
		return "The path the bot has traveled.";
	}
}
