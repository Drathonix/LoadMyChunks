package com.vicious.persist.util;

import java.io.File;

/**
 * Filesystem interaction utility class.
 * @author Jack Andersen
 */
public class FileUtil {
    /**
     * Creates any missing directories for the relative file path.
     */
    public static void resolve(String relativePath) {
        File file = new File(relativePath);
        if(file.getParentFile() != null){
            file.getParentFile().mkdirs();
        }
    }
}
