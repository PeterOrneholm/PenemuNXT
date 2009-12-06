package org.penemunxt.projects.penemunxtexplorer.pc.map.processing;

import java.awt.Graphics;
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
		if (data != null && data.size() > 0) {
			ArrayList<RobotData> dataFiltered;
			dataFiltered = new ArrayList<RobotData>(data.subList(0, Math.min(
					maxIndex, data.size() - 1)));
			this.processData(dataFiltered, scale, centerX, centerY, g);
		}
	}

	public void processData(ArrayList<RobotData> data, float scale,
			int centerX, int centerY, Graphics g) {

		if (list != null && list.size() > 0) {
			for (IMapProcessor mapProcessor : this.getList()) {
				mapProcessor.reset();
			}
			for (IMapProcessor mapProcessor : this.getList()) {
				if (mapProcessor.isEnabled()) {
					mapProcessor.processData(data, scale, centerX, centerY, g);
				}
			}
		}
	}
}
