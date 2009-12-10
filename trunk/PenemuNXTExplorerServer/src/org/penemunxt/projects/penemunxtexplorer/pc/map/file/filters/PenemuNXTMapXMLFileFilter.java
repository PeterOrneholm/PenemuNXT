package org.penemunxt.projects.penemunxtexplorer.pc.map.file.filters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PenemuNXTMapXMLFileFilter extends FileFilter {
	public final static String ALLOWED_FILE_EXTENSION = ".penemunxtmapxml";
	public final static String DESCRIPTION = "PenemuNXT XML Map (*" + ALLOWED_FILE_EXTENSION + ")";

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