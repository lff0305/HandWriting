package org.lff.handwriting;

import javax.swing.*; /**
 * @author Feifei Liu
 * @datetime 2017-11-17  10:24
 */
public class PreviewUtil {
    public static void preview(JScrollPane scrollPane, JPanel previewPanel, String text, Option option) {
        //Update
        PreviewPanel pp = (PreviewPanel) previewPanel;
        pp.updateOption(option);
        pp.updateText(text);
        pp.updateRowCount(12);
        pp.updateSize();
        scrollPane.setViewportView(pp);
        pp.invalidate();
        pp.repaint();
    }
}
