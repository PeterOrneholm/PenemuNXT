package org.penemunxt.windows.pc;

import java.awt.*;
import javax.swing.*;

public final class WindowUtilities {
    public static void scrollToVisible(JTable table, int rowIndex, int colIndex) {
        Rectangle rect = table.getCellRect(rowIndex, colIndex, true);
        table.scrollRectToVisible(rect);
    }
}
