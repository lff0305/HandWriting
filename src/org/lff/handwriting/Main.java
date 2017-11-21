package org.lff.handwriting;

import java.awt.*;

/**
 * @author Feifei Liu
 * @datetime 2017-11-15  12:04
 */
public class Main {
    public static void main(String[] argu) {
        MainDialog dlg = MainDialog.getInstance();
        dlg.setSize(800, 600);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dlg.setLocation((int)((dim.width / 2 - dlg.getSize().width / 2) * 0.6), dim.height / 2 - dlg.getSize().height / 2);
        dlg.setVisible(true);
    }
}
