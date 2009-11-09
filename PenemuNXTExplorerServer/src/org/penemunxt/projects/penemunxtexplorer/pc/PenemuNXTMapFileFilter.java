package org.penemunxt.projects.penemunxtexplorer.pc;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PenemuNXTMapFileFilter extends FileFilter {
	public final static String ALLOWED_FILE_EXTENSION = ".penemunxtmap";

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
		return "PenemuNXT Map (*" + ALLOWED_FILE_EXTENSION + ")";
	}
}