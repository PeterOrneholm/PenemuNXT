package org.penemunxt.projects.penemunxtexplorer.pc.map.processing;

import java.awt.*;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.RobotData;

public class MapProcessors {
	ArrayList<IMapProcessor> list;

	public ArrayList<IMapProcessor> getList() {
		return list;
	}

	public void setList(ArrayList<IMapProcessor> list) {
		this.list = list;
	}

	public MapProcessors(ArrayList<IMapProcessor> list) {
		super();
		this.list = list;
	}

	public void processData(ArrayList<RobotData> data, int maxIndex,
			float scale, int centerX, int centerY, Graphics g) {
		this.processData(data, scale, 0, centerX, centerY, g);
	}
	
	public void processData(ArrayList<RobotData> data, int maxIndex,
			float scale, double rotate, int centerX, int centerY, Graphics g) {
		if (data != null && data.size() > 0) {
			ArrayList<RobotData> dataFiltered;
			dataFiltered = new ArrayList<RobotData>(data.subList(0, Math.min(
					maxIndex, data.size() - 1)));
			this.processData(dataFiltered, scale, rotate, centerX, centerY, g);
		}
	}

	public void processData(ArrayList<RobotData> data, float scale,
			int centerX, int centerY, Graphics g) {
		this.processData(data, scale, 0, centerX, centerY, g);
	}

	public void processData(ArrayList<RobotData> data, float scale,
			double rotate, int centerX, int centerY, Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.translate(centerX, centerY);
		g2.scale(scale, scale);

		if (list != null && list.size() > 0) {
			for (IMapProcessor mapProcessor : this.getList()) {
				mapProcessor.reset();
			}
			for (IMapProcessor mapProcessor : this.getList()) {
				if (mapProcessor.isEnabled()) {
					if(mapProcessor.isAffectedByRotation()){
						g2.rotate(rotate);
					}
					mapProcessor.processData(data, g);
					if(mapProcessor.isAffectedByRotation()){
						g2.rotate(-rotate);
					}
				}
			}
		}
	}
}
