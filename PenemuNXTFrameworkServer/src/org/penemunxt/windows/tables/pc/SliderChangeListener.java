package org.penemunxt.windows.tables.pc;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public interface SliderChangeListener extends ChangeListener {
	public void stateChanged(ChangeEvent e, int value);
}
