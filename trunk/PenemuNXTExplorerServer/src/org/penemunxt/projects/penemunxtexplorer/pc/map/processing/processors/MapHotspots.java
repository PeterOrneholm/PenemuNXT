package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.*;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.*;

public class MapHotspots extends MapObjectPositions implements IMapProcessor {
	public MapHotspots() {
		super();
	}

	public MapHotspots(int size, boolean enabled) {
		super(null, size, enabled);
	}

	@Override
	public void processData(ArrayList<RobotData> data, float scale,
			int centerX, int centerY, Graphics g) {
		ArrayList<MapPositionPoints> tempObjectPoitionsPoints = this
				.getObjectPositionsPoints(data);
		int maxPoints = MapPositionPoints
				.GetMaxPoints(tempObjectPoitionsPoints);

		for (MapPositionPoints ScanPoint : tempObjectPoitionsPoints) {
			float pointPercentage = (ScanPoint.getPoints() / (float) maxPoints);
			Color c = new Color((int) (pointPercentage * 255),
					255 - (int) (pointPercentage * 255), 0);

			MapUtilities.paintOval(ScanPoint.getY(), ScanPoint.getX(), c,
					(int) (pointPercentage * this.getSize()), scale, centerX,
					centerY, g);

		}
	}

	@Override
	public String getName() {
		return "Hotspots";
	}

	@Override
	public String getDescription() {
		return "Places calculated to be \"real\" objects.";
	}
}
