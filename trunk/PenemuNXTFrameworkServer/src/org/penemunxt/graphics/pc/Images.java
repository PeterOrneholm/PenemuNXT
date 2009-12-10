package org.penemunxt.graphics.pc;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Images {
	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		image = new ImageIcon(image).getImage();
		BufferedImage bImage = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bImage = gc.createCompatibleImage(image.getWidth(null), image
					.getHeight(null));
		} catch (HeadlessException e) {
		}

		if (bImage == null) {
			bImage = new BufferedImage(image.getWidth(null), image
					.getHeight(null), BufferedImage.TYPE_INT_RGB);
		}

		Graphics g = bImage.createGraphics();

		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bImage;
	}
}
