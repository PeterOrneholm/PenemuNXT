package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.IMapProcessor;

public class MapBumpDistance extends MapSimplePosition implements IMapProcessor {
	public MapBumpDistance() {
		super();
	}
	
	public MapBumpDistance(Color c, int size, boolean enabled) {
		super(c, size, enabled);
	}

	@Override
	public void processData(ArrayList<RobotData> data, float scale,
			int centerX, int centerY, Graphics g) {
		super.processPositionData(data, RobotData.POSITION_TYPE_BUMP_DISTANCE, scale, centerX, centerY, g);
	}
	
	@Override
	public String getName() {
		return "Bump (Front distance)";
	}
	
	@Override
	public String getDescription() {
		return "When the robot gets to close to a wall.";
	}
}
