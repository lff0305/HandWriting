package org.lff.handwriting.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Feifei Liu
 * @datetime 2017-11-27  10:56
 */
public class FontUtil {

    private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static Map<String, Float> ratio = new HashMap<>();

    static {
        ratio.put("JARDOTTY.TTF", 1.7f);
        ratio.put("KD.TTF", 2.1f);
    }

    public static List<String> listFonts() {
        List<String> result = new ArrayList<>();
        try {
            final InputStream is = FontUtil.class.getResourceAsStream("/font");
            final InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            final BufferedReader br = new BufferedReader(isr);
            return br.lines().collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to list fonts", e);
        }
        return result;
    }

    public static void main(String[] main) {
        List<String> list = listFonts();
        System.out.println(list);
    }

    public static void iterateFonts(Consumer<String> f) {
        listFonts().stream().forEach(font -> f.accept(font));
    }

    public static Font getFont(String name) {
        InputStream is = FontUtil.class.getResourceAsStream("/font/" + name);
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font;
        } catch (FontFormatException e) {
            logger.error("Failed to load font", e);
        } catch (IOException e) {
            logger.error("Failed to load font", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return null;
    }

    public static float getRatio(String name) {
        Float f = ratio.get(name.toUpperCase());
        if (f == null) {
            logger.error("Failed to get ratio for {}", name);
            return 1.7f;
        }
        return f;
    }

    public static byte[] getFontBuffer(String fontName) throws IOException {
        InputStream is = FontUtil.class.getResourceAsStream("/font/" + fontName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int size = is.read(buf);
        while (size != -1) {
            bos.write(buf, 0, size);
            size = is.read(buf);
        }
        bos.close();
        return bos.toByteArray();
    }
}
