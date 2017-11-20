package org.lff.handwriting;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * @author Feifei Liu
 * @datetime 2017-11-15  12:17
 */
public class ConfigPanel extends JPanel {
    private static final long serialVersionUID = -8231175780066678247L;
    private JLabel label1 = new JLabel("Rows count");
    private JLabel label2 = new JLabel("Font color");
    private JLabel label3 = new JLabel("Line color");
    private JLabel label4 = new JLabel("Line style");
    private JComboBox cmbRowsCount = new JComboBox();
    private JComboBox cmbFontColor = new JComboBox();
    private JComboBox cmbLineColor = new JComboBox();
    private JComboBox cmbLineStyle = new JComboBox();
    private MigLayout layout = new MigLayout("wrap 2");

    public ConfigPanel() {
        this.setLayout(layout);
        init();
    }

    private void init() {
        label1.setSize(60, 12);
        this.add(label1);
        cmbRowsCount.addItem(" 12 ");
        cmbRowsCount.addItem(" 13 ");
        this.add(cmbRowsCount);
        cmbFontColor.addItem(" Black ");
        cmbFontColor.addItem(" Red ");
        this.add(label2); // Wrap to next row
        this.add(cmbFontColor);

        this.add(label3);
        this.add(cmbLineColor);
        cmbLineColor.addItem(" Pink ");
        cmbLineColor.addItem(" Black ");
        cmbLineColor.addItem(" Gray ");
        this.add(label4);
        this.add(cmbLineStyle);
        cmbLineStyle.addItem(" Dash ");
        cmbLineStyle.addItem(" Solid ");
    }
}
