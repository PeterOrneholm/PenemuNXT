package org.penemunxt.projects.penemunxtexplorer.pc.map.timeline;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JSlider;

public class MapTimelinePlayer extends Thread implements Runnable {
	private boolean isActive;
	private int fps;
	private JSlider sldTimeline;

	ArrayList<ActionListener> dataEndListeners = new ArrayList<ActionListener>();
	
	// Listeners
	public synchronized void addDataEndListeners(ActionListener listener) {
		dataEndListeners.add(listener);
	}

	public synchronized void removeDataEndListeners(ActionListener listener) {
		dataEndListeners.remove(listener);
	}
	
	private void triggerDataEnd() {
		if (dataEndListeners != null && dataEndListeners.size() > 0) {
			for (ActionListener listener : dataEndListeners) {
				listener.actionPerformed(new ActionEvent(this, 0, ""));
			}
		}
	}
	
	public int getFPS() {
		return fps;
	}

	public void setFPS(int fps) {
		this.fps = fps;
	}

	public void deactivate() {
		this.isActive = false;
	}

	public MapTimelinePlayer(JSlider sldTimeline, int fps) {
		super();
		this.fps = fps;
		this.sldTimeline = sldTimeline;
		this.isActive = true;
	}

	@Override
	public void run() {
		while (this.isActive) {
			if (sldTimeline.getValue() < sldTimeline.getMaximum()) {
				sldTimeline.setValue(sldTimeline.getValue() + 1);
				try {
					Thread.sleep((int) (1000f/fps));
					Thread.yield();
				} catch (InterruptedException e) {
				}
			} else {
				triggerDataEnd();
				break;
			}
		}
	}
}
