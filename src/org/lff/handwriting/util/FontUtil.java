package org.lff.handwriting.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Feifei Liu
 * @datetime 2017-11-27  10:56
 */
public class FontUtil {

    private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static Map<String, Float> ratio = new HashMap<>();

    private static List<String> fonts = null;

    static {
        ratio.put("JARDOTTY.TTF", 1.7f);
        ratio.put("KD.TTF", 2.1f);
    }

    public synchronized static List<String> listFonts() {
        if (fonts != null) {
            return fonts;
        }
        List<String> result = new ArrayList<>();
        FileSystem fileSystem = null;
        try {
            URI uri = FontUtil.class.getResource("/font").toURI();
            Path myPath;
            if (uri.getScheme().equals("jar")) {
                fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                myPath = fileSystem.getPath("/font");
                Stream<Path> walk = Files.walk(myPath, 1);
                for (Iterator<Path> it = walk.iterator(); it.hasNext();){
                    Path p = it.next();
                    if (p.getNameCount() < 2) {
                        continue;
                    }
                    result.add(p.getName(1).toString());
                }
            } else {
                myPath = Paths.get(uri);
                Stream<Path> walk = Files.walk(myPath, 1);
                for (Iterator<Path> it = walk.iterator(); it.hasNext();){
                    File f = it.next().toFile();
                    if (f.isDirectory()) {
                        continue;
                    }
                    result.add(f.getName());
                }
            }

        } catch (Exception e) {
            logger.error("Failed to list fonts", e);
        } finally {
            if (fileSystem != null) {
                try {
                    fileSystem.close();
                } catch (IOException e) {
                }
            }
        }
        return fonts = result;
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
