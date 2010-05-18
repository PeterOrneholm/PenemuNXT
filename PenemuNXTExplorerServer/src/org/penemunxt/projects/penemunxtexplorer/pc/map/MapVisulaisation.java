package org.penemunxt.projects.penemunxtexplorer.pc.map;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.VolatileImage;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

import org.penemunxt.graphics.pc.Images;
import org.penemunxt.projects.penemunxtexplorer.pc.connection.*;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.*;

public class MapVisulaisation extends JPanel implements MouseListener,
		MouseMotionListener, MouseWheelListener {
	public final static int MAP_FRAME_LAST = -100;

	final static float MAP_DEFAULT_SCALE_FACTOR = 0.004f;
	public final static int MAP_MIN_SCALE = 1;
	public final static int MAP_MAX_SCALE = 150;
	final static int MAP_INIT_SCALE = 35;

	int mapScale;
	double mapRotate;
	Point mapCenter;

	Point mapStartDrag;
	Point mapCenterBeforeDrag;

	DataShare DS;
	VolatileImage OSI;
	MapProcessors mapProcessors;
	int mapCurrentFrame;
	boolean mapCentered;

	public ArrayList<ChangeListener> mapScaleChanged = new ArrayList<ChangeListener>();
	public ArrayList<ChangeListener> mapCenterChanged = new ArrayList<ChangeListener>();
	public ArrayList<MouseListener> mapMouseClick = new ArrayList<MouseListener>();

	private void fireMapScaleChanged(ChangeEvent ce) {
		for (ChangeListener listener : mapScaleChanged) {
			listener.stateChanged(ce);
		}
	}

	private void fireMapCenterChanged(ChangeEvent ce) {
		for (ChangeListener listener : mapCenterChanged) {
			listener.stateChanged(ce);
		}
	}

	private void fireMapMouseClick(MouseEvent me) {
		for (MouseListener listener : mapMouseClick) {
			listener.mouseClicked(me);
		}
	}

	public int getMapScale() {
		return mapScale;
	}

	public void setMapScale(int mapScale) {
		this.setMapScale(mapScale, true);
	}

	public void setMapScale(int mapScale, boolean triggerChanged) {
		this.mapScale = Math.max(Math.min(mapScale, MAP_MAX_SCALE),
				MAP_MIN_SCALE);
		this.refresh();

		if (triggerChanged && mapScaleChanged != null) {
			fireMapScaleChanged(new ChangeEvent(this));
		}
	}

	public double getMapRotate() {
		return mapRotate;
	}

	public void setMapRotate(double mapRotate) {
		this.mapRotate = mapRotate;
	}

	public Point getMapCenter() {
		return mapCenter;
	}

	public void setMapCenter(Point mapCenter) {
		this.setMapCenter(mapCenter, true);
	}

	public void setMapCenter(Point mapCenter, boolean triggerChanged) {
		this.mapCenter = mapCenter;
		this.refresh();

		if (triggerChanged && mapScaleChanged != null) {
			fireMapCenterChanged(new ChangeEvent(this));
		}
	}

	public int getMapCurrentFrame() {
		return mapCurrentFrame;
	}

	public void setMapCurrentFrame(int mapCurrentFrame) {
		if (mapCurrentFrame == MAP_FRAME_LAST) {
			if (DS != null && DS.NXTRobotData != null) {
				this.mapCurrentFrame = DS.NXTRobotData.size();
			} else {
				this.mapCurrentFrame = 0;
			}

		} else {
			this.mapCurrentFrame = mapCurrentFrame;
		}

		// this.refresh();
	}

	public MapProcessors getMapProcessors() {
		return mapProcessors;
	}

	public void setMapProcessors(MapProcessors mapProcessors) {
		this.mapProcessors = mapProcessors;
	}

	public DataShare getDS() {
		return DS;
	}

	public void setDS(DataShare dS) {
		DS = dS;
	}

	public MapVisulaisation(MapProcessors mapProcessors) {
		this(0, 0, null, mapProcessors, true);
	}

	public MapVisulaisation(int mapScale, MapProcessors mapProcessors,
			boolean enableInteraction) {
		this(mapScale, 0, null, mapProcessors, enableInteraction);
	}

	public MapVisulaisation(int mapScale, double mapRotate,
			MapProcessors mapProcessors, boolean enableInteraction) {
		this(mapScale, mapRotate, null, mapProcessors, enableInteraction);
	}

	public MapVisulaisation(int mapScale, double mapRotate, Point mapCenter,
			MapProcessors mapProcessors, boolean enableInteraction) {
		super();
		this.reset();
		if (mapScale >= MAP_MIN_SCALE && mapScale <= MAP_MAX_SCALE) {
			this.mapScale = mapScale;
		}

		this.mapRotate = mapRotate;

		if (mapCenter != null) {
			this.mapCenter = mapCenter;
		}

		this.mapProcessors = mapProcessors;

		if (enableInteraction) {
			this.addMouseMotionListener(this);
			this.addMouseListener(this);
			this.addMouseWheelListener(this);
		}

		this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		mapCentered = false;
	}

	public Image getMapImage() {
		if (OSI != null) {
			try {
				return Images.toBufferedImage(OSI);
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	public void refresh() {
		repaint();
	}

	public void reset() {
		this.resetMapCenter();
		this.resetMapScale();
	}

	public void resetMapScale() {
		this.setMapScale(MAP_INIT_SCALE);
	}

	public void resetMapCenter() {
		Point mapDataCenter;
		if (this.DS != null) {
			// mapDataCenter = MapUtilities.getCenterPos(this.DS.NXTRobotData);
		} else {
			mapDataCenter = new Point(0, 0);
		}

		this.setMapCenter(new Point((this.getWidth() / 2),
				(this.getHeight() / 2)));
	}

	@Override
	public void paintComponent(Graphics g) {

		if (mapCentered == false && mapCenter.x == 0 && mapCenter.y == 0) {
			resetMapCenter();
			mapCentered = true;
		}

		if (OSI == null || OSI.getWidth() != getWidth()
				|| OSI.getHeight() != getHeight()) {
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			GraphicsConfiguration gc = ge.getDefaultScreenDevice()
					.getDefaultConfiguration();

			OSI = createVolatileImage(this.getWidth(), this.getHeight());
		}

		Graphics OSIG = OSI.getGraphics();
		OSIG.clearRect(0, 0, OSI.getWidth(), OSI.getHeight());

		if (mapProcessors != null && DS != null && DS.NXTRobotData != null) {
			mapProcessors.processData(DS.NXTRobotData, mapCurrentFrame,
					(mapScale * MAP_DEFAULT_SCALE_FACTOR), mapRotate,
					(int) mapCenter.getX(), (int) mapCenter.getY(), OSIG);
		}
		g.drawImage(OSI, 0, 0, null);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.isControlDown()){
			double mapRotateTemp = Math.toDegrees(this.getMapRotate());
			mapRotateTemp += -e.getWheelRotation() * 5;
			this.setMapRotate(Math.toRadians(mapRotateTemp%360));
		}else{
			int mapScaleTemp = this.getMapScale();
			mapScaleTemp += -e.getWheelRotation() * 2;
			mapScaleTemp = Math.max(mapScaleTemp, MAP_MIN_SCALE);
			mapScaleTemp = Math.min(mapScaleTemp, MAP_MAX_SCALE);
			this.setMapScale(mapScaleTemp);
		}

		refresh();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (mapCenter != null) {
			mapCenterBeforeDrag = (Point) mapCenter.clone();
			mapStartDrag = new Point(e.getX(), e.getY());
		}

		refresh();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		AffineTransform currentMapTransform = MapUtilities.getMapTransform(
				(int) mapCenter.getX(), (int) mapCenter.getY(),
				(mapScale * MAP_DEFAULT_SCALE_FACTOR), mapRotate);
		if (currentMapTransform != null) {
			try {
				Point2D p = currentMapTransform.inverseTransform(
						e.getPoint(), null);
				MouseEvent me = new MouseEvent(this, e.getID(), e.getWhen(), e.getModifiers(), (int) p.getX(), (int) p.getY(), e.getClickCount(), e.isPopupTrigger());
				fireMapMouseClick(me);
			} catch (NoninvertibleTransformException e1) {
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mapCenterBeforeDrag != null && mapStartDrag != null) {
			int x = (int) (mapCenterBeforeDrag.getX() + (e.getX() - mapStartDrag
					.getX()));
			int y = (int) (mapCenterBeforeDrag.getY() + (e.getY() - mapStartDrag
					.getY()));
			this.getMapCenter().setLocation(x, y);
			this.setMapCenter(this.getMapCenter());
		}

		refresh();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
