package org.penemunxt.projects.penemunxtexplorer.pc.map.processing;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import org.penemunxt.projects.penemunxtexplorer.*;

public interface IMapProcessor {
	public static enum MapProcessorType {
		SimpleData,
		Improvement
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
	
	public void processData(ArrayList<RobotData> data, float scale,
			int centerX, int centerY, Graphics g);
}