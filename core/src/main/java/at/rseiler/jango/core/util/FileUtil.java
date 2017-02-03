package at.rseiler.jango.core.util;

import org.apache.commons.lang3.SystemUtils;

public final class FileUtil {
    public static String sanitizeName(String name) {
        if (null == name) {
            return "";
        }

        if (SystemUtils.IS_OS_LINUX) {
            return name.replaceAll("/+", "").trim();
        }

        return name.replaceAll("[\u0001-\u001f<>:\"/\\\\|?*\u007f]+", "").trim();
    }

    private FileUtil() {
    }
}
