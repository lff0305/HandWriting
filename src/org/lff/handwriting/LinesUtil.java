package org.lff.handwriting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feifei Liu
 * @datetime 2017-11-21  9:47
 */
public class LinesUtil {
    public static List<String> build(String text, Option option) {
        List<String> result = new ArrayList<>();
        String[] lines = text.split("\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() && option.isSkipEmptyLine()) {
                continue;
            }
            result.add(line);
            if (option.isAddEmptyLineAfter() && !line.isEmpty()) {
                result.add("");
            }
        }
        return result;
    }
}
