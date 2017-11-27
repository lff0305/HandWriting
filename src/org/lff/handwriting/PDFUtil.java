package org.lff.handwriting;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import org.lff.handwriting.util.FontUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Created by liuff on 2017/4/20 9:39
 */
public class PDFUtil {

    private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static byte[] getContent(String text, Option option) throws IOException {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(bas));

        try {
            Color lineColor = buildLineColor(option);
            Color textColor = buildTextColor(option);

            List<String> lines = LinesUtil.build(text, option);

            final byte[] fontBuf = FontUtil.getFontBuffer(option.getFontName());
            final float ratio = FontUtil.getRatio(option.getFontName());
            logger.info("Font loaded. name= {}, size = {}", option.getFontName(), fontBuf.length);

            int pageCount = getPageCount(lines, option.getRowCount());

            String wrapped = "";

            for (int i = 0; i < pageCount || !wrapped.isEmpty(); i++) {

                PdfPage page = pdfDoc.addNewPage();
                PdfCanvas canvas = new PdfCanvas(page);

                int width = (int) page.getPageSize().getWidth();
                int height = (int) page.getPageSize().getHeight();


                int contentSize = height - option.getTopOffset() - option.getBottomOffset();
                float cellHeight = (float) ((contentSize + option.getRowGap() - option.getRowCount() * option.getRowGap()) / (4.0 * option.getRowCount()));
                option.setCellHeight(cellHeight);

                logger.info("Width = {} Height = {} rowCount = {}", width, height, option.getRowCount());
                logger.info("CellHeight = {}, Gap Size = {} ", option.getCellHeight(), option.getRowGap());
                logger.info("TopOffset = {}, BottomOffset = {}", option.getTopOffset(), option.getBottomOffset());

                for (int j = 0; j < option.getRowCount(); j++) {
                    String line = "";
                    int index = i * option.getRowCount() + j;
                    if (index < lines.size()) {
                        line = lines.get(index);
                    }

                    logger.info("Get line = {}, wrapped = {}", line, wrapped);

                    line = wrapped + line;
                    wrapped = "";

                    float h = option.getTopOffset() + (option.getCellHeight() * 4 + option.getRowGap()) * j;


                    applyLineStyle(option, canvas);

                    canvas.setStrokeColor(lineColor)
                            .setLineWidth(0.4f)
                            .moveTo(option.getLeftOffset(), height - (h))
                            .lineTo(width - option.getRightOffset(), height - (h))
                            .closePathStroke();
                    canvas.setStrokeColor(lineColor)
                            .setLineWidth(0.4f)
                            .moveTo(option.getLeftOffset(), height - (option.getCellHeight() + h))
                            .lineTo(width - option.getRightOffset(), height - (option.getCellHeight() + h))
                            .closePathStroke();
                    canvas.setStrokeColor(lineColor)
                            .setLineWidth(0.4f)
                            .moveTo(option.getLeftOffset(), height - (2 * option.getCellHeight() + h))
                            .lineTo(width - option.getRightOffset(), height - (2 * option.getCellHeight() + h))
                            .closePathStroke();
                    canvas.setStrokeColor(lineColor)
                            .setLineWidth(0.4f)
                            .moveTo(option.getLeftOffset(), height - (3 * option.getCellHeight() + h))
                            .lineTo(width - option.getRightOffset(), height - (3 * option.getCellHeight() + h))
                            .closePathStroke();

                    if (line != null) {
                        line = line.trim();
                        String[] words = line.split("( )+");
                        int c = 0;
                        for (int k = 0; k < words.length; k++) {
                            String word = words[k];
                            word = word.trim();
                            PdfFont font = PdfFontFactory.createFont(fontBuf, PdfEncodings.CP1250, true);
                            canvas.setFontAndSize(font, option.getCellHeight() * ratio);
                            float wordWidth = font.getWidth(word + " ", option.getCellHeight() * ratio);
                            if (option.getLeftOffset() + c + wordWidth > width - option.getRightOffset()) {
                                wrapped += word;
                                wrapped += " ";
                                continue;
                            }
                            canvas.setColor(textColor, true);
                            canvas.beginText();
                            canvas.moveText(option.getLeftOffset() + c, height - (h + cellHeight * 2));
                            canvas.showText(word);
                            canvas.endText();
                            c += wordWidth;
                        }
                    }
                }
            }
        } finally {
            pdfDoc.close();
        }

        return bas.toByteArray();
    }

    private static Color buildTextColor(Option option) {
        return buildColor(option.getTextColor());
    }

    private static void applyLineStyle(Option option, PdfCanvas canvas) {
        if (option.getLineStyle() == LineStyle.DASH) {
             canvas.setLineDash(3, 3, 0.5f);
        }
        if (option.getLineStyle() == LineStyle.SOLID) {
        }
    }

    private static Color buildLineColor(Option option) {
        return buildColor(option.getLineColor());
    }

    private static Color buildColor(String colorName) {
        java.awt.Color color = ColorUtil.getColor(colorName);
        return new DeviceRgb(color.getRed(), color.getGreen(), color.getBlue());
    }

    private static int getPageCount(List<String> lines, int rowCount) {
        int pageCount = lines.size() / rowCount;
        if (lines.size() % rowCount != 0) {
            pageCount ++;
        }
        return pageCount;
    }
}