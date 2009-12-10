package org.penemunxt.projects.penemunxtexplorer.pc.map.file.filters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageJPEGFileFilter extends FileFilter {
	public final static String ALLOWED_FILE_EXTENSION = ".jpg";
	public final static String DESCRIPTION = "JPEG Image (*" + ALLOWED_FILE_EXTENSION + ")";
	public final static String IMAGE_FORMAT_NAME = "JPEG";
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		if (f.getName().endsWith(ALLOWED_FILE_EXTENSION)) {
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
}