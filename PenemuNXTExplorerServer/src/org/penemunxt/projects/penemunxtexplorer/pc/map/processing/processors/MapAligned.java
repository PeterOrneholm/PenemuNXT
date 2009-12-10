package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.IMapProcessor;

public class MapAligned extends MapSimplePosition implements IMapProcessor {
	public MapAligned() {
		super();
	}
	
	public MapAligned(Color c, int size, boolean enabled) {
		super(c, size, enabled);
	}

	@Override
	public void processData(ArrayList<RobotData> data, Graphics g) {
		super.processPositionData(data, RobotData.POSITION_TYPE_ALIGNED, g);
	}
	
	@Override
	public String getName() {
		return "Aligned to wall";
	}
	
	@Override
	public String getDescription() {
		return "When the robot recognizes that it's not aligned to the wall.";
	}
}
