package org.penemunxt.projects.communicationtest.pc;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MapPositionPoints {
	private int Points;
	private int X;
	private int Y;
	private int tempDistance;

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

	public MapPositionPoints(int points, int x, int y) {
		super();
		Points = points;
		X = x;
		Y = y;
	}

	public static void GetPositionsPoints(
			ArrayList<MapPositionPoints> UnfilteredPositions,
			int maxDistanceSq, int findConnections) {
		for (MapPositionPoints MPP : UnfilteredPositions) {
			ArrayList<MapPositionPoints> ClosestPositions = new ArrayList<MapPositionPoints>();
			int longestDistanceSq = 0;

			for (MapPositionPoints ScanPoint : UnfilteredPositions) {
				if (ScanPoint != MPP) {
					int distanceSq = (int) Point2D.distanceSq((int) MPP.getX(),
							(int) MPP.getY(), (int) ScanPoint.getX(),
							(int) ScanPoint.getY());

					if (distanceSq < maxDistanceSq) {
						if (ClosestPositions.size() < findConnections) {
							ScanPoint.setTempDistance(distanceSq);
							ClosestPositions.add(ScanPoint);
							if (distanceSq > longestDistanceSq) {
								longestDistanceSq = distanceSq;
							}
						} else if (distanceSq < longestDistanceSq) {
							for (MapPositionPoints ClosestPoint : ClosestPositions) {
								if (ClosestPoint.getTempDistance() == longestDistanceSq) {
									ClosestPositions.remove(ClosestPoint);
									break;
								}
							}
							ScanPoint.setTempDistance(distanceSq);
							ClosestPositions.add(ScanPoint);

							longestDistanceSq = 0;
							for (MapPositionPoints ClosestPoint : ClosestPositions) {
								if (ClosestPoint.getTempDistance() > longestDistanceSq) {
									longestDistanceSq = ClosestPoint
											.getTempDistance();
								}
							}
						}
					}
				}
			}

			MPP.setPoints(MPP.getPoints() + findConnections);
			for (MapPositionPoints ClosestPoint : ClosestPositions) {
				ClosestPoint.setPoints(ClosestPoint.getPoints() + 1);
			}
		}
	}

	public static int GetMaxPoints(ArrayList<MapPositionPoints> Positions) {
		int maxPoints = 0;
		for (MapPositionPoints MPP : Positions) {
			maxPoints = Math.max(maxPoints, MPP.getPoints());
		}
		return maxPoints;
	}
}
