package org.penemunxt.projects.communicationtest.pc;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MapPositionPoints {
	private int Points;
	private int X;
	private int Y;
	private int tempDistance;
	private ArrayList<MapPositionPoints> ClosestNeighbors;
	private ArrayList<MapPositionPoints> NeighborsLinesToThis;

	public int getPoints() {
		return Points;
	}

	public void setPoints(int points) {
		Points = points;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public int getTempDistance() {
		return tempDistance;
	}

	public void setTempDistance(int tempDistance) {
		this.tempDistance = tempDistance;
	}

	public ArrayList<MapPositionPoints> getClosestNeighbors() {
		return ClosestNeighbors;
	}

	public void setClosestNeighbors(
			ArrayList<MapPositionPoints> closestNeighbors) {
		ClosestNeighbors = closestNeighbors;
	}

	public ArrayList<MapPositionPoints> getNeighborsLinesToThis() {
		return NeighborsLinesToThis;
	}

	public void setNeighborsLinesToThis(
			ArrayList<MapPositionPoints> neighborsLinesToThis) {
		NeighborsLinesToThis = neighborsLinesToThis;
	}

	public MapPositionPoints(int points, int x, int y) {
		super();
		Points = points;
		X = x;
		Y = y;
	}

	public void clear() {
		this.ClosestNeighbors = null;
		this.Points = 0;
	}

	public static ArrayList<MapPositionPoints> GetFilteredPositionsPoints(
			ArrayList<MapPositionPoints> UnfilteredPositions,
			int maxDistanceSq, int findConnections, int requiredConnections,
			int requiredPoints) {
		ArrayList<MapPositionPoints> FilteredPositions = new ArrayList<MapPositionPoints>();

		GetPositionsPoints(UnfilteredPositions, maxDistanceSq, findConnections);

		for (MapPositionPoints MPP : UnfilteredPositions) {
			if (MPP.ClosestNeighbors.size() >= requiredConnections
					&& MPP.getPoints() >= requiredPoints) {
				FilteredPositions.add(MPP);
			}
		}

		return FilteredPositions;
	}

	public static void GetPositionsPoints(
			ArrayList<MapPositionPoints> UnfilteredPositions,
			int maxDistanceSq, int findConnections) {
		for (MapPositionPoints MPP : UnfilteredPositions) {
			MPP.ClosestNeighbors = new ArrayList<MapPositionPoints>();
			int longestDistanceSq = 0;

			for (MapPositionPoints ScanPoint : UnfilteredPositions) {
				if (ScanPoint != MPP) {
					int distanceSq = (int) Point2D.distanceSq(MPP.getX(),
							MPP.getY(), ScanPoint.getX(),
							ScanPoint.getY());

					if (distanceSq < maxDistanceSq) {
						if (MPP.ClosestNeighbors.size() < findConnections) {
							ScanPoint.setTempDistance(distanceSq);
							MPP.ClosestNeighbors.add(ScanPoint);
							if (distanceSq > longestDistanceSq) {
								longestDistanceSq = distanceSq;
							}
						} else if (distanceSq < longestDistanceSq) {
							for (MapPositionPoints ClosestPoint : MPP.ClosestNeighbors) {
								if (ClosestPoint.getTempDistance() == longestDistanceSq) {
									MPP.ClosestNeighbors.remove(ClosestPoint);
									break;
								}
							}
							ScanPoint.setTempDistance(distanceSq);
							MPP.ClosestNeighbors.add(ScanPoint);

							longestDistanceSq = 0;
							for (MapPositionPoints ClosestPoint : MPP.ClosestNeighbors) {
								if (ClosestPoint.getTempDistance() > longestDistanceSq) {
									longestDistanceSq = ClosestPoint
											.getTempDistance();
								}
							}
						}
					}
				}
			}

			MPP.setPoints(MPP.getPoints() + MPP.ClosestNeighbors.size());
			for (MapPositionPoints ClosestPoint : MPP.ClosestNeighbors) {
				ClosestPoint.setPoints(ClosestPoint.getPoints() + 1);
			}
		}
	}

	public MapPositionPoints GetPositionWithMostPoints() {
		MapPositionPoints closestPosition = null;
		int highestPoints = 0;

		if (this.ClosestNeighbors != null) {
			for (MapPositionPoints CN : this.ClosestNeighbors) {
				if (closestPosition == null || CN.getPoints() > highestPoints) {
					closestPosition = CN;
				}
			}
		}

		return closestPosition;
	}

	public static int GetMaxPoints(ArrayList<MapPositionPoints> Positions) {
		int maxPoints = 0;
		for (MapPositionPoints MPP : Positions) {
			maxPoints = Math.max(maxPoints, MPP.getPoints());
		}
		return maxPoints;
	}

	public static void ClearList(ArrayList<MapPositionPoints> Positions) {
		for (MapPositionPoints MPP : Positions) {
			MPP.clear();
		}
	}
}
