package org.lff.handwriting.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

/**
 * @author Feifei Liu
 * @datetime 2017-11-30  9:48
 */
public class VersionUtil {

    private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static String getVersion() {
        Properties p = new Properties();
        try {
            p.load(VersionUtil.class.getClass().getResourceAsStream("/org/lff/handwriting/version.properties"));
        } catch (IOException e) {
            logger.error("Cannot load version.properties", e);
            return "NA";
        }
        String major = p.getProperty("major");
        String minor = p.getProperty("minor");
        String build = p.getProperty("buildNo");
        String timestamp = p.getProperty("timestamp");
        return major + "." + minor + " build " + build + " " + timestamp;
    }
}
