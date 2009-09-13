package org.penemunxt.graphics.pc;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public class Graph {
	VolatileImage VI;

	public void drawGraph(ArrayList<Integer> values, int maxValue,
			String valueUnit, Rectangle2D.Float bounds, Graphics g) {
		this.drawGraph(values, maxValue, valueUnit, Color.BLACK, bounds, false,
				g);
	}

	public void drawGraph(ArrayList<Integer> values, int maxValue,
			String valueUnit, Color strokeColor, Rectangle2D.Float bounds,
			boolean drawBox, Graphics g) {
		this.drawGraph(values, maxValue, 3, valueUnit, strokeColor, 3,
				new Font("Arial", Font.PLAIN, 28), bounds, drawBox, 80, g);
	}

	public void drawGraph(ArrayList<Integer> values, int maxValue,
			int xMultiplier, String valueUnit, Color strokeColor,
			int strokeWidth, Font valueFont, Rectangle2D.Float bounds,
			boolean drawBox, int scrollAtPercentage, Graphics g) {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc = ge.getDefaultScreenDevice()
				.getDefaultConfiguration();

		if (VI == null || VI.getWidth() != bounds.getWidth()
				|| VI.getHeight() != bounds.getHeight()) {
			VI = gc.createCompatibleVolatileImage((int) bounds.getWidth(),
					(int) bounds.getHeight());
		}

		Graphics2D g2 = (Graphics2D) VI.getGraphics();
		g2.clearRect(0, 0, VI.getWidth(), VI.getHeight());
		
		int xStart = 0;

		if ((values.size() * xMultiplier) >= (bounds.getWidth() * (scrollAtPercentage / 100f))) {
			xStart += (int) -((values.size() * xMultiplier) - (bounds
					.getWidth() * (scrollAtPercentage / 100f)));
		}

		for (int i = 0; i < values.size(); i++) {
			if (i > 0) {
				g2.setColor(strokeColor);
				g2.setStroke(new BasicStroke(strokeWidth));

				int x1 = xStart + ((i - 1) * xMultiplier);
				int x2 = xStart + (i * xMultiplier);

				int y1 = (int) (bounds.getHeight() - (int) ((values.get(i - 1) / (float) maxValue) * bounds
						.getHeight()));
				int y2 = (int) (bounds.getHeight() - (int) ((values.get(i) / (float) maxValue) * bounds
						.getHeight()));

				g2.drawLine(x1, y1, x2, y2);
				if (i == (values.size() - 1)) {
					g2.setFont(valueFont);
					int C = (int) ((values.get(i) / (float) maxValue) * 255);
					g2.setColor(new Color(C, C, 255 - C));
					g2.drawString(values.get(i) + valueUnit, x2 + 10, y2);
				}
			}
		}

		if (drawBox) {
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(1));
			g2.drawRect((int) 0, 0, (int) bounds.getWidth(), (int) bounds
					.getHeight());
		}
		g.drawImage(VI, (int) bounds.getX(), (int) bounds.getY(), null);
	}
}
