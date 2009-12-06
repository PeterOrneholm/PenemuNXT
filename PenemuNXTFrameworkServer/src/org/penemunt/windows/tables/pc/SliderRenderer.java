package org.penemunt.windows.tables.pc;

import java.awt.Component;

import javax.swing.*;
import javax.swing.table.*;

public class SliderRenderer extends JSlider implements TableCellRenderer {
	int min;
	int max;

	public SliderRenderer(int min, int max) {
		super(SwingConstants.HORIZONTAL);
		this.min = min;
		this.max = max;

		this.setMinimum(min);
		this.setMaximum(max);
		
		setSize(115, 15);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value == null) {
			return this;
		}
		if (value instanceof Integer) {
			int v = (Integer) value;
			setValue(Math.min(Math.max(v, min), max));
		} else {
			setValue(max);
		}
		return this;
	}
}
