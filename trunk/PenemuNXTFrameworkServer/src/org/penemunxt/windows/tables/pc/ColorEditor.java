package org.penemunxt.windows.tables.pc;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

public class ColorEditor extends AbstractCellEditor implements TableCellEditor,
		ActionListener {
	Color currentColor;
	JButton button;
	JColorChooser colorChooser;
	JDialog dialog;
	protected static final String EDIT = "edit";

	public ColorEditor() {
		button = new JButton();
		button.setActionCommand(EDIT);
		button.addActionListener(this);
		button.setBorderPainted(false);

		Color initColor = null;
		try{
			initColor = (Color) this.getCellEditorValue();
		}catch(Exception e){
			initColor = null;
		}
		
		if(initColor!=null){
			colorChooser = new JColorChooser(initColor);
		}else{
			colorChooser = new JColorChooser();
		}
		
		colorChooser.setPreviewPanel(new JPanel());
		dialog = JColorChooser.createDialog(button, "Pick a Color", true,
				colorChooser, this, null);
	}

	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
			button.setBackground(currentColor);
			colorChooser.setColor(currentColor);
			dialog.setVisible(true);

			fireEditingStopped();
		} else {
			currentColor = colorChooser.getColor();
		}
	}

	public Object getCellEditorValue() {
		return currentColor;
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		currentColor = (Color) value;
		return button;
	}
}