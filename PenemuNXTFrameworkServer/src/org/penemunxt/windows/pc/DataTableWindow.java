package org.penemunxt.windows.pc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public abstract class DataTableWindow extends Thread implements Runnable,
		WindowListener {

	public static int WINDOW_STATE_OPEN = 100;
	public static int WINDOW_STATE_CLOSED = 200;

	public static float WINDOW_WIDTH_PERCENTAGE = 0.5f;
	public static float WINDOW_HEIGHT_PERCENTAGE = 0.25f;

	String ApplicationName;
	Image ApplicationIcon;

	protected Panel panelMain;
	private JTable DataTable;
	JFrame mainFrame;
	int WindowState;

	public int getWindowState() {
		return WindowState;
	}

	public void setWindowState(int windowState) {
		WindowState = windowState;
	}

	@Override
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	public DataTableWindow(String applicationName, Image applicationIcon) {
		this.ApplicationName = applicationName;
		this.ApplicationIcon = applicationIcon;
		this.WindowState = DataTableWindow.WINDOW_STATE_OPEN;
	}

	private void createAndShowGUI() {
		mainFrame = new JFrame(ApplicationName);
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setContentPane(getContentPanel());
		mainFrame.setIconImage(ApplicationIcon);

		Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setSize((int) (ScreenSize.width * WINDOW_WIDTH_PERCENTAGE),
				(int) (ScreenSize.height * WINDOW_HEIGHT_PERCENTAGE));

		mainFrame.setBackground(Color.WHITE);
		mainFrame.setVisible(true);
		mainFrame.addWindowListener(this);
	}

	public Panel getContentPanel() {
		// Panels
		panelMain = new Panel();
		JScrollPane scrollTable = new JScrollPane(getDataTable());

		// Main
		panelMain.setLayout(new BorderLayout());
		panelMain.add(scrollTable, BorderLayout.CENTER);

		return panelMain;
	}

	public void open() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public void focus() {
		if(mainFrame!=null){
			mainFrame.requestFocus();	
		}
	}

	public void refresh(boolean focus) {
		if (focus) {
			focus();
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		setWindowState(DataTableWindow.WINDOW_STATE_CLOSED);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	public void setDataTable(JTable dataTable) {
		DataTable = dataTable;
	}

	public JTable getDataTable() {
		return DataTable;
	}
}