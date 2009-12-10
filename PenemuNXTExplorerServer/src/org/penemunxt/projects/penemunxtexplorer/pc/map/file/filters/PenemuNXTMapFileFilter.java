package org.penemunxt.projects.penemunxtexplorer.pc.map.file.filters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PenemuNXTMapFileFilter extends FileFilter {
	public final static String DESCRIPTION = "All PenemuNXT Maps (*" + PenemuNXTMapStreamFileFilter.ALLOWED_FILE_EXTENSION + "; *" + PenemuNXTMapXMLFileFilter.ALLOWED_FILE_EXTENSION + ")";
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		if (f.getName().endsWith(PenemuNXTMapStreamFileFilter.ALLOWED_FILE_EXTENSION) || f.getName().endsWith(PenemuNXTMapXMLFileFilter.ALLOWED_FILE_EXTENSION)) {
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
}