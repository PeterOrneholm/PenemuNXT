package org.penemunxt.windows.tables.pc;

import java.awt.Component;

import javax.swing.*;
import javax.swing.table.*;

public class SliderEditor extends AbstractCellEditor implements TableCellEditor {
	JSlider sldSlider;
	int min;
	int max;

	public SliderEditor(int min, int max) {
		this.min = min;
		this.max = max;

		sldSlider = new JSlider(SwingConstants.HORIZONTAL);
		sldSlider.setMinimum(min);
		sldSlider.setMaximum(max);
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
}
