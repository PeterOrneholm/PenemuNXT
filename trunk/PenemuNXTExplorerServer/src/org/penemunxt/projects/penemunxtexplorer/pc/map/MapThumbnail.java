package org.penemunxt.projects.penemunxtexplorer.pc.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Panel;
import javax.swing.*;

import org.penemunxt.windows.pc.ComponentSpacer;

public class MapThumbnail extends Thread implements Runnable {
	final static Color MAP_PANEL_BACKGROUND_COLOR = new Color(227, 227, 227);
	final static Color MAP_PANEL_BORDER_COLOR = Color.BLACK;
	final static int MAP_PANEL_BORDER_WIDTH = 2;
	final static int MAP_PANEL_MARGIN = 0;

	public JWindow mainWindow;
	MapVisulaisation mapVisulaisation;
	JPanel pnlMapVisulaisationThumbnailWrapper;

	public MapThumbnail() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	@Override
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	private void createAndShowGUI() {
		mainWindow = new JWindow();
		mainWindow.setAlwaysOnTop(true);
		mainWindow.setContentPane(getContentPanel());

		mainWindow.setSize(200, 200);

		mainWindow.setBackground(Color.WHITE);
		mainWindow.setVisible(false);
		mainWindow.pack();
	}

	public Panel getContentPanel() {
		// Panels
		Panel panelMain = new Panel();

		// Main
		panelMain.setLayout(new BorderLayout());
		panelMain.add(getThumbnailPanel(), BorderLayout.CENTER);

		return panelMain;
	}

	public JPanel getThumbnailPanel() {
		pnlMapVisulaisationThumbnailWrapper = new JPanel(new BorderLayout());

		return pnlMapVisulaisationThumbnailWrapper;
	}

	public boolean isOpen() {
		return mainWindow.isVisible();
	}

	public void open(MapVisulaisation mapVisulaisation, int width, int height) {
		this.mapVisulaisation = mapVisulaisation;
		setSize(width, height);
		show();
	}

	private void show() {
		pnlMapVisulaisationThumbnailWrapper.removeAll();
		pnlMapVisulaisationThumbnailWrapper.add(new ComponentSpacer(
				mapVisulaisation, MAP_PANEL_MARGIN, MAP_PANEL_BORDER_WIDTH,
				MAP_PANEL_BORDER_COLOR, MAP_PANEL_BACKGROUND_COLOR),
				BorderLayout.CENTER);
		mainWindow.setVisible(true);
		mapVisulaisation.refresh();
	}

	public void hide() {
		mainWindow.setVisible(false);
	}

	public void move(int x, int y) {
		mainWindow.setBounds(x, y, mainWindow.getWidth(), mainWindow
				.getHeight());
	}

	public void setFrame(int frame) {
		mapVisulaisation.setMapCurrentFrame(frame);
	}

	public void setScale(int scale) {
		mapVisulaisation.setMapScale(scale);
	}

	public void setSize(int width, int height) {
		mainWindow.setBounds(mainWindow.getX(), mainWindow.getY(), width,
				height);
	}

	public int getWidth() {
		return mainWindow.getWidth();
	}

	public int getHeight() {
		return mainWindow.getHeight();
	}
}
