package org.lff.handwriting;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceCmyk;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuff on 2017/4/20 9:39
 */
public class PDFUtil {

    private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int SIZE_LIMIT = 40;

    public static final InputStream FONT = PDFUtil.class.getResourceAsStream("/font/JARDOTTY.ttf");

    public static byte[] getContent(String text, Option option) throws IOException {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(bas));
        Color magentaColor = new DeviceCmyk(0.f, 1.f, 0.f, 0.f);

        List<String> lines = LinesUtil.build(text);

        final byte[] fontBuf = getFontBuffer();

        logger.info("Font loaded. size = {}", fontBuf.length);

        int pageCount = getPageCount(lines, option.getRowCount());

        for (int i=0; i<pageCount; i++) {

            PdfPage page = pdfDoc.addNewPage();
            PdfCanvas canvas = new PdfCanvas(page);
            int width = (int) page.getPageSize().getWidth();
            int height = (int) page.getPageSize().getHeight();


            int contentSize = height - option.getTopOffset() - option.getBottomOffset();
            float cellHeight = (float)((contentSize + option.getRowGap() - option.getRowCount() * option.getRowGap()) / ( 4.0 * option.getRowCount()));
            option.setCellHeight(cellHeight);

            logger.info("Width = {} Height = {} rowCount = {}", width, height, option.getRowCount());
            logger.info("CellHeight = {}, Gap Size = {} ", option.getCellHeight(), option.getRowGap());
            logger.info("TopOffset = {}, BottomOffset = {}", option.getTopOffset(), option.getBottomOffset());

            for (int j=0; j<option.getRowCount(); j++) {
                String line = "";
                int index = i * option.getRowCount() + j;
                if (index < lines.size()) {
                    line = lines.get(i);
                }

                float h = option.getTopOffset() + (option.getCellHeight() * 4 + option.getRowGap()) * j;

                canvas.setStrokeColor(magentaColor)
                        .setLineDash(3, 3, 0.5f)
                        .setLineWidth(0.4f)
                        .moveTo(option.getLeftOffset(), height - (h))
                        .lineTo(width - option.getRightOffset(), height - (h))
                        .closePathStroke();
                canvas.setStrokeColor(magentaColor)
                        .setLineDash(3, 3, 0.5f)
                        .setLineWidth(0.4f)
                        .moveTo(option.getLeftOffset(), height - (option.getCellHeight() + h))
                        .lineTo(width - option.getRightOffset(), height - (option.getCellHeight() + h))
                        .closePathStroke();
                canvas.setStrokeColor(magentaColor)
                        .setLineDash(3, 3, 0.5f)
                        .setLineWidth(0.4f)
                        .moveTo(option.getLeftOffset(),  height - (2 * option.getCellHeight() + h))
                        .lineTo(width - option.getRightOffset(),  height - (2 * option.getCellHeight() + h))
                        .closePathStroke();
                canvas.setStrokeColor(magentaColor)
                        .setLineDash(3, 3, 0.5f)
                        .setLineWidth(0.4f)
                        .moveTo(option.getLeftOffset(), height - (3 * option.getCellHeight() + h))
                        .lineTo(width - option.getRightOffset(),  height - (3 * option.getCellHeight() + h))
                        .closePathStroke();



                if (line != null) {
                    line = line.trim();
                    String[] words = line.split("( )+");
                    int c = 0;
                    for (int k=0; k<words.length; k++) {
                        String word = words[k];
                        word = word.trim();
                        canvas.beginText();
                        canvas.moveText(option.getLeftOffset() + c, height - (h + cellHeight * 2));
                        PdfFont font = PdfFontFactory.createFont(fontBuf, PdfEncodings.CP1250, true);
                        canvas.setFontAndSize(font, option.getCellHeight() * 1.7f);
                        canvas.showText(word);
                        canvas.endText();
                        c += font.getWidth(word + " ", option.getCellHeight() * 1.7f);
                    }
                }


            }

        }

        pdfDoc.close();

        return bas.toByteArray();
    }

    private static int getPageCount(List<String> lines, int rowCount) {
        int pageCount = lines.size() / rowCount;
        if (lines.size() % rowCount != 0) {
            pageCount ++;
        }
        return pageCount;
    }

    private static byte[] getFontBuffer() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int size = FONT.read(buf);
        while (size != -1) {
            bos.write(buf, 0, size);
            size = FONT.read(buf);
        }
        bos.close();
        return bos.toByteArray();
    }
}