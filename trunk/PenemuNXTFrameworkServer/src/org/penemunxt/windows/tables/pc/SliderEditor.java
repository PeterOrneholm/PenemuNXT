package org.penemunxt.windows.tables.pc;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;

public class SliderEditor extends AbstractCellEditor implements TableCellEditor, ChangeListener {
	ArrayList<SliderChangeListener> changeListeners = new ArrayList<SliderChangeListener>();
	
	JSlider sldSlider;
	int min;
	int max;
	
	public SliderEditor(int min, int max, SliderChangeListener changeListener) {
		this.min = min;
		this.max = max;

		sldSlider = new JSlider(SwingConstants.HORIZONTAL);
		sldSlider.setMinimum(min);
		sldSlider.setMaximum(max);
		sldSlider.addChangeListener(this);
		if(changeListener!=null){
			changeListeners.add(changeListener);
		}		
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (value == null) {
			return sldSlider;
		}
		if (value instanceof Integer) {
			int v = (Integer) value;
			sldSlider.setValue(Math.min(Math.max(v, min), max));
		} else {
			sldSlider.setValue(max);
		}
		return sldSlider;
	}

	@Override
	public Object getCellEditorValue() {
		return sldSlider.getValue();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(changeListeners!=null && changeListeners.size() > 0){
			for (SliderChangeListener listener : changeListeners) {
				listener.stateChanged(e);
				listener.stateChanged(e, sldSlider.getValue());
			}
		}
	}
}
