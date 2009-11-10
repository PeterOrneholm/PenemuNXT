package org.penemunxt.graphics.pc;

import java.net.URL;

import javax.swing.ImageIcon;

public class Icons {
	public final static String PENEMUNXT_CIRCLE_LOGO_ICON_16_X_16_NAME = "PenemuNXT_Circle_Logo_Icon_16x16.png";
	public final static String PENEMUNXT_LOGO_PORTRAIT_NAME = "PenemuNXT_Logo_Portrait.png";
	public final static String PENEMUNXT_LOGO_LANDSCAPE_NAME = "PenemuNXT_Logo_Landscape.png";

	public final static URL PENEMUNXT_CIRCLE_LOGO_ICON_16_X_16_URL = Icons.class
			.getResource(PENEMUNXT_CIRCLE_LOGO_ICON_16_X_16_NAME);
	public final static URL PENEMUNXT_LOGO_PORTRAIT_URL = Icons.class
			.getResource(PENEMUNXT_LOGO_PORTRAIT_NAME);
	public final static URL PENEMUNXT_LOGO_LANDSCAPE_URL = Icons.class
	.getResource(PENEMUNXT_LOGO_LANDSCAPE_NAME);
	
	public final static ImageIcon PENEMUNXT_CIRCLE_LOGO_ICON_16_X_16_ICON = new ImageIcon(
			PENEMUNXT_CIRCLE_LOGO_ICON_16_X_16_URL);
	public final static ImageIcon PENEMUNXT_LOGO_PORTRAIT_ICON = new ImageIcon(
			PENEMUNXT_LOGO_PORTRAIT_URL);
	public final static ImageIcon PENEMUNXT_LOGO_LANDSCAPE_ICON = new ImageIcon(
			PENEMUNXT_LOGO_LANDSCAPE_URL);
}
