package org.penemunxt.projects.penemunxtexplorer.pc.map.processing;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;

public interface IMapProcessor {
	public static enum MapProcessorType {
		SimpleData,
		CalculatedData
	}
	
	public Color getColor();
	public void setColor(Color c);

	public int getSize();
	public void setSize(int size);
	
	public boolean isEnabled();
	public void setEnabled(boolean enabled);
	
	public String getName();
	public String getDescription();
	public MapProcessorType getType();
	
	public void reset();
	
	public void processData(ArrayList<RobotData> data, Graphics g);
}