package org.penemunxt.graphics.pc;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Graph {
	VolatileImage VI;

	private static float getPercentage(int value, int maxValue, int minValue) {
		return (Math
				.max(
						0,
						Math
								.min(
										100f,
										100f * ((float) (value - minValue) / (float) (maxValue - minValue))))) / 100f;

	}

	public void drawGraph(ArrayList<Integer> values, int minValue,
			int maxValue, String valueUnit, Rectangle bounds, Graphics g) {
		this.drawGraph(values, minValue, maxValue, valueUnit, Color.BLACK,
				bounds, false, g);
	}

	public void drawGraph(ArrayList<Integer> values, int minValue,
			int maxValue, String valueUnit, Color strokeColor,
			Rectangle bounds, boolean drawBox, Graphics g) {
		this.drawGraph(values, minValue, maxValue, 3, valueUnit, strokeColor,
				3, new Font("Arial", Font.PLAIN, 28), bounds, drawBox, 80, g);
	}

	public void drawGraph(ArrayList<Integer> values, int minValue,
			int maxValue, int xMultiplier, String valueUnit, Color strokeColor,
			int strokeWidth, Font valueFont, Rectangle bounds, boolean drawBox,
			int scrollAtPercentage, Graphics g) {
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
				float lastValuePercentage = getPercentage(values.get(i - 1),
						minValue, maxValue);
				float curValuePercentage = getPercentage(values.get(i),
						minValue, maxValue);

				g2.setColor(strokeColor);
				g2.setStroke(new BasicStroke(strokeWidth));

				int x1 = xStart + ((i - 1) * xMultiplier);
				int x2 = xStart + (i * xMultiplier);

				System.out.println(lastValuePercentage);

				int y1 = (int) (lastValuePercentage * bounds.getHeight());
				int y2 = (int) (curValuePercentage * bounds.getHeight());

				g2.drawLine(x1, y1, x2, y2);
				if (i == (values.size() - 1)) {
					g2.setFont(valueFont);
					int C = (int) (curValuePercentage * 255);
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
