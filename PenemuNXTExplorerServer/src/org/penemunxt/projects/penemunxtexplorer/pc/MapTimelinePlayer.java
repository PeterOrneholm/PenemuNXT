package org.penemunxt.projects.penemunxtexplorer.pc;

import javax.swing.JSlider;

public class MapTimelinePlayer extends Thread implements Runnable {
	private boolean isActive;
	private int delay;
	private JSlider sldTimeline;

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public void deactivate() {
		this.isActive = false;
	}

	public MapTimelinePlayer(JSlider sldTimeline, int delay) {
		super();
		this.delay = delay;
		this.sldTimeline = sldTimeline;
		this.isActive = true;
	}

	@Override
	public void run() {
		while (this.isActive) {
			if (sldTimeline.getValue() < sldTimeline.getMaximum()) {
				sldTimeline.setValue(sldTimeline.getValue() + 1);
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
				}
			} else {
				break;
			}
		}
	}
}
