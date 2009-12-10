package org.penemunxt.projects.penemunxtexplorer.pc.map.processing;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;

public abstract class MapProcessorMain implements IMapProcessor, Cloneable {
	private final static Color DEFAULT_COLOR = Color.BLACK;
	private final static int DEFAULT_SIZE = 25;
	
	private boolean enabled;
	private Color c;
	private int size;

	public MapProcessorMain() {
		this(DEFAULT_COLOR, DEFAULT_SIZE, true);
	}

	public MapProcessorMain(Color c, int size, boolean enabled) {
		super();
		this.setEnabled(enabled);
		this.c = c;
		this.size = size;
	}

	public Color getColor() {
		return c;
	}

	public void setColor(Color c) {
		this.c = c;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
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
		return null;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void processData(ArrayList<RobotData> data, Graphics g) {
	}
	
	@Override
	public void reset() {
	}
}
