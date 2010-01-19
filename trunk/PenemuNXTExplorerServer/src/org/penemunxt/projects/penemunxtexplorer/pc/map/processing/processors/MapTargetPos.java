package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.IMapProcessor;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapUtilities;

public class MapTargetPos extends MapSimplePosition implements IMapProcessor {
	public MapTargetPos() {
		super();
	}

	public MapTargetPos(Color c, int size, boolean enabled) {
		super(c, size, enabled);
	}

	@Override
	public void processData(ArrayList<RobotData> data, Graphics g) {
		if (data != null && data.size() > 0) {
			RobotData LatestData = MapUtilities.getLatestData(data);
			if (LatestData != null) {
				MapUtilities.paintOval(LatestData.getTargetPosX(), LatestData.getTargetPosX(), this
						.getColor(), this.getSize(), g);
			}
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
