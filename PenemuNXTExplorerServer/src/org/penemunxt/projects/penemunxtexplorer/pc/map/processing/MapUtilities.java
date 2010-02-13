package org.penemunxt.projects.penemunxtexplorer.pc.map.processing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.sensors.SensorRanges;

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

	public static Point getObjectPos(RobotData robotData) {
		Point newPoint = null;

		Point ObjectPos = MapUtilities.getHeadingPos(robotData.getPosX(),
				robotData.getPosY(), (-robotData.getHeadHeading() + robotData
						.getRobotHeading()), robotData.getHeadDistance());

		if (robotData.getHeadDistance() > SensorRanges.OPTICAL_DISTANCE_MIN_LENGTH_MM
				&& robotData.getHeadDistance() < SensorRanges.OPTICAL_DISTANCE_MAX_LENGTH_MM) {
			newPoint = ObjectPos;
		}

		return newPoint;
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

	public static void paintSquare(int x, int y, Color c, int size, Graphics g) {
		g.setColor(c);
		Point pos = getMapPos(x, y);
		g.fillRect(pos.x - (size / 2), pos.y - (size / 2), size, size);
	}

	public static void paintLine(int x1, int y1, int x2, int y2, Color c,
			float size, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		Stroke oldStroke = g2.getStroke();
		BasicStroke newStroke = new BasicStroke(size);

		Point pos1 = MapUtilities.getMapPos(x1, y1);
		Point pos2 = MapUtilities.getMapPos(x2, y2);

		g2.setStroke(newStroke);
		g2.setColor(c);
		g2.drawLine(pos1.x, pos1.y, pos2.x, pos2.y);
		g2.setStroke(oldStroke);
	}

	public static Point getCenterPos(ArrayList<RobotData> data) {
		return MapUtilities.getCenterPos(data, -1);
	}

	public static Point getCenterPos(ArrayList<RobotData> data, int maxIndex) {
		int x = 0;
		int y = 0;
		int index = data.size() - 1;

		if (maxIndex >= 0) {
			index = maxIndex;
		}
		
		if(!(index>=0)){
			index = 0;
		}

		for (int i = 0; i <= index; i++) {
			x += data.get(i).getPosX();
			y += data.get(i).getPosY();
		}

		x /= (index + 1);
		y /= (index + 1);

		return new Point(x, y);
	}
	
	public static AffineTransform getMapTransform(int centerX, int centerY, float scale, double rotate){
		AffineTransform mapTransform = new AffineTransform();
		
		mapTransform.translate(centerX, centerY);
		mapTransform.scale(scale, scale);
		mapTransform.rotate(rotate);
		
		return mapTransform;
	}
}
