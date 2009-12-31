package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.IMapProcessor;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapPositionPoints;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapUtilities;

public class MapFindLines extends MapObjectPositions implements IMapProcessor {
	public MapFindLines() {
		super();
	}

	public MapFindLines(int size, boolean enabled) {
		super(null, size, enabled);
	}

	@Override
	public void processData(ArrayList<RobotData> data, Graphics g) {
		ArrayList<MapPositionPoints> tempObjectPoitionsPoints = this
				.getObjectPositionsPoints(data);

		MapPositionPoints.ClearList(tempObjectPoitionsPoints);
		tempObjectPoitionsPoints = MapPositionPoints
				.GetFilteredPositionsPoints(tempObjectPoitionsPoints,
						HOT_SPOTS_MAX_DISTANCE_SQ_TO_NEXT_POSITON * 100, 25, 5,
						0);

		int maxPoints = MapPositionPoints
				.GetMaxPoints(tempObjectPoitionsPoints);

		for (MapPositionPoints ScanPoint : tempObjectPoitionsPoints) {
			MapPositionPoints PositionWithMostPoints = ScanPoint
					.GetPositionWithMostPoints();
			if (PositionWithMostPoints.getNeighborsLinesToThis() == null) {
				PositionWithMostPoints
						.setNeighborsLinesToThis(new ArrayList<MapPositionPoints>());
			}

			PositionWithMostPoints.getNeighborsLinesToThis().add(ScanPoint);
		}

		for (MapPositionPoints ScanPoint : tempObjectPoitionsPoints) {
			float pointPercentage = (ScanPoint.getPoints() / (float) maxPoints);
			Color c = new Color((int) (pointPercentage * 255),
					255 - (int) (pointPercentage * 255), 0);

			MapPositionPoints PositionWithMostPoints = ScanPoint
					.GetPositionWithMostPoints();

			if (ScanPoint.getNeighborsLinesToThis() != null
					&& ScanPoint.getNeighborsLinesToThis().size() > 0) {

				MapUtilities.paintLine(ScanPoint.getY(),
					ScanPoint.getX(), PositionWithMostPoints
					.getY(), PositionWithMostPoints.getX(), c, this.getSize(), g);
			}
		}
	}

	@Override
	public String getName() {
		return "Find lines";
	}

	@Override
	public String getDescription() {
		return "Calculation of where the objects are placed in lines.";
	}
}
