package org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.IMapProcessor;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapUtilities;

public class MapBackgroundGrid extends MapObjectPositions implements
		IMapProcessor {

	final static int LINE_SPACE_DEFAULT = 100;
	final static Color MIDDLE_CROSS_LINE_COLOR_DEFAULT = Color.RED;
	final static int MIDDLE_CROSS_LINE_WIDTH_MULTIPLIER_DEFAULT = 5;

	final int LINE_MAX_X = 10000;
	final int LINE_MAX_Y = 10000;

	int gridLineSpace;
	Color gridMiddleCrossLineColor;
	int gridMiddleCrossLineWidth;

	public int getGridLineSpace() {
		return gridLineSpace;
	}

	public void setGridLineSpace(int gridLineSpace) {
		this.gridLineSpace = gridLineSpace;
	}

	public Color getGridMiddleCrossLineColor() {
		return gridMiddleCrossLineColor;
	}

	public void setGridMiddleCrossLineColor(Color gridMiddleCrossLineColor) {
		this.gridMiddleCrossLineColor = gridMiddleCrossLineColor;
	}

	public int getGridMiddleCrossLineWidth() {
		return gridMiddleCrossLineWidth;
	}

	public void setGridMiddleCrossLineWidth(int gridMiddleCrossLineWidth) {
		this.gridMiddleCrossLineWidth = gridMiddleCrossLineWidth;
	}

	public MapBackgroundGrid() {
		super();
		setDefaults(this.getSize());
	}

	public MapBackgroundGrid(Color c, int size, boolean enabled) {
		super(c, size, enabled);
		setDefaults(this.getSize());
	}

	public MapBackgroundGrid(Color c, int size, int gridLineSpace,
			Color gridMiddleCrossLineColor, int gridMiddleCrossLineWidth,
			boolean enabled) {
		super(c, size, enabled);

		this.gridLineSpace = gridLineSpace;
		this.gridMiddleCrossLineColor = gridMiddleCrossLineColor;
		this.gridMiddleCrossLineWidth = gridMiddleCrossLineWidth;
	}

	private void setDefaults(int size) {
		this.gridLineSpace = LINE_SPACE_DEFAULT;
		this.gridMiddleCrossLineColor = MIDDLE_CROSS_LINE_COLOR_DEFAULT;
		this.gridMiddleCrossLineWidth = (size * MIDDLE_CROSS_LINE_WIDTH_MULTIPLIER_DEFAULT);
	}

	@Override
	public void processData(ArrayList<RobotData> data, Graphics g) {	
		// Horizontal
		for (int i = 0; i <= LINE_MAX_X; i += this.getGridLineSpace()) {
			MapUtilities.paintLine(i, -LINE_MAX_Y, i, LINE_MAX_Y, this
					.getColor(), this.getSize(), g);
		}

		for (int i = 0; i >= -LINE_MAX_X; i -= this.getGridLineSpace()) {
			MapUtilities.paintLine(i, -LINE_MAX_Y, i, LINE_MAX_Y, this
					.getColor(), this.getSize(), g);
		}

		// Vertical
		for (int i = 0; i <= LINE_MAX_Y; i += this.getGridLineSpace()) {
			MapUtilities.paintLine(-LINE_MAX_X, i, LINE_MAX_X, i, this
					.getColor(), this.getSize(), g);
		}

		for (int i = 0; i >= -LINE_MAX_Y; i -= this.getGridLineSpace()) {
			MapUtilities.paintLine(-LINE_MAX_X, i, LINE_MAX_X, i, this
					.getColor(), this.getSize(), g);
		}

		// Middle cross
		// //Horizontal
		MapUtilities.paintLine(0, -LINE_MAX_Y, 0, LINE_MAX_Y, this
				.getGridMiddleCrossLineColor(), this
				.getGridMiddleCrossLineWidth(), g);
		// //Vertical
		MapUtilities.paintLine(-LINE_MAX_X, 0, LINE_MAX_X, 0, this
				.getGridMiddleCrossLineColor(), this
				.getGridMiddleCrossLineWidth(), g);
	}

	@Override
	public String getName() {
		return "Background grid";
	}

	@Override
	public String getDescription() {
		return "Grid with lines in the backround";
	}

	public boolean isAffectedByRotation() {
		return false;
	}
}
