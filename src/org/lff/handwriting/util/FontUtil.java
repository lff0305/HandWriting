package org.lff.handwriting.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Feifei Liu
 * @datetime 2017-11-27  10:56
 */
public class FontUtil {

    private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
}
