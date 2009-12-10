package org.penemunxt.projects.penemunxtexplorer.pc.map.file;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.beans.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.penemunxt.graphics.pc.Images;
import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.pc.connection.DataShare;
import org.penemunxt.projects.penemunxtexplorer.pc.map.MapVisulaisation;
import org.penemunxt.projects.penemunxtexplorer.pc.map.file.filters.ImageJPEGFileFilter;
import org.penemunxt.projects.penemunxtexplorer.pc.map.file.filters.ImagePNGFileFilter;
import org.penemunxt.projects.penemunxtexplorer.pc.map.file.filters.PenemuNXTMapFileFilter;
import org.penemunxt.projects.penemunxtexplorer.pc.map.file.filters.PenemuNXTMapStreamFileFilter;
import org.penemunxt.projects.penemunxtexplorer.pc.map.file.filters.PenemuNXTMapXMLFileFilter;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.MapProcessors;

public class MapFileUtilities {
	public static void openData(String defaultFolderPath, Object parent,
			DataShare DS) {
		MapFileUtilities.openData(defaultFolderPath, parent, DS, null);
	}

	public static void openData(String defaultFolderPath, Object parent,
			DataShare DS, MapProcessors mapProcessors) {
		String filePath = "";
		JFileChooser FC = new JFileChooser(defaultFolderPath);
		FC.addChoosableFileFilter(new PenemuNXTMapXMLFileFilter());
		FC.addChoosableFileFilter(new PenemuNXTMapStreamFileFilter());
		FC.addChoosableFileFilter(new PenemuNXTMapFileFilter());

		if (mapProcessors != null) {
			FC.setAccessory(new MapFilePreview(FC, mapProcessors));
		}

		int dialogResult;
		if (parent != null) {
			dialogResult = FC.showOpenDialog((Component) parent);
		} else {
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

		MapFileUtilities.openData(filePath, DS);
	}

	public static void openData(String filePath, DataShare DS) {
		FileInputStream FIS = null;

		if (filePath.length() > 0) {
			XMLDecoder xdec;
			ArrayList<RobotData> OpenedRobotData = null;
			File f = new File(filePath);

			if (f.exists()) {
				try {
					FIS = new FileInputStream(filePath);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					FIS = null;
					OpenedRobotData = null;
				}

				if (FIS != null) {
					if (filePath
							.endsWith(PenemuNXTMapXMLFileFilter.ALLOWED_FILE_EXTENSION)) {
						xdec = new XMLDecoder(FIS);
						OpenedRobotData = (ArrayList<RobotData>) xdec
								.readObject();
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
	}

	public static void saveData(String defaultFolderPath, Object parent,
			DataShare DS) {
		String filePath = "";
		JFileChooser FC = new JFileChooser(defaultFolderPath);
		FC.addChoosableFileFilter(new PenemuNXTMapXMLFileFilter());
		FC.addChoosableFileFilter(new PenemuNXTMapStreamFileFilter());

		if (filePath == null || filePath.length() == 0) {
			int dialogResult;
			if (parent != null) {
				dialogResult = FC.showSaveDialog((Component) parent);
			} else {
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

		MapFileUtilities.saveData(filePath, DS);
	}

	public static void saveData(String filePath, DataShare DS) {
		FileOutputStream FOS = null;

		if (filePath.length() > 0 && DS != null && DS.NXTRobotData != null) {
			try {
				FOS = new FileOutputStream(filePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				FOS = null;
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

	public static void saveRenderedMap(String defaultFolderPath, Object parent,
			MapVisulaisation map) {
		String filePath = "";
		JFileChooser FC = new JFileChooser(defaultFolderPath);
		FC.addChoosableFileFilter(new ImageJPEGFileFilter());
		FC.addChoosableFileFilter(new ImagePNGFileFilter());

		if (filePath == null || filePath.length() == 0) {
			int dialogResult;
			if (parent != null) {
				dialogResult = FC.showSaveDialog((Component) parent);
			} else {
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
		String imageFormat = "";
		if (FC.getFileFilter().getDescription() == ImageJPEGFileFilter.DESCRIPTION) {
			fileExtension = ImageJPEGFileFilter.ALLOWED_FILE_EXTENSION;
			imageFormat = ImageJPEGFileFilter.IMAGE_FORMAT_NAME;
		} else {
			fileExtension = ImagePNGFileFilter.ALLOWED_FILE_EXTENSION;
			imageFormat = ImagePNGFileFilter.IMAGE_FORMAT_NAME;
		}

		if (!filePath.endsWith(fileExtension)) {
			filePath += fileExtension;
		}

		MapFileUtilities.saveRenderedMap(map, imageFormat, filePath);
	}

	public static void saveRenderedMap(MapVisulaisation map,
			String imageFormat, String filePath) {
		Image mapImage = map.getMapImage();
		if (mapImage != null) {
			try {
				ImageIO.write((RenderedImage) mapImage, imageFormat, new File(
						filePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
