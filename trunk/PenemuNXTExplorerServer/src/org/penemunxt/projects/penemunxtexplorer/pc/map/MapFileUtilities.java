package org.penemunxt.projects.penemunxtexplorer.pc.map;

import java.awt.Component;
import java.beans.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.connection.DataShare;

public class MapFileUtilities {
	public static void openData(String filePath, DataShare DS) {
		MapFileUtilities.openData(filePath, DS, null, "");
	}

	public static void openData(String filePath, DataShare DS,
			Object parent, String defaultFolderPath) {
		JFileChooser FC = new JFileChooser(defaultFolderPath);
		FC.addChoosableFileFilter(new PenemuNXTMapXMLFileFilter());
		FC.addChoosableFileFilter(new PenemuNXTMapStreamFileFilter());
		FC.addChoosableFileFilter(new PenemuNXTMapFileFilter());

		if (filePath == null || filePath.length() == 0) {
			int dialogResult;
			if(parent!=null){
				dialogResult = FC.showOpenDialog((Component)parent);
			}else{
				dialogResult = FC.showOpenDialog(null);
			}
			
			if (dialogResult == JFileChooser.APPROVE_OPTION) {
				try {
					filePath = FC.getSelectedFile().getPath();
				} catch (Exception ex) {
					filePath = "";
				}
			} else {
				filePath = "";
			}
		}

		FileInputStream FIS = null;

		if (filePath.length() > 0) {
			XMLDecoder xdec;
			ArrayList<RobotData> OpenedRobotData = null;

			try {
				FIS = new FileInputStream(filePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				OpenedRobotData = null;
			}

			if (FIS != null) {
				if (filePath
						.endsWith(PenemuNXTMapXMLFileFilter.ALLOWED_FILE_EXTENSION)) {
					xdec = new XMLDecoder(FIS);
					OpenedRobotData = (ArrayList<RobotData>) xdec.readObject();
					xdec.close();
				} else {
					try {
						ObjectInputStream objIn;
						objIn = new ObjectInputStream(FIS);
						OpenedRobotData = (ArrayList<RobotData>) objIn
								.readObject();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}

			if (OpenedRobotData != null) {
				for (RobotData robotData : OpenedRobotData) {
					if (robotData == null) {
						OpenedRobotData.remove(robotData);
					}
				}
				if (OpenedRobotData.size() > 0) {
					DS.NXTRobotData = OpenedRobotData;
				}
			}
		}
	}

	public static void saveData(String filePath, DataShare DS) {
		MapFileUtilities.saveData(filePath, DS, null, "");
	}
	
	public static void saveData(String filePath, DataShare DS,
			Object parent, String defaultFolderPath) {
		JFileChooser FC = new JFileChooser(defaultFolderPath);
		FC.addChoosableFileFilter(new PenemuNXTMapXMLFileFilter());
		FC.addChoosableFileFilter(new PenemuNXTMapStreamFileFilter());

		if (filePath == null || filePath.length() == 0) {
			int dialogResult;
			if(parent!=null){
				dialogResult = FC.showSaveDialog((Component)parent);
			}else{
				dialogResult = FC.showSaveDialog(null);
			}
			
			if (dialogResult == JFileChooser.APPROVE_OPTION) {
				try {
					filePath = FC.getSelectedFile().getPath();
				} catch (Exception ex) {
					filePath = "";
				}
			}
		}

		String fileExtension = "";
		if (FC.getFileFilter().getDescription() == PenemuNXTMapXMLFileFilter.DESCRIPTION) {
			fileExtension = PenemuNXTMapXMLFileFilter.ALLOWED_FILE_EXTENSION;
		} else {
			fileExtension = PenemuNXTMapStreamFileFilter.ALLOWED_FILE_EXTENSION;
		}

		if (!filePath.endsWith(fileExtension)) {
			filePath += fileExtension;
		}

		FileOutputStream FOS = null;

		if (filePath.length() > 0 && DS != null && DS.NXTRobotData != null) {
			try {
				FOS = new FileOutputStream(filePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			if (FOS != null) {
				if (filePath
						.endsWith(PenemuNXTMapXMLFileFilter.ALLOWED_FILE_EXTENSION)) {
					XMLEncoder xenc = new XMLEncoder(FOS);
					xenc.writeObject(DS.NXTRobotData);
					xenc.close();
				} else {
					try {
						ObjectOutputStream objOut = new ObjectOutputStream(FOS);
						objOut.writeObject(DS.NXTRobotData);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				try {
					FOS.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
