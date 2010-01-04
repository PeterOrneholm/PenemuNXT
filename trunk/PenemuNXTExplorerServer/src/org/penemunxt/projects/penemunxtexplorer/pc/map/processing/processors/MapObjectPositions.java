package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.IMapProcessor;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapPositionPoints;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapUtilities;
import org.penemunxt.sensors.SensorRanges;

public abstract class MapObjectPositions extends MapSimplePosition implements
		IMapProcessor {
	final static int HOT_SPOTS_MAX_DISTANCE_SQ_TO_NEXT_POSITON = 800;
	final static int HOT_SPOTS_FIND_CONNECTIONS = 10;

	private ArrayList<Point> objectPositions;
	private ArrayList<MapPositionPoints> objectPositionsPoints;

	public MapObjectPositions() {
		super();
	}

	public MapObjectPositions(Color c, int size, boolean enabled) {
		super(c, size, enabled);
	}

	public ArrayList<Point> getObjectPositions(ArrayList<RobotData> data) {
		calculateObjectPositions(data);
		return objectPositions;
	}

	public void setObjectPositions(ArrayList<Point> objectPositions) {
		this.objectPositions = objectPositions;
	}

	public ArrayList<MapPositionPoints> getObjectPositionsPoints(
			ArrayList<RobotData> data) {
		calculateObjectPositionsPoints(data);
		return objectPositionsPoints;
	}

	public void setObjectPositionsPoints(
			ArrayList<MapPositionPoints> objectPositionsPoints) {
		this.objectPositionsPoints = objectPositionsPoints;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public MapProcessorType getType() {
		return MapProcessorType.CalculatedData;
	}

	@Override
	public void processData(ArrayList<RobotData> data, Graphics g) {
	}

	@Override
	public void reset() {
		objectPositions = null;
		objectPositionsPoints = null;
	}

	public void calculateObjectPositions(ArrayList<RobotData> data) {
		if (objectPositions == null) {
			objectPositions = new ArrayList<Point>();
			if (data != null) {
				for (RobotData robotData : data) {
					if (robotData != null) {
						if (robotData.getType() != RobotData.POSITION_TYPE_NOT_VALID) {
							Point ObjectPos = MapUtilities
									.getObjectPos(robotData);

							if (ObjectPos != null) {
								objectPositions.add(ObjectPos);
							}
						}
					}
				}
			}
		}
	}

	public void calculateObjectPositionsPoints(ArrayList<RobotData> data) {
		ArrayList<Point> tempObjectPositions = getObjectPositions(data);

		if (objectPositionsPoints == null) {
			objectPositionsPoints = new ArrayList<MapPositionPoints>();
			if (data != null) {
				for (Point objectPosition : tempObjectPositions) {
					objectPositionsPoints.add(new MapPositionPoints(0,
							(int) objectPosition.getX(), (int) objectPosition
									.getY()));
				}
			}

			objectPositionsPoints = MapPositionPoints
					.GetFilteredPositionsPoints(objectPositionsPoints,
							HOT_SPOTS_MAX_DISTANCE_SQ_TO_NEXT_POSITON,
							HOT_SPOTS_FIND_CONNECTIONS, 1, 0);
		}
	}
}
