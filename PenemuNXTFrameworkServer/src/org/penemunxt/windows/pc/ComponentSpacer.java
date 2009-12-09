package org.penemunxt.windows.pc;

import java.awt.*;

import javax.swing.*;

public class ComponentSpacer extends Panel {
	JPanel pnlMargin;
	JPanel pnlBorder;
	Panel pnlContainer;

	Component spacerComponent;

	public ComponentSpacer(JComponent spacerComponent, int borderWidth,
			Color borderColor, Color backgroundColor) {
		this(spacerComponent, 0, null, borderWidth, borderColor, backgroundColor);
	}

	public ComponentSpacer(JComponent spacerComponent, int margin, int borderWidth,
			Color borderColor, Color backgroundColor) {
		this(spacerComponent, margin, null, borderWidth, borderColor, backgroundColor);
	}

	
	public ComponentSpacer(JComponent spacerComponent, int margin, Color marginBackground,
			int borderWidth, Color borderColor, Color backgroundColor) {
		super(new BorderLayout());

		this.spacerComponent = spacerComponent;
		createGUI();

		this.setComponentMargin(margin);
		this.setComponentBorder(borderWidth, borderColor);
		this.setComponentBackground(backgroundColor);
		if(marginBackground!=null){
			this.setMarginBackground(marginBackground);	
		}
	}

	private void createGUI() {
		pnlMargin = new JPanel(new BorderLayout());
		pnlBorder = new JPanel(new BorderLayout());
		pnlContainer = new Panel(new BorderLayout());

		pnlContainer.add(spacerComponent, BorderLayout.CENTER);
		pnlBorder.add(pnlContainer, BorderLayout.CENTER);
		pnlMargin.add(pnlBorder, BorderLayout.CENTER);
		this.add(pnlMargin, BorderLayout.CENTER);
	}

	public void setMarginBackground(Color c) {
		this.setBackground(c);
		pnlMargin.setBackground(c);
	}
	
	public void setComponentBackground(Color c) {
		pnlContainer.setBackground(c);
	}

	public void setComponentMargin(int top, int left, int bottom, int right) {
		pnlMargin.setBorder(BorderFactory.createEmptyBorder(top, left, bottom,
				right));
	}

	public void setComponentMargin(int margin) {
		this.setComponentMargin(margin, margin, margin, margin);
	}

	public void setComponentBorder(int width, Color c) {
		pnlBorder.setBorder(BorderFactory.createLineBorder(c, width));
	}
}
