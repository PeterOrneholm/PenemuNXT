package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.IMapProcessor;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapProcessorMain;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapUtilities;

public abstract class MapSimplePosition extends MapProcessorMain implements
		IMapProcessor {
	public MapSimplePosition() {
		super();
	}

	public MapSimplePosition(Color c, int size, boolean enabled) {
		super(c, size, enabled);
	}

	@Override
	public MapProcessorType getType() {
		return MapProcessorType.SimpleData;
	}

	public void processPositionData(RobotData data, Graphics g) {
		if (data != null) {
			MapUtilities.paintOval(data.getPosY(), data.getPosX(), this
					.getColor(), this.getSize(), g);
		}
	}

	public void processPositionData(ArrayList<RobotData> data,
			int positionTypeFilter,
			Graphics g) {
		if (data != null) {
			for (RobotData robotData : data) {
				if (robotData != null) {
					if (robotData.getType() == positionTypeFilter) {
						this.processPositionData(robotData, g);
					}
				}
			}
		}
	}

	@Override
	public void processData(ArrayList<RobotData> data, Graphics g) {
	}
}
