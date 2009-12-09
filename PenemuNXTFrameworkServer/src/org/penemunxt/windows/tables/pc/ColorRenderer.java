package org.penemunxt.windows.tables.pc;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class ColorRenderer extends JLabel implements TableCellRenderer {
	public ColorRenderer() {
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table,
			Object color, boolean isSelected, boolean hasFocus, int row,
			int column) {
		Color newColor = (Color) color;
		setBackground(newColor);
		return this;
	}
}