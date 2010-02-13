package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.*;
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
			if (LatestData != null
					&& !(LatestData.getTargetPosX() == 0 && LatestData
							.getTargetPosY() == 0)) {
				MapUtilities.paintOval(LatestData.getTargetPosX(), LatestData
						.getTargetPosX(), this.getColor(), this.getSize(), g);
			}
		}
	}

	@Override
	public String getName() {
		return "Target position";
	}

	@Override
	public String getDescription() {
		return "The position the robot is told to head for.";
	}
}
