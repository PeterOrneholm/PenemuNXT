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

	public MapFindLines(boolean enabled) {
		super(null, 0, enabled);
	}

	@Override
	public void processData(ArrayList<RobotData> data, float scale,
			int centerX, int centerY, Graphics g) {
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

			Point mapPosFrom = MapUtilities.getMapPos(ScanPoint.getY(),
					ScanPoint.getX(), scale, centerX, centerY);
			Point mapPosTo = MapUtilities.getMapPos(PositionWithMostPoints
					.getY(), PositionWithMostPoints.getX(), scale, centerX,
					centerY);

			if (ScanPoint.getNeighborsLinesToThis() != null
					&& ScanPoint.getNeighborsLinesToThis().size() > 0) {

				g.setColor(c);
				g.drawLine((int) mapPosFrom.getX(), (int) mapPosFrom.getY(),
						(int) mapPosTo.getX(), (int) mapPosTo.getY());
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
