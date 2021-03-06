package org.lff.handwriting;

import net.miginfocom.swing.MigLayout;
import org.lff.handwriting.util.FontUtil;

import javax.swing.*;
import java.awt.*;

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
    private JLabel label5 = new JLabel("Font Name");
    private JLabel label6 = new JLabel("Text Size ratio");
    private JComboBox cmbRowsCount = new JComboBox();
    private JComboBox cmbFont = new JComboBox();
    private JComboBox cmbFontColor = new JComboBox();
    private JComboBox cmbLineColor = new JComboBox();
    private JComboBox cmbLineStyle = new JComboBox();
    private JTextField edtSizeRatio = new JTextField();
    private JCheckBox chkEmptyLine = new JCheckBox("Add an empty line for each line");
    private JCheckBox chkSkipEmptyLine = new JCheckBox("Skip empty lines");
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
        ColorUtil.iterateColors( c -> {
            cmbFontColor.addItem(c);
        });

        this.add(label5);
        loadFonts(cmbFont);
        this.add(cmbFont);

        this.add(label6);
        this.edtSizeRatio.setPreferredSize(new Dimension(64, 18));
        this.edtSizeRatio.setText("1.7");
        this.add(edtSizeRatio);

        FontUtil.iterateFonts(c -> {
            cmbFont.addItem(c);
        });

        cmbFontColor.setSelectedItem("MAGENTA");
        this.add(label2); // Wrap to next row
        this.add(cmbFontColor);

        this.add(label3);
        this.add(cmbLineColor);
        ColorUtil.iterateColors( c -> {
            cmbLineColor.addItem(c);
        });
        cmbLineColor.setSelectedItem("BLACK");
        this.add(label4);
        this.add(cmbLineStyle);
        cmbLineStyle.addItem(" Dash ");
        cmbLineStyle.addItem(" Solid ");

        this.add(chkEmptyLine, "span 2");
        this.add(chkSkipEmptyLine, "span 2");

        cmbFont.addActionListener(l -> {
            JComboBox v = (JComboBox)l.getSource();
            float r = FontUtil.getRatio((String)v.getSelectedItem());
            this.edtSizeRatio.setText(String.valueOf(r));
        });

    }

    private void loadFonts(JComboBox cmbFont) {

    }

    public boolean isSkipEmptyLine() {
        return this.chkSkipEmptyLine.isSelected();
    }

    public boolean isAddEmptyLine() {
        return this.chkEmptyLine.isSelected();
    }

    public String getLineColor() {
        return (String)this.cmbLineColor.getSelectedItem();
    }

    public LineStyle getLineStype() {
        String v = (String)this.cmbLineStyle.getSelectedItem();
        v = v.trim();
        if (v.equalsIgnoreCase("SOLID")) {
            return LineStyle.SOLID;
        }

        return LineStyle.DASH;
    }

    public String getTextColor() {
        String v = (String)this.cmbFontColor.getSelectedItem();
        return v.trim();
    }

    public String getFontName() {
        String v = (String) cmbFont.getSelectedItem();
        return v.trim();
    }
}
