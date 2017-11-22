package org.lff.handwriting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Feifei Liu
 * @datetime 2017-11-21  11:54
 */
public class ColorUtil {

    private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static Map<String, String> colorMap = new HashMap<>();
    static {
        colorMap.put("MAGENTA", "0xFF00FF");
        colorMap.put("BLACK", "0x000000");
        colorMap.put("RED", "0xFF0000");
        colorMap.put("GRAY", "0x808080");

    }

    public static Color getColor(String lineColor) {
        String code = colorMap.get(lineColor.trim().toUpperCase());
        if (code == null) {
            logger.info("Failed to map color {}", lineColor);
            return Color.BLACK;
        }
        Color c = Color.decode(code);
        return c;
    }
}
