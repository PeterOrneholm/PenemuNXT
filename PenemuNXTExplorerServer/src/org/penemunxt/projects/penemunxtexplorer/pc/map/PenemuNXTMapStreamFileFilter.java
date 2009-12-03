package org.penemunxt.projects.penemunxtexplorer.pc.map;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PenemuNXTMapStreamFileFilter extends FileFilter {
	public final static String ALLOWED_FILE_EXTENSION = ".penemunxtmap";
	public final static String DESCRIPTION = "PenemuNXT Stream Map (*" + ALLOWED_FILE_EXTENSION + ")";

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