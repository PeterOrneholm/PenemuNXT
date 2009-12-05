package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.IMapProcessor;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapUtilities;

public class MapCurrentPos extends MapSimplePosition implements IMapProcessor {
	public MapCurrentPos() {
		super();
	}

	public MapCurrentPos(Color c, int size, boolean enabled) {
		super(c, size, enabled);
	}

	@Override
	public void processData(ArrayList<RobotData> data, float scale,
			int centerX, int centerY, Graphics g) {
		if (data != null && data.size() > 0) {
			RobotData LatestData = MapUtilities.getLatestData(data);
			super.processPositionData(LatestData, scale, centerX, centerY, g);
		}
	}

	@Override
	public String getName() {
		return "Current position";
	}

	@Override
	public String getDescription() {
		return "The current position of the robot.";
	}
}
