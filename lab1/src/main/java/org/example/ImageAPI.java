
package org.example;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageAPI {

    private static final Logger log =
            LoggerFactory.getLogger(ImageAPI.class);

    public enum OSType {
        WINDOWS, MACOS, LINUX, OTHER
    }

    public OSType getOperatingSystemType() {
        String os = System.getProperty("os.name", "generic")
                .toLowerCase(Locale.ENGLISH);

        if (os.contains("mac") || os.contains("darwin")) {
            return OSType.MACOS;
        } else if (os.contains("win")) {
            return OSType.WINDOWS;
        } else if (os.contains("nux")) {
            return OSType.LINUX;
        } else {
            return OSType.OTHER;
        }
    }

    public ImageAPI() throws Exception {
        OSType osType = getOperatingSystemType();

        log.info("Checking OS: {}", osType);

        if (osType == OSType.OTHER) {
            throw new Exception("Unsupported operating system");
        }

        // Автоматическая загрузка OpenCV
        nu.pattern.OpenCV.loadLocally();

        log.info("OpenCV library loaded successfully");
    }
}
