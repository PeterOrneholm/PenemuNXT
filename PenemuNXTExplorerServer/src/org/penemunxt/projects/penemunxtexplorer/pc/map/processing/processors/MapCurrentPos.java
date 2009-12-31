package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.IMapProcessor;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapUtilities;

public class MapCurrentPos extends MapSimplePosition implements IMapProcessor {
	public final static int EXPLORER_BOT_LENGTH_MM = 230;
	public final static int EXPLORER_BOT_WIDTH_MM = 140;

	public MapCurrentPos() {
		super();
	}

	public MapCurrentPos(Color c, int size, boolean enabled) {
		super(c, size, enabled);
	}

	@Override
	public void processData(ArrayList<RobotData> data, Graphics g) {
		if (data != null && data.size() > 0) {
			RobotData LatestData = MapUtilities.getLatestData(data);
			super.processPositionData(LatestData, g);

			Graphics2D g2 = (Graphics2D) g;

//			int x = LatestData.getPosX() - (EXPLORER_BOT_WIDTH_MM / 2);
//			int y = LatestData.getPosY();
//			int width = EXPLORER_BOT_WIDTH_MM;
//			int height = EXPLORER_BOT_LENGTH_MM;
//
//			Point mapPos = MapUtilities.getMapPos(y, x);
//
//			g2.setColor(this.getColor());
//			g2.fillRect(mapPos.x, mapPos.y, width, height);

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
