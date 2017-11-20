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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuff on 2017/4/20 9:39
 */
public class PDFUtil {

    private static final int SIZE_LIMIT = 40;

    public static final InputStream FONT = PDFUtil.class.getResourceAsStream("/font/JARDOTTY.ttf");

    public static byte[] getContent(String text, Option option) throws IOException {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(bas));
        Color magentaColor = new DeviceCmyk(0.f, 1.f, 0.f, 0.f);

        String[] lines = text.split("\\n");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int size = FONT.read(buf);
        while (size != -1) {
            bos.write(buf, 0, size);
            size = FONT.read(buf);
        }
        bos.close();



        int pageCount = lines.length / option.getRowCount();
        if (lines.length % option.getRowCount() != 0) {
            pageCount ++;
        }

        for (int i=0; i<pageCount; i++) {

            PdfPage page = pdfDoc.addNewPage();
            PdfCanvas canvas = new PdfCanvas(page);
            int width = (int) page.getPageSize().getWidth();
            int height = (int) page.getPageSize().getHeight();


            int contentSize = height - option.getTopOffset() - option.getBottomOffset();
            float cellHeight = (float)((contentSize + option.getRowGap() - option.getRowCount() * option.getRowGap()) / ( 4.0 * option.getRowCount()));
            option.setCellHeight(cellHeight);

            System.out.println("Width = " + width + " Height = " + height + " \ncellHeight = " + cellHeight);
            System.out.println("RowCount = " + option.getRowCount() + " Gap Size = " + option.getRowGap());
            System.out.println("TopOffset = " + option.getTopOffset());
            System.out.println("BottonOffset = " + option.getBottomOffset());

            for (int j=0; j<option.getRowCount(); j++) {
                String line = "";
                int index = i * option.getRowCount() + j;
                if (index < lines.length) {
                    line = lines[index];
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
                        PdfFont font = PdfFontFactory.createFont(bos.toByteArray(), PdfEncodings.CP1250, true);
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

    private static List<String> parseLines(String txt) {
        String [] words = txt.split(" ");
        List<String> result = new ArrayList<>();
        String s = "";
        for (String word : words) {
            if (s.length() + word.length() + 1 < SIZE_LIMIT) {
                s += word;
                s += " ";
            } else {
                result.add(s);
                s = "";
            }
        }
        if (!s.isEmpty()) {
            result.add(s);
        }
        return result;
    }
}