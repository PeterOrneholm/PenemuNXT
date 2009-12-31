package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.IMapProcessor;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapUtilities;

public class MapClearArea extends MapObjectPositions implements IMapProcessor {
	public MapClearArea() {
		super();
	}

	public MapClearArea(Color c, int size, boolean enabled) {
		super(c, size, enabled);
	}

	@Override
	public void processData(ArrayList<RobotData> data, Graphics g) {
		if (data != null) {
			for (RobotData robotData : data) {
				if (robotData != null) {
					Point ObjectPos = MapUtilities.getObjectPos(robotData);
					if (ObjectPos != null) {
						MapUtilities.paintLine(robotData.getPosY(), robotData
								.getPosX(), ObjectPos.y, ObjectPos.x, this
								.getColor(), this.getSize(), g);
					}
				}
			}
		}
	}

	@Override
	public String getName() {
		return "Clear area";
	}

	@Override
	public String getDescription() {
		return "Area where there are no objects.";
	}
}
