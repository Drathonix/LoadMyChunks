package com.vicious.persist.shortcuts;

import com.vicious.persist.Persist;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Shortcut class that handles automatic file format migration.
 * @author Jack Andersen
 * @since 1.2.0
 */
public class Migrator {
    /**
     * Migrates a file of any supported extension if it has the same extension-less name.
     * The old file will be deleted and replaced with a new one in the correct format with the same data.
     *
     * @param format the format that will be migrated to.
     * @param relativePath the path of the new file.
     * @return whether migration was successful.
     */
    public static boolean migrate(NotationFormat format, String relativePath) {
        try {
            //TODO: this is a temp work-around, its not clean.
            String pathFileName=Paths.get(relativePath).getFileName().toString();
            String filenameWithoutExtension = pathFileName.substring(0, pathFileName.lastIndexOf('.'));
            File directory = new File(relativePath).getParentFile();
            File[] options = directory.listFiles();
            if(options != null) {
                for (File option : options) {
                    if(option.getName().startsWith(filenameWithoutExtension)) {
                        for (NotationFormat value : NotationFormat.values()) {
                            if (value.isValidFile(option.getName())) {
                                Map<String, Object> map = value.parser.mappify(Files.newInputStream(option.toPath()));
                                option.delete();
                                format.writer.write(map, Files.newOutputStream(new File(relativePath).toPath()));
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            Persist.logger.warning("Could not migrate file");
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
