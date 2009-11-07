package org.penemunxt.projects.communicationtest.pc;

import java.awt.Color;
import java.util.Hashtable;

public class MapPoints {
	final static int MAP_POINT_CENTER_POINTS = 17;

	public static void updateMapPositionPoints(
			Hashtable<String, MapPositionPoints> mapPoints, int centerX,
			int centerY) {
		updateMapPositionPoint(mapPoints, centerX, centerY,
				MAP_POINT_CENTER_POINTS);

		int distance = 0;
		for (int i = (MAP_POINT_CENTER_POINTS - 1); i > 0; i--) {
			distance++;
			updateMapPositionPointsAtDistance(mapPoints, centerX, centerY, i,
					distance);
		}
	}

	public static void updateMapPositionPointsAtDistance(
			Hashtable<String, MapPositionPoints> mapPoints, int centerX,
			int centerY, int points, int distance) {

		// Top And Bottom
		for (int x = (centerX - distance); x <= (centerX + distance); x++) {
			updateMapPositionPoint(mapPoints, x, (centerY + distance), points);
			updateMapPositionPoint(mapPoints, x, (centerY - distance), points);
		}

		// Left And Right
		for (int y = (centerY - distance + 1); y <= (centerY + distance - 1); y++) {
			updateMapPositionPoint(mapPoints, (centerX + distance), y, points);
			updateMapPositionPoint(mapPoints, (centerX - distance), y, points);
		}
	}

	public static void updateMapPositionPoint(
			Hashtable<String, MapPositionPoints> mapPoints, int x, int y,
			int addPoints) {
		if (mapPoints != null) {
			String pointKey = String.valueOf(x) + ';' + String.valueOf(y);
			Integer oldValue = 0;
			if (mapPoints.containsKey(pointKey)) {
				oldValue = mapPoints.get(pointKey).getPoints();
				if (oldValue == null) {
					oldValue = 0;
				}
				mapPoints.remove(pointKey);
			}

			mapPoints.put(pointKey, new MapPositionPoints(new Integer(oldValue
					+ addPoints), x, y));
		}
	}

	/*
	 * MapPoints.updateMapPositionPoints(PositionPoints, (int)
	 * HeadMapPos.getX(), (int) HeadMapPos .getY());
	 */

	// if (PositionPoints != null && 1 == 2) {
	// System.out.println(PositionPoints.size());
	// for (MapPositionPoints MPP : PositionPoints.values()) {
	// if (MPP.getPoints() > 0) {
	// int value = (int) (Math
	// .min(MPP.getPoints() / 10.0, 1.0) * 255);
	// Color c;
	// c = new Color(255, 0, 255);
	// if (MPP.getPoints() >= sldMapPointsSensitivity
	// .getValue()) {
	// paintOval(MPP.getY(), MPP.getX(), c, 5, g);
	// }
	// }
	// }
	// }
}
