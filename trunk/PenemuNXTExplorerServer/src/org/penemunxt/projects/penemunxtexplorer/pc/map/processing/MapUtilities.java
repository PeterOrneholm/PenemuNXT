package org.penemunxt.projects.penemunxtexplorer.pc.map.processing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;

public class MapUtilities {
	public static RobotData getLatestData(ArrayList<RobotData> data) {
		if (data != null && data.size() > 0) {
			return MapUtilities.getLatestData(data, data.size() - 1);
		} else {
			return null;
		}
	}

	public static RobotData getLatestData(ArrayList<RobotData> data,
			int maxIndex) {
		if (data != null && data.size() > 0) {
			return data.get(Math.max(maxIndex, 0));
		} else {
			return null;
		}
	}

	public static Point getMapPos(int x, int y) {
		return new Point(-y, x);
	}

	public static Point getHeadingPos(int x, int y, int heading, int distance) {
		int nx;
		int ny;

		nx = (int) (distance * Math.cos(heading * Math.PI / 180)) + x;
		ny = (int) (distance * Math.sin(heading * Math.PI / 180)) + y;

		return new Point(nx, ny);
	}

	public static void paintArrow(Graphics g, int x0, int y0, int x1, int y1) {
		int deltaX = x1 - x0;
		int deltaY = y1 - y0;
		double frac = 0.2;

		g.drawLine(x0, y0, x1, y1);
		g.drawLine(x0 + (int) ((1 - frac) * deltaX + frac * deltaY), y0
				+ (int) ((1 - frac) * deltaY - frac * deltaX), x1, y1);
		g.drawLine(x0 + (int) ((1 - frac) * deltaX - frac * deltaY), y0
				+ (int) ((1 - frac) * deltaY + frac * deltaX), x1, y1);

	}

	public static void paintOval(int x, int y, Color c, int size, Graphics g) {
		g.setColor(c);
		Point pos = getMapPos(x, y);
		g.fillOval(pos.x - (size / 2), pos.y - (size / 2), size, size);
	}
}
