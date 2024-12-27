package com.vicious.persist.util;

import java.io.File;

public class FileUtil {
    public static void resolve(String fileName) {
        File file = new File(fileName);
        if(file.getParentFile() != null){
            file.getParentFile().mkdirs();
        }
    }
}
