package org.lff.handwriting;

import org.lff.handwriting.util.FontUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.lang.invoke.MethodHandles;

/**
 * @author Feifei Liu
 * @datetime 2017-11-17  10:16
 */
public class PreviewPanel extends JPanel {

    private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final long serialVersionUID = -4929347747026022144L;
    private final JScrollPane scrollPane;
    private Option option;

    public PreviewPanel(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    private String text;

    private int rowCount = 12;
    int leftOffset = 36;
    int rightOffset = 36;
    int right = 570;
    int topOffset = 36;
    int bottomOffset = 36;
    int rowGap = 36;
    int cellHeight = 12;


    public void updateText(String text) {
        this.text = text;
    }

    public void updateOption(Option option) {
        this.option = option;
    }
    public void updateRowCount(int rowCount) {
        this.rowCount = rowCount;
        right = this.getWidth() - this.rightOffset;
    }

    public void updateSize() {
        logger.info("Current height is {}", this.getHeight());
        int height = topOffset + bottomOffset + cellHeight * 4 * rowCount + rowGap * ( rowCount - 1);
        logger.info("Set new width {}", height);
        setPreferredSize(new Dimension(this.getWidth(), height));
    }

    public void paintComponent(Graphics g){

        if (rowCount == 0) {
            return;
        }
        if (text == null) {
            return;
        }

        super.paintComponent(g);

        //creates a copy of the Graphics instance
        Graphics2D g2d = (Graphics2D) g.create();

        Stroke stroke = getLineStroke();
        g2d.setStroke(stroke);

        Font font = FontUtil.getFont(option.getFontName());
        float ratio = FontUtil.getRatio(option.getFontName());

        logger.info("Font info : {} {}", option.getFontName(), ratio);

        Font sizedFont = font.deriveFont(cellHeight * ratio);

        java.util.List<String> lines = LinesUtil.build(text, option);

        String wrap = "";

        for (int i = 0; i < rowCount && i < lines.size(); i++) {
            int h = topOffset + (cellHeight * 4 + rowGap) * i;
            g2d.setColor(ColorUtil.getColor(option.getLineColor()));
            g2d.drawLine(leftOffset, h, right, h);
            g2d.drawLine(leftOffset, h + cellHeight, right, h + cellHeight);
            g2d.drawLine(leftOffset, h + 2 * cellHeight, right, h + 2 * cellHeight);
            g2d.drawLine(leftOffset, h + 3 * cellHeight, right, h + 3 * cellHeight);;

            g2d.setColor(ColorUtil.getColor(option.getTextColor()));
            g2d.setFont(sizedFont);
            String line = lines.get(i);
            line = line.trim();
            if (!wrap.isEmpty()) {
                line = wrap + line;
            }
            wrap = "";
            String[] words = line.split("( )+");
            int c = 0;
            for (int j=0; j<words.length; j++) {
                String word = words[j];
                word = word.trim();
                AffineTransform affinetransform = new AffineTransform();
                FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
                int textwidth = (int)(sizedFont.getStringBounds(word + " ", frc).getWidth());
                if (leftOffset + c + textwidth > right) {
                    wrap += word;
                    wrap += " ";
                    continue;
                }
                g2d.drawString(word, leftOffset + c, h + cellHeight * 2);
                c += textwidth;
            }
        }

        g2d.dispose();
    }

    private Stroke getLineStroke() {
        Stroke stroke = null;
        if (option.getLineStyle() == LineStyle.DASH) {
            stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6, 1}, 0);
        }
        if (option.getLineStyle() == LineStyle.SOLID) {
            stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, null, 0);
        }
        return stroke;
    }
}
